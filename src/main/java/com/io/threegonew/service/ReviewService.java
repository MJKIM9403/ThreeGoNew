package com.io.threegonew.service;

import com.io.threegonew.domain.*;
import com.io.threegonew.dto.*;
import com.io.threegonew.repository.CommentRepository;
import com.io.threegonew.repository.LikesRepository;
import com.io.threegonew.repository.ReviewPhotoRepository;
import com.io.threegonew.repository.ReviewRepository;
import com.io.threegonew.util.AesUtil;
import com.io.threegonew.util.FileHandler;
import com.io.threegonew.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final FileHandler fileHandler;
    private final ReviewPhotoRepository reviewPhotoRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Review saveReview(ReviewBook reviewBook, User user, TourItem tourItem, AddReviewRequest request) throws Exception{
        Review review = Review.builder()
                            .reviewBook(reviewBook)
                            .user(user)
                            .tourItem(tourItem)
                            .tourItemTitle(request.getTouritemTitle())
                            .reviewContent(request.getReviewContent())
                            .build();

        Review savedReview = reviewRepository.save(review);

        List<ReviewPhoto> photoList = fileHandler.parseReviewPhoto(savedReview, request.getPhotoList());

        if(!photoList.isEmpty()) {
            for(ReviewPhoto photo : photoList) {
                // 파일을 DB에 저장
                savedReview.addPhoto(reviewPhotoRepository.save(photo));
            }
        }

        return savedReview;
    }

    @Transactional
    public void updateReview(ReviewBook reviewBook, TourItem tourItem, UpdateReviewRequest request) throws Exception{
        Review review = reviewRepository.findById(request.getReviewId()).orElseThrow(
                () -> new IllegalArgumentException("리뷰 정보를 찾을 수 없습니다."));

        List<ReviewPhoto> copyPhotoList = new ArrayList<>();
        copyPhotoList.addAll(review.getReviewPhotoList());

        if(request.getDeletePhotoId() != null){
            for(Long deleteId : request.getDeletePhotoId()){
                ReviewPhoto deletePhoto = reviewPhotoRepository.findById(deleteId).orElseThrow(
                        () -> new IllegalArgumentException("삭제할 사진 정보를 찾을 수 없습니다."));
                copyPhotoList.remove(deletePhoto);
                reviewPhotoRepository.delete(deletePhoto);
                fileHandler.deleteReviewPhoto(deletePhoto);
            }
        }

        List<ReviewPhoto> addPhotoList = fileHandler.parseReviewPhoto(review, request.getPhotoList());

        if(!addPhotoList.isEmpty()) {
            for(ReviewPhoto photo : addPhotoList) {
                // 파일을 DB에 저장
                copyPhotoList.add(reviewPhotoRepository.save(photo));
            }
        }

        review.update(reviewBook, tourItem, request.getTouritemTitle(), request.getReviewContent(), copyPhotoList);
    }

    @Transactional
    public void deleteReview(Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException("리뷰 정보를 찾을 수 없습니다."));

        List<ReviewPhoto> reviewPhotoList = review.getReviewPhotoList();

        for(ReviewPhoto deletePhoto : reviewPhotoList){
            fileHandler.deleteReviewPhoto(deletePhoto);
        }

        reviewRepository.delete(review);
    }

    @Transactional
    public int viewCountUp(Long reviewId){
        return reviewRepository.increaseViewCount(reviewId);
    }

    public PageResponse getMyReviews(MyPageRequest request){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<MyReviewResponse> page = reviewRepository.findMyReviews(pageable, request.getUserId())
                .map(this::myReviewMapper);

        PageResponse<MyReviewResponse> pageResponse = PageResponse.<MyReviewResponse>withAll()
                .dtoList(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .total(page.getTotalElements())
                .build();

        return pageResponse;
    }

    public Long getMyReviewCount(String userId){
        return reviewRepository.countMyReviews(userId);
    }

    public ReviewResponse getDetailReview(Long reviewId){
        Review findReview = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException("리뷰 정보를 찾을 수 없습니다."));
        try {
            return reviewMapper(findReview);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<SimpleReviewResponse> getReviewsByReviewBook(ReviewBook reviewBook){
        return reviewRepository.findByReviewBook(reviewBook).stream()
                .map(review -> {
                    try {
                        return simpleReviewMapper(review);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }

    @Transactional
    public PageResponse getMyLikeReview(MyPageRequest request){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<MyReviewResponse> page = reviewRepository.findMyLikeReviews(pageable, request.getUserId())
                .map(this::myReviewMapper);

        PageResponse<MyReviewResponse> pageResponse = PageResponse.<MyReviewResponse>withAll()
                .dtoList(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .total(page.getTotalElements())
                .build();

        return pageResponse;
    }

    @Transactional
    public EditReviewResponse getEditReview(Long reviewId){
        Review findReview = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException("리뷰 정보를 찾을 수 없습니다."));

        return editReviewMapper(findReview);
    }

    // 추천 피드
    @Transactional
    public PageResponse getRecommendReview(PageWithFromDateRequest request){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        LocalDateTime fromDate = request.getFromDate();
        LocalDateTime toDate = fromDate.minusHours(72);

        Page<SimpleReviewResponse> page = reviewRepository.findRecommendReviews(pageable, toDate, fromDate)
                .map(this::simpleReviewMapper);

        PageResponse<SimpleReviewResponse> pageResponse = PageResponse.<SimpleReviewResponse>withAll()
                .dtoList(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .total(page.getTotalElements())
                .build();

        return pageResponse;
    }

    // 팔로우 피드
    @Transactional
    public PageResponse getFollowReview(PageWithFromDateRequest request) throws AccessDeniedException{
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        String loginUserId = SecurityUtils.getCurrentUsername();

        if(loginUserId.equals("anonymousUser")){
            throw new AccessDeniedException("유저 정보를 찾을 수 없습니다.");
        }

        Page<SimpleReviewResponse> page = reviewRepository.findFollowReview(pageable, loginUserId, request.getFromDate())
                .map(this::simpleReviewMapper);

        PageResponse<SimpleReviewResponse> pageResponse = PageResponse.<SimpleReviewResponse>withAll()
                .dtoList(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .total(page.getTotalElements())
                .build();

        return pageResponse;
    }

    @Transactional
    public PageResponse getRecommendReviewByKeyword(PageWithFromDateRequest request){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        LocalDateTime fromDate = request.getFromDate();
        LocalDateTime toDate = fromDate.minusHours(72);
        String keyword = "%" + request.getKeyword() + "%";
        Page<SimpleReviewResponse> page = reviewRepository.findRecommendReviewsByKeyword(pageable, keyword, toDate, fromDate)
                .map(this::simpleReviewMapper);

        PageResponse<SimpleReviewResponse> pageResponse = PageResponse.<SimpleReviewResponse>withAll()
                .dtoList(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .total(page.getTotalElements())
                .build();

        return pageResponse;
    }

    private SimpleReviewResponse simpleReviewMapper(Review review){
        String loginUserId = SecurityUtils.getCurrentUsername();
        return SimpleReviewResponse.builder()
                .reviewId(review.getReviewId())
                .userInfo(userInfoMapper(review.getUser()))
                .tourItemTitle(review.getTourItemTitle())
                .reviewContent(review.getReviewContent().replace("\r\n","<br/>"))
                .reviewPhotoList(review.getReviewPhotoList().stream()
                        .map(this::reviewPhotoMapper).collect(Collectors.toList()))
                .viewCount(review.getViewCount())
                .commentCount(commentRepository.countCommentsByReviewId(review.getReviewId()))
                .likeCount(likesRepository.countByReviewId(review.getReviewId()))
                .likeState(likesRepository.existsByUserIdAndReviewId(loginUserId ,review.getReviewId()))
                .build();
    }

    private MyReviewResponse myReviewMapper(Review review){
        return MyReviewResponse.builder()
                .reviewId(review.getReviewId())
                .firstPhoto(reviewPhotoMapper(review.getReviewPhotoList().get(0)))
                .photoCount(review.getReviewPhotoList().size())
                .likeCount(likesRepository.countByReviewId(review.getReviewId()))
                .commentCount(commentRepository.countCommentsByReviewId(review.getReviewId()))
                .build();
    }

    private ReviewResponse reviewMapper(Review review) throws Exception {
        String bookId = null;
        String bookTitle = null;
        String bookCoverImg = null;
        if(review.getReviewBook() != null) {
            bookId = review.getReviewBook().getBookId().toString();
            bookId = AesUtil.aesCBCEncode(bookId);
            bookTitle = review.getReviewBook().getBookTitle();
            bookCoverImg = review.getReviewBook().getCoverFilePath();
        }
        String touritemId = review.getTourItem() == null ? null : review.getTourItem().getContentid();

        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .reviewBookId(bookId)
                .reviewBookTitle(bookTitle)
                .reviewBookCoverImg(bookCoverImg)
                .userInfo(userInfoMapper(review.getUser()))
                .tourItemId(touritemId)
                .tourItemTitle(review.getTourItemTitle())
                .reviewContent(review.getReviewContent().replace("\r\n", "<br>"))
                .viewCount(review.getViewCount())
                .reviewPhotoList(review.getReviewPhotoList().stream()
                                .map(this::reviewPhotoMapper).collect(Collectors.toList()))
                .regDate(review.getRegDate())
                .modDate(review.getModDate())
                .build();
    }

    private EditReviewResponse editReviewMapper(Review review){
        Long bookId = null;
        String bookTitle = null;
        if(review.getReviewBook() != null) {
            bookId = review.getReviewBook().getBookId();
            bookTitle = review.getReviewBook().getBookTitle();
        }
        String touritemId = review.getTourItem() == null ? null : review.getTourItem().getContentid();

        return EditReviewResponse.builder()
                .reviewId(review.getReviewId())
                .reviewBookId(bookId)
                .reviewBookTitle(bookTitle)
                .tourItemId(touritemId)
                .tourItemTitle(review.getTourItemTitle())
                .reviewContent(review.getReviewContent())
                .reviewPhotoList( review.getReviewPhotoList().stream()
                        .map(this::reviewPhotoMapper).collect(Collectors.toList()))
                .build();
    }

    private ReviewPhotoResponse reviewPhotoMapper(ReviewPhoto reviewPhoto) {
        return ReviewPhotoResponse.builder()
                .fileId(reviewPhoto.getFileId())
                .filePath(reviewPhoto.getFilePath())
                .build();
    }

    private UserInfoResponse userInfoMapper(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImg(user.getU_sfile())
                .about(user.getAbout())
                .build();
    }
}

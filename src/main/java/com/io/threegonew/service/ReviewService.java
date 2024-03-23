package com.io.threegonew.service;

import com.io.threegonew.domain.*;
import com.io.threegonew.dto.*;
import com.io.threegonew.repository.ReviewPhotoRepository;
import com.io.threegonew.repository.ReviewRepository;
import com.io.threegonew.util.FileHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final FileHandler fileHandler;
    private final ReviewPhotoRepository reviewPhotoRepository;

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

        List<ReviewPhoto> photoList = fileHandler.parseFileInfo(savedReview, request.getPhotoList());

        if(!photoList.isEmpty()) {
            for(ReviewPhoto photo : photoList) {
                // 파일을 DB에 저장
                savedReview.addPhoto(reviewPhotoRepository.save(photo));
            }
        }

        return savedReview;
    }

    public PageResponse findMyReview(MyPageRequest request){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<MyReviewResponse> page = reviewRepository.findMyReview(pageable, request.getUserId())
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

    public ReviewResponse findDetailReview(Long reviewId){
        Review findReview = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException("리뷰 정보를 찾을 수 없습니다."));
        return reviewMapper(findReview);
    }

    private MyReviewResponse myReviewMapper(Review review){
        return MyReviewResponse.builder()
                .reviewId(review.getReviewId())
                .firstPhoto(reviewPhotoMapper(review.getReviewPhotoList().get(0)))
                .photoCount(review.getReviewPhotoList().size())
                .likeCount(0)   /* TODO: 좋아요, 댓글 구현 후 값 바꿀 것*/
                .commentCount(0)
                .build();
    }

    private ReviewResponse reviewMapper(Review review){
        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .reviewBookId(review.getReviewBook().getBookId())
                .reviewBookTitle(review.getReviewBook().getBookTitle())
                .reviewBookCoverImg(review.getReviewBook().getCoverFilePath())
                .userInfo(userInfoResponse(review.getUser()))
                .tourItemId(review.getTourItem().getContentid())
                .tourItemTitle(review.getTourItemTitle())
                .reviewContent(review.getReviewContent().replace("\r\n", "<br>"))
                .viewCount(review.getViewCount())
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

    private UserInfoResponse userInfoResponse(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImg(user.getU_sfile())
                .about(user.getAbout())
                .build();
    }
}

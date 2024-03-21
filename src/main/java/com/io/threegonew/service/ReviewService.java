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

    @Transactional
    public PageResponse findMyReview(MyPageRequest request){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<ReviewResponse> page = reviewRepository.findMyReview(pageable, request.getUserId())
                .map(this::reviewMapper);

        PageResponse<ReviewResponse> pageResponse = PageResponse.<ReviewResponse>withAll()
                .dtoList(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .total(page.getTotalElements())
                .build();

        return pageResponse;
    }

    private ReviewResponse reviewMapper(Review review){
        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .reviewBookId(review.getReviewBook().getBookId())
                .reviewBookTitle(review.getReviewBook().getBookTitle())
                .userInfo(userInfoResponse(review.getUser()))
                .tourItemId(review.getTourItem().getContentid())
                .tourItemTitle(review.getTourItemTitle())
                .reviewContent(review.getReviewContent())
                .viewCount(review.getViewCount())
                .reviewPhotoList(review.getReviewPhotoList())
                .build();
    }

    private UserInfoResponse userInfoResponse(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImg("../assets/img/profileimg/" + user.getU_sfile())
                .about(user.getAbout())
                .build();
    }
}

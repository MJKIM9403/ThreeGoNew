package com.io.threegonew.service;

import com.io.threegonew.domain.*;
import com.io.threegonew.dto.AddReviewRequest;
import com.io.threegonew.repository.ReviewPhotoRepository;
import com.io.threegonew.repository.ReviewRepository;
import com.io.threegonew.util.FileHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

        List<ReviewPhoto> photoList = fileHandler.parseFileInfo(request.getPhotoList());

        if(!photoList.isEmpty()) {
            for(ReviewPhoto photo : photoList) {
                // 파일을 DB에 저장
                review.addPhoto(reviewPhotoRepository.save(photo));
            }
        }

        return reviewRepository.save(review);
    }
}

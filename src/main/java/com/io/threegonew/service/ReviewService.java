package com.io.threegonew.service;

import com.io.threegonew.domain.Review;
import com.io.threegonew.dto.AddReviewRequest;
import com.io.threegonew.repository.ReviewBookRepository;
import com.io.threegonew.repository.ReviewRepository;
import com.io.threegonew.repository.TourItemRepository;
import com.io.threegonew.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private ReviewRepository reviewRepository;

//    public Review saveReview(AddReviewRequest request) {
//        Review review = Review.builder()
//
//    }
}

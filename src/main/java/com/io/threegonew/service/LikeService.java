package com.io.threegonew.service;

import com.io.threegonew.domain.Like;
import com.io.threegonew.domain.Review;
import com.io.threegonew.domain.User;
import com.io.threegonew.repository.LikeRepository;
import com.io.threegonew.repository.ReviewRepository;
import com.io.threegonew.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public Like addLike(Long reviewId, String loginUserId) throws Exception {
        User user = userRepository.findById(loginUserId).orElseThrow(()->
                new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->
                new IllegalArgumentException("리뷰 정보를 찾을 수 없습니다."));

        if(likeRepository.existsByUserAndReview(user, review)){
            throw new Exception();
        }
        return likeRepository.save(Like.builder()
                                    .user(user)
                                    .review(review)
                                    .build());
    }

    @Transactional
    public void deleteLike(Long reviewId, String loginUserId) {
        User user = userRepository.findById(loginUserId).orElseThrow(()->
                new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->
                new IllegalArgumentException("리뷰 정보를 찾을 수 없습니다."));

        Like like = likeRepository.findByUserAndReview(user, review).orElseThrow(()->
                new IllegalArgumentException("좋아요 정보를 찾을 수 없습니다."));

        likeRepository.delete(like);
    }
}

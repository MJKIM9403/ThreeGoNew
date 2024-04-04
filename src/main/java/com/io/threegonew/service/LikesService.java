package com.io.threegonew.service;

import com.io.threegonew.domain.Likes;
import com.io.threegonew.repository.LikesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;

    @Transactional
    public Likes addLike(String loginUserId, Long reviewId) throws Exception {

        if(getLikeState(loginUserId, reviewId)){
            throw new Exception();
        }
        return likesRepository.save(Likes.builder()
                            .userId(loginUserId)
                            .reviewId(reviewId)
                            .build());
    }

    @Transactional
    public void deleteLike(String loginUserId, Long reviewId) {
        Likes like = getLikeByUserIdAndReviewId(loginUserId, reviewId);

        likesRepository.delete(like);
    }

    public Likes getLikeByUserIdAndReviewId(String loginUserId, Long reviewId) {
        return likesRepository.findByUserIdAndReviewId(loginUserId, reviewId).orElseThrow(() ->
                new IllegalArgumentException("좋아요 정보를 찾을 수 없습니다.")
        );
    }

    public boolean getLikeState(String loginUserId, Long reviewId){
        return likesRepository.existsByUserIdAndReviewId(loginUserId, reviewId);
    }

    public Long getLikeCount(Long reviewId){
        return likesRepository.countByReviewId(reviewId);
    }
}

package com.io.threegonew.service;

import com.io.threegonew.domain.Likes;
import com.io.threegonew.repository.LikesRepository;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;

    @Transactional
    public Likes addLike(String loginUserId, Long reviewId) throws DuplicateRequestException {

        if(getLikeState(loginUserId, reviewId)){
            throw new DuplicateRequestException();
        }
        return likesRepository.save(Likes.builder()
                            .userId(loginUserId)
                            .reviewId(reviewId)
                            .build());
    }

    @Transactional
    public void deleteLike(String loginUserId, Long reviewId) throws AccessDeniedException{
        Likes like = getLikeByUserIdAndReviewId(loginUserId, reviewId);
        if(!like.getUserId().equals(loginUserId)){
            throw new AccessDeniedException("관심리뷰 등록 삭제 권한이 없습니다.");
        }
        likesRepository.delete(like);
    }

    public Likes getLikeByUserIdAndReviewId(String loginUserId, Long reviewId) {
        return likesRepository.findByUserIdAndReviewId(loginUserId, reviewId).orElseThrow(() ->
                new IllegalArgumentException("관심리뷰 정보를 찾을 수 없습니다.")
        );
    }

    public boolean getLikeState(String loginUserId, Long reviewId){
        return likesRepository.existsByUserIdAndReviewId(loginUserId, reviewId);
    }

    public Long getLikeCount(Long reviewId){
        return likesRepository.countByReviewId(reviewId);
    }
}

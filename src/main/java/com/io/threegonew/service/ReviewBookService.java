package com.io.threegonew.service;

import com.io.threegonew.domain.ReviewBook;
import com.io.threegonew.repository.ReviewBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewBookService {
    private ReviewBookRepository reviewBookRepository;

    public ReviewBook findReviewBook(Long reviewBookId){
        return reviewBookRepository.findById(reviewBookId).orElseThrow(
                () -> new IllegalArgumentException("리뷰북 정보를 찾을 수 없습니다.")
        );
    }
}

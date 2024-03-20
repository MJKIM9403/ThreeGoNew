package com.io.threegonew.service;

import com.io.threegonew.domain.Planner;
import com.io.threegonew.domain.ReviewBook;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.AddReviewBookRequest;
import com.io.threegonew.dto.ReviewBookResponse;
import com.io.threegonew.repository.ReviewBookRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewBookService {

    private final ReviewBookRepository reviewBookRepository;
    private final ModelMapper modelMapper;

    public ReviewBook createReviewBook(User user, Planner planner, AddReviewBookRequest request){
        ReviewBook reviewBook = ReviewBook.builder()
                                        .user(user)
                                        .planner(planner)
                                        .bookTitle(request.getBookTitle())
                                        .bookContent(request.getBookContent())
                                        /*.coverOfile()
                                        .coverFilePath()*/
                                        .build();

        return reviewBookRepository.save(reviewBook);
    }

    public ReviewBook findReviewBook(Long reviewBookId){
        return reviewBookRepository.findById(reviewBookId).orElseThrow(
                () -> new IllegalArgumentException("리뷰북 정보를 찾을 수 없습니다.")
        );
    }

    public List<ReviewBookResponse> findMyReviewBookList(User user){
        List<ReviewBookResponse> bookResponseList =
                reviewBookRepository.findByUser(user).stream()
                        .map(reviewBook -> modelMapper.map(reviewBook, ReviewBookResponse.class))
                        .collect(Collectors.toList());
        return bookResponseList;
    }
}

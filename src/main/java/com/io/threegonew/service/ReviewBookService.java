package com.io.threegonew.service;

import com.io.threegonew.domain.Planner;
import com.io.threegonew.domain.ReviewBook;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.*;
import com.io.threegonew.repository.ReviewBookRepository;
import com.io.threegonew.util.AesUtil;
import com.io.threegonew.util.FileHandler;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewBookService {

    private final ReviewBookRepository reviewBookRepository;
    private final FileHandler fileHandler;
    private final ModelMapper modelMapper;

    public ReviewBook createReviewBook(User user, Planner planner, AddReviewBookRequest request) throws Exception {
        ReviewBook reviewBook = ReviewBook.builder()
                                        .user(user)
                                        .planner(planner)
                                        .bookTitle(request.getBookTitle())
                                        .bookContent(request.getBookContent())
                                        .build();

        if(request.getCoverFile() != null){
            fileHandler.updateBookCover(reviewBook, request.getCoverFile());
        }

        return reviewBookRepository.save(reviewBook);
    }

    public ReviewBook findReviewBook(Long reviewBookId){
        return reviewBookRepository.findById(reviewBookId).orElseThrow(
                () -> new IllegalArgumentException("리뷰북 정보를 찾을 수 없습니다.")
        );
    }

    public ReviewBookResponse findReviewBookResponse(Long reviewBookId){
        return reviewBookMapper(findReviewBook(reviewBookId));
    }

    public PageResponse findMyReviewBook(MyPageRequest request){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<MyReviewBookResponse> page = reviewBookRepository.findMyReviewBook(pageable, request.getUserId())
                .map(reviewBook -> {
                    try {
                        return myReviewBookMapper(reviewBook);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        PageResponse<MyReviewBookResponse> pageResponse = PageResponse.<MyReviewBookResponse>withAll()
                .dtoList(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .total(page.getTotalElements())
                .build();

        return pageResponse;
    }

    private MyReviewBookResponse myReviewBookMapper(ReviewBook reviewBook) throws Exception {
        return MyReviewBookResponse.builder()
                                    .bookId(AesUtil.aesCBCEncode(reviewBook.getBookId().toString()))
                                    .bookTitle(reviewBook.getBookTitle())
                                    .coverFilePath(reviewBook.getCoverFilePath())
                                    .build();
    }

    public List<ReviewBookResponse> findReviewBookByUser(User user){
        List<ReviewBookResponse> bookResponseList =
                reviewBookRepository.findByUser(user).stream()
                        .map(this::reviewBookMapper)
                        .collect(Collectors.toList());
        return bookResponseList;
    }

    private ReviewBookResponse reviewBookMapper(ReviewBook reviewBook){
        String content = reviewBook.getBookContent() == null ? null : reviewBook.getBookContent().replace("\r\n","<br>");
        return ReviewBookResponse.builder()
                .bookId(reviewBook.getBookId())
                .user(userInfoMapper(reviewBook.getUser()))
                .planner(plannerMapper(reviewBook.getPlanner()))
                .bookTitle(reviewBook.getBookTitle())
                .bookContent(content)
                .coverOfile(reviewBook.getCoverOfile())
                .coverFilePath(reviewBook.getCoverFilePath())
                .build();
    }

    private PlannerResponse plannerMapper(Planner planner){
        return PlannerResponse.builder()
                .plannerId(planner.getPlannerId())
                .userId(planner.getUserId())
                .plannerName(planner.getPlannerName())
                .startDate(planner.getStartDate())
                .endDate(planner.getEndDate())
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

package com.io.threegonew.service;

import com.io.threegonew.domain.Bookmark;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.BookmarkRequest;
import com.io.threegonew.repository.BookmarkRepository;
import com.io.threegonew.repository.TourItemRepository;
import com.io.threegonew.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final TourItemRepository tourItemRepository;

    public Bookmark addBookmark(BookmarkRequest bookmarkRequest){
        User user = userRepository.findById(bookmarkRequest.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
        TourItem tourItem = tourItemRepository.findById(bookmarkRequest.getContentId()).orElseThrow(() ->
                new IllegalArgumentException("관광지 정보를 찾을 수 없습니다."));

        return bookmarkRepository.save(Bookmark.builder()
                .tourItem(tourItem)
                .user(user)
                .build());
    }

    public Long bookmarkCount(String contentId){
        TourItem tourItem = tourItemRepository.findById(contentId).orElseThrow(() ->
                new IllegalArgumentException("관광지 정보를 찾을 수 없습니다."));
        return bookmarkRepository.countBookmarkByTourItem(tourItem);
    }

    public Optional<Bookmark> findBookmark(BookmarkRequest bookmarkRequest){
        User user = userRepository.findById(bookmarkRequest.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
        TourItem tourItem = tourItemRepository.findById(bookmarkRequest.getContentId()).orElseThrow(() ->
                new IllegalArgumentException("관광지 정보를 찾을 수 없습니다."));

        return bookmarkRepository.findByTourItemAndUser(tourItem, user);
    }

    public void deleteBookmark(Bookmark bookmark){
        bookmarkRepository.delete(bookmark);
    }
}

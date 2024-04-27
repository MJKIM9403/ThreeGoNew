package com.io.threegonew.service;

import com.io.threegonew.domain.Bookmark;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.domain.User;
import com.io.threegonew.repository.BookmarkRepository;
import com.io.threegonew.repository.TourItemRepository;
import com.io.threegonew.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final TourItemRepository tourItemRepository;

    public Bookmark addBookmark(String loginUserId, String contentId){
        if(existBookmark(loginUserId, contentId)){
            throw new DuplicateRequestException();
        }

        User user = userRepository.findById(loginUserId).orElseThrow(() ->
                new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
        TourItem tourItem = tourItemRepository.findById(contentId).orElseThrow(() ->
                new IllegalArgumentException("관광지 정보를 찾을 수 없습니다."));

        return bookmarkRepository.save(Bookmark.builder()
                .tourItem(tourItem)
                .user(user)
                .build());
    }

    public Long countBookmark(String contentId){
        TourItem tourItem = tourItemRepository.findById(contentId).orElseThrow(() ->
                new IllegalArgumentException("관광지 정보를 찾을 수 없습니다."));
        return bookmarkRepository.countBookmarkByTourItem(tourItem);
    }

    public Bookmark findBookmark(String loginUserId, String contentId){
        User user = userRepository.findById(loginUserId).orElseThrow(() ->
                new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
        TourItem tourItem = tourItemRepository.findById(contentId).orElseThrow(() ->
                new IllegalArgumentException("관광지 정보를 찾을 수 없습니다."));

        return bookmarkRepository.findByTourItemAndUser(tourItem, user).orElseThrow(() ->
                new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
    }

    public boolean existBookmark(String loginUserId, String contentId){
        User user = userRepository.findById(loginUserId).orElseThrow(() ->
                new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
        TourItem tourItem = tourItemRepository.findById(contentId).orElseThrow(() ->
                new IllegalArgumentException("관광지 정보를 찾을 수 없습니다."));

        return bookmarkRepository.existsBookmarkByTourItemAndUser(tourItem, user);
    }

    public void deleteBookmark(String loginUserId, String contentId) throws AccessDeniedException{
        Bookmark bookmark = findBookmark(loginUserId, contentId);
        if(!bookmark.getUser().getId().equals(loginUserId)){
            throw new AccessDeniedException("북마크 삭제 권한이 없습니다.");
        }
        bookmarkRepository.delete(bookmark);
    }



}

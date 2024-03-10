package com.io.threegonew.controller;

import com.io.threegonew.domain.Bookmark;
import com.io.threegonew.dto.BookmarkRequest;
import com.io.threegonew.dto.BookmarkResponse;
import com.io.threegonew.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/bookmark")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("/check")
    public ResponseEntity<BookmarkResponse> checkBookmark(@RequestBody BookmarkRequest request){
        Optional<Bookmark> bookmark = bookmarkService.findBookmark(request);
        if(request.getUserId().equals("anonymousUser")){
            return ResponseEntity.ok(BookmarkResponse.builder()
                    .bookmarkCount(bookmarkService.bookmarkCount(request.getContentId()))
                    .isChecked(false)
                    .build());
        }else {
            boolean isBookmarkChecked = bookmark.isPresent();
            if(isBookmarkChecked){
                bookmarkService.deleteBookmark(bookmark.get());
                System.out.println("북마크를 삭제했습니다.");
            }else {
                bookmarkService.addBookmark(request);
                System.out.println("북마크를 추가했습니다.");
            }
            return ResponseEntity.ok(BookmarkResponse.builder()
                    .bookmarkCount(bookmarkService.bookmarkCount(request.getContentId()))
                    .isChecked(isBookmarkChecked)
                    .build());
        }
    }
}

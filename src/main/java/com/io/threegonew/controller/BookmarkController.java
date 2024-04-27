package com.io.threegonew.controller;

import com.io.threegonew.commons.SecurityUtils;
import com.io.threegonew.domain.Bookmark;
import com.io.threegonew.dto.BookmarkResponse;
import com.io.threegonew.dto.ErrorResponse;
import com.io.threegonew.service.BookmarkService;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("/contents/{contentId}/bookmarks")
    public ResponseEntity checkBookmark(@PathVariable(value = "contentId") String contentId){
        try{
            String loginUserId = SecurityUtils.getCurrentUsername();
            if(loginUserId.equals("anonymousUser")){
                return ResponseEntity.ok(BookmarkResponse.builder()
                        .bookmarkCount(bookmarkService.countBookmark(contentId))
                        .bookmarkState(false)
                        .build());
            }else {
                boolean isBookmarkChecked = bookmarkService.existBookmark(loginUserId, contentId);
                if(isBookmarkChecked){
                    // 북마크 제거
                    bookmarkService.deleteBookmark(loginUserId, contentId);
                }else {
                    // 북마크 추가
                    bookmarkService.addBookmark(loginUserId, contentId);
                }
                return ResponseEntity.ok(BookmarkResponse.builder()
                        .bookmarkCount(bookmarkService.countBookmark(contentId))
                        .bookmarkState(bookmarkService.existBookmark(loginUserId, contentId))
                        .build());
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400", "북마크 정보를 조회할 수 없습니다.");
        }catch (AccessDeniedException e) {
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.FORBIDDEN, "403", "북마크 삭제 권한이 없습니다.");
        }catch (DuplicateRequestException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.CONFLICT, "409", "이미 존재하는 요청입니다.");
        }
    }
}

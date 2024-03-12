package com.io.threegonew.controller;

import com.io.threegonew.dto.MyBookmarkRequest;
import com.io.threegonew.dto.MyPageResponse;
import com.io.threegonew.dto.PageResponse;
import com.io.threegonew.dto.TourItemResponse;
import com.io.threegonew.service.TourItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyPageApiController {
    private final TourItemService tourItemService;

    @PostMapping("/bookmark")
    public ResponseEntity<MyPageResponse> getMyBookmark(@RequestBody MyBookmarkRequest request){
        MyPageResponse<PageResponse<TourItemResponse>> myPageResponse =
                MyPageResponse.builder()
                        .type("bookmark")
                        .pageResponse(tourItemService.findMyBookmark(request))
                        .build();
        return ResponseEntity.ok().body(myPageResponse);
    }
}

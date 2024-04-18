package com.io.threegonew.controller;

import com.io.threegonew.domain.Bookmark;
import com.io.threegonew.domain.Cat2;
import com.io.threegonew.domain.Cat3;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.BookmarkService;
import com.io.threegonew.service.TourItemContentService;
import com.io.threegonew.service.TourItemContentService2;
import com.io.threegonew.service.TourItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/info")
@RequiredArgsConstructor
public class TourItemController {

    private final TourItemService tourItemService;
    private final TourItemContentService tourItemContentService;
    private final TourItemContentService2 tourItemContentService2;
    private final BookmarkService bookmarkService;

    @GetMapping("/area")
    public String getArea(){
        return "tourinfo/area";
    }

    @GetMapping("/city/{areaCode}")
    public String getSelectList(@PathVariable(name = "areaCode") Integer areaCode, Model model){
        TourItemSelectRequest request = TourItemSelectRequest.builder().areaCode(String.valueOf(areaCode)).build();
        PageResponse pageResponse = tourItemService.findSelectedTourItemList(request);
        model.addAttribute("areaList", tourItemService.findAreaList());
        model.addAttribute("cat1List", tourItemService.findCat1List());
        model.addAttribute("cat2List", new ArrayList<Cat2>());
        model.addAttribute("cat3List", new ArrayList<Cat3>());
        model.addAttribute("sigunguList", tourItemService.findSigunguList(areaCode));
        model.addAttribute("contentTypeList", tourItemService.findContentTypeList());
        model.addAttribute("selectedArea", tourItemService.findArea(areaCode));
        model.addAttribute("pageResponse", pageResponse);

        return "tourinfo/city";
    }

    @PostMapping("/api/cat2")
    public String getCat2List(@RequestBody TourItemSelectRequest request, Model model){
        if(request.getCat1() != null){
            model.addAttribute("cat2List", tourItemService.findCat2List(request.getCat1()));
            model.addAttribute("cat3List", new ArrayList<>());
        }else {
            model.addAttribute("cat2List", new ArrayList<>());
            model.addAttribute("cat3List", new ArrayList<>());
        }

        return "tourinfo/city :: #category-middle";
    }
    @PostMapping("/api/cat3")
    public String getCat3List(@RequestBody TourItemSelectRequest request, Model model){
        if(request.getCat2() != null){
            model.addAttribute("cat3List", tourItemService.findCat3List(request.getCat2()));
        }else {
            model.addAttribute("cat3List", new ArrayList<>());
        }

        return "tourinfo/city :: #category-row";
    }

    @GetMapping("/content/{contentid}")
    public String getContentInfo(@PathVariable(name = "contentid") String contentid, Model model){
        TourItemResponse tourItemResponse = tourItemService.findTourItemResponse(contentid);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = "";
        boolean isBookmarkChecked = false;

        if (principal.equals("anonymousUser")) {
            userId = "anonymousUser";
        }else {
            UserDetails userDetails = (UserDetails)principal;
            userId = userDetails.getUsername();
            Optional<Bookmark> bookmark = bookmarkService.findBookmark(BookmarkRequest.builder()
                                        .contentId(contentid)
                                        .userId(userId)
                                        .build());
            isBookmarkChecked = bookmark.isPresent();
        }

        TourItemContentResponse tourItemContentResponse = tourItemContentService2.getContentInfo(tourItemResponse).block();

        model.addAttribute("response", tourItemContentResponse);
        model.addAttribute("userId", userId);
        model.addAttribute("isBookmarkChecked", isBookmarkChecked);
        return "tourinfo/content";
    }


    @PostMapping("/api/touritems")
    public String getTourItemList(@RequestBody TourItemSelectRequest request, Model model) {
        PageResponse pageResponse = tourItemService.findSelectedTourItemList(request);
        model.addAttribute("pageResponse", pageResponse);
        return "tourinfo/city :: #touritems";
    }


}

package com.io.threegonew.controller;

import com.io.threegonew.commons.SecurityUtils;
import com.io.threegonew.domain.Cat2;
import com.io.threegonew.domain.Cat3;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.BookmarkService;
import com.io.threegonew.service.TourItemContentService;
import com.io.threegonew.service.TourItemContentService2;
import com.io.threegonew.service.TourItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/tour")
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
        TourItemSelectRequest request = TourItemSelectRequest.builder().area(String.valueOf(areaCode)).build();
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

    @GetMapping("/cat2")
    public String getCat2List(@RequestParam(value = "cat1") String cat1, Model model){
        if(!cat1.isEmpty()){
            model.addAttribute("cat2List", tourItemService.findCat2List(cat1));
            model.addAttribute("cat3List", new ArrayList<>());
        }else {
            model.addAttribute("cat2List", new ArrayList<>());
            model.addAttribute("cat3List", new ArrayList<>());
        }

        return "tourinfo/city :: #category-middle";
    }
    @GetMapping("/cat3")
    public String getCat3List(@RequestParam(value = "cat2") String cat2, Model model){
        if(!cat2.isEmpty()){
            model.addAttribute("cat3List", tourItemService.findCat3List(cat2));
        }else {
            model.addAttribute("cat3List", new ArrayList<>());
        }

        return "tourinfo/city :: #category-row";
    }

    @GetMapping("/touritems")
    public String getTourItemList(@ModelAttribute TourItemSelectRequest request, Model model) {
        PageResponse pageResponse = tourItemService.findSelectedTourItemList(request);
        model.addAttribute("pageResponse", pageResponse);
        return "tourinfo/city :: #touritems";
    }

    @GetMapping("/content/{contentid}")
    public String getContentInfo(@PathVariable(name = "contentid") String contentid, Model model){
        TourItemResponse tourItemResponse = tourItemService.findTourItemResponse(contentid);

        String userId = SecurityUtils.getCurrentUsername();
        boolean isBookmarkChecked = false;

        if (!userId.equals("anonymousUser")) {
            isBookmarkChecked = bookmarkService.existBookmark(userId, contentid);
        }

        TourItemContentResponse tourItemContentResponse = tourItemContentService2.getContentInfo(tourItemResponse);

        model.addAttribute("response", tourItemContentResponse);
        model.addAttribute("userId", userId);
        model.addAttribute("isBookmarkChecked", isBookmarkChecked);
        return "tourinfo/content";
    }

}

package com.io.threegonew.controller;

import com.io.threegonew.domain.Cat2;
import com.io.threegonew.domain.Cat3;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.dto.TourItemResponse;
import com.io.threegonew.dto.TourItemSelectRequest;
import com.io.threegonew.service.TourItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/info")
@RequiredArgsConstructor
public class TourItemController {

    private final TourItemService tourItemService;

    @GetMapping("/area")
    public String getArea(){
        return "tourinfo/area";
    }

    @GetMapping("/city/{areaCode}")
    public String getSelectList(@PathVariable(name = "areaCode") Integer areaCode, Model model){
        TourItemSelectRequest request = TourItemSelectRequest.builder().areaCode(String.valueOf(areaCode)).build();
        Page<TourItemResponse> page = tourItemService.findSelectedTourItemList(request);
        model.addAttribute("areaList", tourItemService.findAreaList());
        model.addAttribute("cat1List", tourItemService.findCat1List());
        model.addAttribute("cat2List", new ArrayList<Cat2>());
        model.addAttribute("cat3List", new ArrayList<Cat3>());
        model.addAttribute("sigunguList", tourItemService.findSigunguList(areaCode));
        model.addAttribute("contentTypeList", tourItemService.findContentTypeList());
        model.addAttribute("selectedArea", tourItemService.findArea(areaCode));
        model.addAttribute("tourItemList", page.getContent());
        model.addAttribute("pages", page);

        return "tourinfo/city";
    }

    @PostMapping("/api/cat2")
    public String getCat2List(@RequestBody TourItemSelectRequest request, Model model){
        if(request.getCat1() != null){
            model.addAttribute("cat2List", tourItemService.findCat2List(request.getCat1()));
        }else {
            model.addAttribute("cat2List", new ArrayList<>());
        }

        return "tourinfo/city :: #category-cat2";
    }
    @PostMapping("/api/cat3")
    public String getCat3List(@RequestBody TourItemSelectRequest request, Model model){
        if(request.getCat2() != null){
            model.addAttribute("cat3List", tourItemService.findCat3List(request.getCat2()));
        }else {
            model.addAttribute("cat3List", new ArrayList<>());
        }

        return "tourinfo/city :: #category-cat3";
    }

//    @GetMapping("/api/touritems")
//    @ResponseBody
//    public ResponseEntity<Map<String,Object>> getTourItemList(@RequestParam Map<String, String> params) {
//        Map<String,Object> returnData = new HashMap<>();
//        Map<String,Object> data = new HashMap<>();
//        TourItemSelectRequest request = TourItemSelectRequest.builder()
//                .areaCode(params.get("areaCode"))
//                .sigunguCode(params.get("sigunguCode"))
//                .cat1(params.get("cat1"))
//                .cat2(params.get("cat2"))
//                .cat3(params.get("cat3"))
//                .contentTypeId(params.get("contentTypeId"))
//                .build();
//        List<TourItem> tourItemList = tourItemService.findSelectedTourItemList(request);
//        data.put("contents", tourItemList);
//
//        returnData.put("result", true);
//        returnData.put("data", data);
//
//
//        return ResponseEntity.ok().body(returnData);
//    }
    @PostMapping("/api/touritems")
    public String getTourItemList(@RequestBody TourItemSelectRequest request, Model model) {
        Page<TourItemResponse> page = tourItemService.findSelectedTourItemList(request);
        model.addAttribute("tourItemList", page.getContent());
        model.addAttribute("pages", page);
        return "tourinfo/city :: #touritems";
    }


}

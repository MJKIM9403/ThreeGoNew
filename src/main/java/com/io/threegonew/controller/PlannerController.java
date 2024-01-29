package com.io.threegonew.controller;

import com.io.threegonew.domain.Cat2;
import com.io.threegonew.domain.Cat3;
import com.io.threegonew.dto.PageResponse;
import com.io.threegonew.dto.TourItemSelectRequest;
import com.io.threegonew.service.TourItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("plan")
@RequiredArgsConstructor
public class PlannerController {
    private final TourItemService tourItemService;


    @GetMapping(value = {"/city/{areaCode}/{sigunguCode}","/city/{areaCode}"})
    public String getSelectList(@PathVariable(name = "areaCode") Integer areaCode,
                                @PathVariable(name = "sigunguCode", required = false) Integer sigunguCode,
                                Model model){
        if(sigunguCode == null) {
            sigunguCode = 0;
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

        } else {
            TourItemSelectRequest request = TourItemSelectRequest.builder().areaCode(String.valueOf(areaCode)).sigunguCode(String.valueOf(sigunguCode)).build();
            PageResponse pageResponse = tourItemService.findSelectedTourItemList(request);
            model.addAttribute("areaList", tourItemService.findAreaList());
            model.addAttribute("cat1List", tourItemService.findCat1List());
            model.addAttribute("cat2List", new ArrayList<Cat2>());
            model.addAttribute("cat3List", new ArrayList<Cat3>());
            model.addAttribute("sigunguList", tourItemService.findSigunguList(areaCode));
            model.addAttribute("contentTypeList", tourItemService.findContentTypeList());
            model.addAttribute("selectedArea", tourItemService.findArea(areaCode));
            model.addAttribute("pageResponse", pageResponse);
        }


        return "plan/plan";
    }



//    @ResponseBody
//    @RequestMapping(value = {"/show?areaCode={areaCode}&sigunguCode={sigunguCode}"}, method = RequestMethod.GET)
//    public String getMyPlan(@PathVariable(name = "areaCode") Integer areaCode, @PathVariable(name = "sigunguCode", required = false) Integer sigunguCode, Model model) {
//        TourItemSelectRequest request = TourItemSelectRequest.builder().areaCode(String.valueOf(areaCode)).sigunguCode(String.valueOf(sigunguCode)).build();
//        PageResponse pageResponse = tourItemService.findSelectedTourItemList(request);
//        model.addAttribute("areaList", tourItemService.findAreaList());
//        model.addAttribute("cat1List", tourItemService.findCat1List());
//        model.addAttribute("cat2List", new ArrayList<Cat2>());
//        model.addAttribute("cat3List", new ArrayList<Cat3>());
//        model.addAttribute("sigunguList", tourItemService.findSigunguList(areaCode));
//        model.addAttribute("contentTypeList", tourItemService.findContentTypeList());
//        model.addAttribute("selectedArea", tourItemService.findArea(areaCode));
//        model.addAttribute("pageResponse", pageResponse);
//
//
//        return "plan/plan";

//    public ResponseEntity<String> getSelectList(@RequestParam(name = "areaCode") Integer areaCode, @RequestParam(name = "sigunguCode", required = false) Integer sigunguCode){
//
//        return new ResponseEntity<>(areaCode + "/" + sigunguCode, HttpStatus.OK);
//    }

//    @GetMapping("/show?areaCode={areaCode}&sigunguCode={sigunguCode}")
//    public String getMyPlan(@PathVariable(name = "areaCode") Integer areaCode, @PathVariable(name = "sigunguCode", required = false) Integer sigunguCode, Model model) {
//        TourItemSelectRequest request = TourItemSelectRequest.builder().areaCode(String.valueOf(areaCode)).sigunguCode(String.valueOf(sigunguCode)).build();
//        PageResponse pageResponse = tourItemService.findSelectedTourItemList(request);
//        model.addAttribute("areaList", tourItemService.findAreaList());
//        model.addAttribute("cat1List", tourItemService.findCat1List());
//        model.addAttribute("cat2List", new ArrayList<Cat2>());
//        model.addAttribute("cat3List", new ArrayList<Cat3>());
//        model.addAttribute("sigunguList", tourItemService.findSigunguList(areaCode));
//        model.addAttribute("contentTypeList", tourItemService.findContentTypeList());
//        model.addAttribute("selectedArea", tourItemService.findArea(areaCode));
//        model.addAttribute("pageResponse", pageResponse);
//
//
//        return "plan/plan";
//    }

}


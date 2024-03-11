package com.io.threegonew.controller;

import com.io.threegonew.domain.Cat2;
import com.io.threegonew.domain.Cat3;
import com.io.threegonew.dto.PageResponse;
import com.io.threegonew.dto.TourItemSelectRequest;
import com.io.threegonew.service.PlannerService;
import com.io.threegonew.service.TourItemContentService;
import com.io.threegonew.service.TourItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("plan")
@RequiredArgsConstructor
public class PlanController {
    private final PlannerService plannerService;
    private final TourItemService tourItemService;
    private final TourItemContentService tourItemContentService;


//    @PostMapping("/")
//    public String addPlanner(AddPlannerRequest request) {
//        plannerService.save(request);
//        return "redirect:/pl"
//    }

    @GetMapping(value = {"/city/{areaCode}/{sigunguCode}","/city/{areaCode}"})
    public String getSelectList(@PathVariable(name = "areaCode") Integer areaCode,
                                @PathVariable(name = "sigunguCode", required = false) Integer sigunguCode,
                                Model model){
        TourItemSelectRequest request = buildTourItemSelectRequest(areaCode, sigunguCode);
        PageResponse pageResponse = tourItemService.findSelectedTourItemList(request);

        model.addAttribute("areaList", tourItemService.findAreaList());
        model.addAttribute("cat1List", tourItemService.findCat1List());
        model.addAttribute("cat2List", new ArrayList<Cat2>());
        model.addAttribute("cat3List", new ArrayList<Cat3>());
        model.addAttribute("sigunguList", tourItemService.findSigunguList(areaCode));
        model.addAttribute("contentTypeList", tourItemService.findContentTypeList());
        model.addAttribute("selectedArea", tourItemService.findArea(areaCode));
        model.addAttribute("pageResponse", pageResponse);

        return "plan/plan";
    }

    private TourItemSelectRequest buildTourItemSelectRequest(Integer areaCode, Integer sigunguCode) {
        String AreaCode = String.valueOf(areaCode);
        String SigunguCode = (sigunguCode == null) ? null : String.valueOf(sigunguCode);

        return TourItemSelectRequest.builder()
                .areaCode(AreaCode)
                .sigunguCode(SigunguCode)
                .build();
    }
}


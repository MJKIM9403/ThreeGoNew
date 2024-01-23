package com.io.threegonew.controller;

import com.io.threegonew.domain.Cat2;
import com.io.threegonew.domain.Cat3;
import com.io.threegonew.service.TourItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        model.addAttribute("areaList", tourItemService.findAreaList());
        model.addAttribute("cat1List", tourItemService.findCat1List());
        model.addAttribute("sigunguList", tourItemService.findSigunguList(areaCode));
        model.addAttribute("contentTypeList", tourItemService.findContentTypeList());
        model.addAttribute("selectedArea", tourItemService.findArea(areaCode));

        return "tourinfo/city";
    }

    @PostMapping("/api/cat/{cat1}")
    @ResponseBody
    public ResponseEntity<List<Cat2>> getCat2List(@PathVariable(name = "cat1") String cat1){
        List<Cat2> cat2List = tourItemService.findCat2List(cat1);
        return ResponseEntity.ok().body(cat2List);
    }

    @PostMapping("/api/cat/{cat1}/{cat2}")
    @ResponseBody
    public ResponseEntity<List<Cat3>> getCat3List(@PathVariable(name = "cat1") String cat1, @PathVariable(name = "cat2") String cat2){
        List<Cat3> cat3List = tourItemService.findCat3List(cat2);
        return ResponseEntity.ok().body(cat3List);
    }


}

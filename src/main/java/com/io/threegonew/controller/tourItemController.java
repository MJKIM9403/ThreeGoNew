package com.io.threegonew.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/info")
public class tourItemController {
    @GetMapping("/area")
    public String getArea(){
        return "tourinfo/area";
    }
}

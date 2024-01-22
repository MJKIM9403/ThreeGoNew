package com.io.threegonew.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class test {
    @GetMapping("/index")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/join")
    public String getJoin() {
        return "join";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/mypage")
    public String getMypage() {
        return "mypage";
    }

    @GetMapping("/area")
    public String getArea() {
        return "tourinfo/area";
    }

//    @GetMapping("/bbs")
//    public String getBoard() {
//        return "board/bbs";
//    }
}

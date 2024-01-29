package com.io.threegonew.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

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

        @GetMapping("/calendar")
        public String getMyCalendar() {
            return "plan/calendar";
        }

//        @GetMapping("/plan")
//        public String getMyPlan() {
//            return "plan/plan";
//        }

        @GetMapping("detail")
        public String getDetail() {
            return "plan/detail";
        }

        @GetMapping("mypage")
        public String getMyPage() {
            return "mypage/mypage";
        }

        @GetMapping("home")
        public String getHome() {
            return "index2";
        }

//    @GetMapping("/area")
//    public String getArea() {
//        return "tourinfo/area";
//    }

//    @GetMapping("/bbs")
//    public String getBoard() {
//        return "board/bbs";
//    }
    }


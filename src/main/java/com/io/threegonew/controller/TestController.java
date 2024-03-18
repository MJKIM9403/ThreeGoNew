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

        @GetMapping("detail")
        public String getDetail() {
            return "plan/detail";
        }

    }


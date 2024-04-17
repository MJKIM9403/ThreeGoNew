package com.io.threegonew.controller;

import com.io.threegonew.data.InsertData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
}


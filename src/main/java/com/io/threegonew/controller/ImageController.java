package com.io.threegonew.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    @GetMapping("/review/{path}/{imagename}")
    public Resource showReviewImage(@PathVariable String path, @PathVariable String imagename) throws MalformedURLException {
        String absolutePath = "C://threeGo/images/";
        String fullPath = absolutePath + path + "/" + imagename;
        return new UrlResource("file:" + fullPath);
    }

    @GetMapping("/book/{imagename}")
    public Resource showBookImage(@PathVariable String imagename) throws MalformedURLException {
        String absolutePath = "C://threeGo/bookcover/";
        String fullPath = absolutePath + "/" + imagename;
        return new UrlResource("file:" + fullPath);
    }

    @GetMapping("/profile/{imagename}")
    public Resource showProfileImage(@PathVariable String imagename) throws MalformedURLException {
        String absolutePath = "C://threeGo/profile/";
        String fullPath = absolutePath + "/" + imagename;
        return new UrlResource("file:" + fullPath);
    }
}

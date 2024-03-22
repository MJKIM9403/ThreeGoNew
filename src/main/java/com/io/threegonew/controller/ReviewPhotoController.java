package com.io.threegonew.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;

@Controller
@RequestMapping("api/review-photo")
public class ReviewPhotoController {

    @GetMapping("/{path}/{imagename}")
    @ResponseBody
    public Resource showImage(@PathVariable String path, @PathVariable String imagename) throws MalformedURLException {
        String absolutePath = "C://threeGo/images/";
        String fullPath = absolutePath + path + "/" + imagename;
        return new UrlResource("file:" + fullPath);
    }
}

package com.io.threegonew.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    @GetMapping("/review/{path}/{imagename}")
    public ResponseEntity<Resource> showReviewImage(@PathVariable String path, @PathVariable String imagename) throws IOException {
        String absolutePath = "C://threeGo/images/";
        String fullPath = absolutePath + path + "/" + imagename;
        MediaType mediaType = MediaType.parseMediaType(Files.probeContentType(Paths.get(fullPath)));
        UrlResource resource = new UrlResource("file:" + fullPath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
                .body(resource);
    }

    @GetMapping("/book/{imagename}")
    public ResponseEntity<Resource> showBookImage(@PathVariable String imagename) throws IOException {
        String absolutePath = "C://threeGo/bookcover/";
        String fullPath = absolutePath + "/" + imagename;
        MediaType mediaType = MediaType.parseMediaType(Files.probeContentType(Paths.get(fullPath)));
        UrlResource resource = new UrlResource("file:" + fullPath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
                .body(resource);
    }

    @GetMapping("/profile/{imagename}")
    public ResponseEntity<Resource> showProfileImage(@PathVariable String imagename) throws IOException {
        String absolutePath = "C://threeGo/profile/";
        String fullPath = absolutePath + "/" + imagename;
        MediaType mediaType = MediaType.parseMediaType(Files.probeContentType(Paths.get(fullPath)));
        UrlResource resource = new UrlResource("file:" + fullPath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
                .body(resource);
    }
}

package com.io.threegonew.controller;

import com.io.threegonew.dto.ErrorResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> showReviewImage(@PathVariable String path, @PathVariable String imagename) {
        try{
            String absolutePath = "C://threeGo/images/";
            String fullPath = absolutePath + path + "/" + imagename;
            MediaType mediaType = MediaType.parseMediaType(Files.probeContentType(Paths.get(fullPath)));
            UrlResource resource = new UrlResource("file:" + fullPath);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
                    .body(resource);
        }catch (IOException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400","리뷰 이미지를 조회할 수 없습니다.");
        }
    }

    @GetMapping("/book/{imagename}")
    public ResponseEntity<?> showBookImage(@PathVariable String imagename){
        try{
            String absolutePath = "C://threeGo/bookcover/";
            String fullPath = absolutePath + "/" + imagename;
            MediaType mediaType = MediaType.parseMediaType(Files.probeContentType(Paths.get(fullPath)));
            UrlResource resource = new UrlResource("file:" + fullPath);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
                    .body(resource);
        }catch (IOException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400","리뷰북 이미지를 조회할 수 없습니다.");
        }

    }

    @GetMapping("/profile/{imagename}")
    public ResponseEntity<?> showProfileImage(@PathVariable String imagename) {
        try{
            String absolutePath = "C://threeGo/profile/";
            String fullPath = absolutePath + "/" + imagename;
            MediaType mediaType = MediaType.parseMediaType(Files.probeContentType(Paths.get(fullPath)));
            UrlResource resource = new UrlResource("file:" + fullPath);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
                    .body(resource);
        }catch (IOException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400","프로필 이미지를 조회할 수 없습니다.");
        }
    }
}

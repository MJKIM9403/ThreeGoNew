package com.io.threegonew.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.io.threegonew.constant.FileType;
import com.io.threegonew.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {
    private final AmazonS3Client amazonS3Client;
    private final RestTemplate restTemplate;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @GetMapping("/review/{path}/{imagename}")
    public ResponseEntity<?> showReviewImage(@PathVariable String path, @PathVariable String imagename) {
        try{
            String imagePath = FileType.REVIEW.getPath() + "/" + path + "/" + imagename;
            return getImageResponse(imagePath);
        }catch (Exception e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400","리뷰 이미지를 조회할 수 없습니다.");
        }
    }

    @GetMapping("/book/{imagename}")
    public ResponseEntity<?> showBookImage(@PathVariable String imagename){
        try{
            String imagePath = FileType.REVIEW_BOOK.getPath() + "/" + imagename;
            return getImageResponse(imagePath);
        }catch (Exception e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400","리뷰북 이미지를 조회할 수 없습니다.");
        }

    }

    @GetMapping("/profile/{imagename}")
    public ResponseEntity<?> showProfileImage(@PathVariable String imagename) {
        try{
            String imagePath = FileType.USER.getPath() + "/" + imagename;
            return getImageResponse(imagePath);
        }catch (Exception e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400","프로필 이미지를 조회할 수 없습니다.");
        }
    }

    private ResponseEntity<UrlResource> getImageResponse(String imagePath){
        URL imageUrl = amazonS3Client.getUrl(bucket,imagePath);

        HttpHeaders headers = restTemplate.headForHeaders(imageUrl.toString());
        String contentType = headers.getContentType() != null ?
                headers.getContentType().toString() : "application/octet-stream";

        UrlResource resource = new UrlResource(imageUrl);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }
}

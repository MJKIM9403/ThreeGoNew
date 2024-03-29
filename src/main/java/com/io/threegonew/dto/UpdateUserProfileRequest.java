package com.io.threegonew.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserProfileRequest {
    private String userId;
    private String name;
    private String about;
    private MultipartFile newProfileImg;
}

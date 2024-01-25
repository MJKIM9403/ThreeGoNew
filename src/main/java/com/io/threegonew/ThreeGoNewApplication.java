package com.io.threegonew;

import jakarta.annotation.PostConstruct;
import lombok.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.File;

@EnableJpaAuditing
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ThreeGoNewApplication {

    // 파일을 저장할 디렉터리
    private final String fileDir = System.getProperty("user.dir") // 현재 디렉토리 경로
            + "/src/main/resources/static/files/"; // 파일이 저장될 폴더의 경로

    public static void main(String[] args) {
        SpringApplication.run(ThreeGoNewApplication.class, args);
    }

    @PostConstruct
    public void init() {
        File directory = new File(fileDir);
        if(!directory.exists()) {
            directory.mkdirs(); // 디렉터리가 없다면 디렉터리 생성
        }
    }
}

package com.io.threegonew.util;

import com.io.threegonew.domain.Review;
import com.io.threegonew.domain.ReviewBook;
import com.io.threegonew.domain.ReviewPhoto;
import com.io.threegonew.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileHandler {

    private final String absolutePath = "C://threeGo/";
    private final String reviewPhotoPath = "images/";
    private final String userProfilePath = "profile/";
    private final String bookCoverPath = "bookcover/";

    public boolean deleteUserProfile(User user) {
        File deleteFile = new File(absolutePath + userProfilePath + user.getU_sfile());
        user.updateProfileImg(null, null);
        return deleteFile.delete();
    }

    public void updateUserProfile(User user, MultipartFile newProfileImg) throws Exception {
        if(user.getU_sfile() != null){
            deleteUserProfile(user);
        }
        if(!newProfileImg.isEmpty()){
            File file = new File(absolutePath + userProfilePath);

            if(!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                // 디렉터리 생성에 실패했을 경우
                if(!wasSuccessful){
                    throw new IOException("file: was not successful");
                }
            }

            // 파일의 확장자 추출
            String oFileExtension = "";
            String contentType = newProfileImg.getContentType();

            // 확장자명이 존재하지 않을 경우 처리 x
            if(ObjectUtils.isEmpty(contentType)) {
                return;
            }
            else {  // 확장자가 jpeg, png인 파일들만 받아서 처리
                if(contentType.contains("image/jpeg")) {
                    oFileExtension = ".jpg";
                }else if(contentType.contains("image/png")) {
                    oFileExtension = ".png";
                }else if(contentType.contains("image/gif")) {
                    oFileExtension = ".gif";
                }else {
                    return; // 다른 확장자일 경우 처리 x
                }
            }

            // 저장할 파일 이름 생성
            String oFileName = newProfileImg.getOriginalFilename();
            String sFileName = UUID.randomUUID() + oFileExtension;
            user.updateProfileImg(oFileName, sFileName);
            File saveFile = new File(absolutePath + userProfilePath + sFileName);
            newProfileImg.transferTo(saveFile);

            // 파일 권한 설정(쓰기, 읽기)
            saveFile.setWritable(true);
            saveFile.setReadable(true);
        }
    }

    public boolean deleteBookCover(ReviewBook reviewBook) {
        File deleteFile = new File(absolutePath + bookCoverPath + reviewBook.getCoverFilePath());
        reviewBook.updateCover(null, null);
        return deleteFile.delete();
    }

    public void updateBookCover(ReviewBook reviewBook, MultipartFile newBookCover) throws Exception {
        if(reviewBook.getCoverFilePath() != null){
            deleteBookCover(reviewBook);
        }
        if(!newBookCover.isEmpty()){
            File file = new File(absolutePath + bookCoverPath);

            if(!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                // 디렉터리 생성에 실패했을 경우
                if(!wasSuccessful){
                    throw new IOException("file: was not successful");
                }
            }

            // 파일의 확장자 추출
            String oFileExtension = "";
            String contentType = newBookCover.getContentType();

            // 확장자명이 존재하지 않을 경우 처리 x
            if(ObjectUtils.isEmpty(contentType)) {
                return;
            }
            else {  // 확장자가 jpeg, png인 파일들만 받아서 처리
                if(contentType.contains("image/jpeg")) {
                    oFileExtension = ".jpg";
                }else if(contentType.contains("image/png")) {
                    oFileExtension = ".png";
                }else if(contentType.contains("image/gif")) {
                    oFileExtension = ".gif";
                }else {
                    return; // 다른 확장자일 경우 처리 x
                }
            }

            // 저장할 파일 이름 생성
            String oFileName = newBookCover.getOriginalFilename();
            String sFileName = UUID.randomUUID() + oFileExtension;
            reviewBook.updateCover(oFileName, sFileName);
            File saveFile = new File(absolutePath + bookCoverPath + sFileName);
            newBookCover.transferTo(saveFile);

            // 파일 권한 설정(쓰기, 읽기)
            saveFile.setWritable(true);
            saveFile.setReadable(true);
        }
    }

    public boolean deleteReviewPhoto(ReviewPhoto deletePhoto){
        File deleteFile = new File(absolutePath + reviewPhotoPath + deletePhoto.getFilePath());
        return deleteFile.delete();
    }

    public List<ReviewPhoto> parseReviewPhoto(Review review, List<MultipartFile> multipartFiles) throws Exception {
        // 반환할 파일 리스트
        List<ReviewPhoto> fileList = new ArrayList<>();

        // 전달되어 온 파일이 존재할 경우
        if(!CollectionUtils.isEmpty(multipartFiles)) {
            // 파일명을 업로드 한 날짜로 변환하여 저장
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);

            // 파일을 저장할 세부 경로 지정
            String path = current_date;
            File file = new File(absolutePath + reviewPhotoPath + path);

            // 디렉터리가 존재하지 않을 경우
            if(!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                // 디렉터리 생성에 실패했을 경우
                if(!wasSuccessful){
                    throw new IOException("file: was not successful");
                }
            }

            // 다중 파일 처리
            for(MultipartFile multipartFile : multipartFiles) {

                // 파일의 확장자 추출
                String oFileExtension = "";
                String contentType = multipartFile.getContentType();

                // 확장자명이 존재하지 않을 경우 처리 x
                if(ObjectUtils.isEmpty(contentType)) {
                    break;
                }
                else {  // 확장자가 jpeg, png인 파일들만 받아서 처리
                    if(contentType.contains("image/jpeg")) {
                        oFileExtension = ".jpg";
                    }else if(contentType.contains("image/png")) {
                        oFileExtension = ".png";
                    }else if(contentType.contains("image/gif")) {
                        oFileExtension = ".gif";
                    }else {
                        break; // 다른 확장자일 경우 처리 x
                    }
                }

                // 저장할 파일 이름 생성
                String sFileName = UUID.randomUUID() + oFileExtension;

                ReviewPhoto photo = ReviewPhoto.builder()
                                .review(review)
                                .ofile(multipartFile.getOriginalFilename())
                                .filePath(path + "/" + sFileName)
                                .fileSize(multipartFile.getSize())
                                .build();

                // 생성 후 리스트에 추가
                fileList.add(photo);

                // 업로드 한 파일 데이터를 지정한 파일에 저장
                File saveFile = new File(absolutePath + reviewPhotoPath + path + "/" + sFileName);
                multipartFile.transferTo(saveFile);

                // 파일 권한 설정(쓰기, 읽기)
                saveFile.setWritable(true);
                saveFile.setReadable(true);
            }
        }

        return fileList;
    }
}

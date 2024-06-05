package com.io.threegonew.commons;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.io.threegonew.constant.FileType;
import com.io.threegonew.domain.Review;
import com.io.threegonew.domain.ReviewBook;
import com.io.threegonew.domain.ReviewPhoto;
import com.io.threegonew.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3FileUploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 파일 확장자 검사
    private String validateExtension(MultipartFile multipartFile) throws IOException {
        // 파일의 확장자 추출
        String extension = "";
        String contentType = multipartFile.getContentType();

        // 확장자명이 존재하지 않을 경우 처리 x
        if(ObjectUtils.isEmpty(contentType)) {
            throw new IOException("저장할 수 없는 확장자 입니다.");
        }
        else {  // 확장자가 jpeg, png인 파일들만 받아서 처리
            if(contentType.contains("image/jpeg")) {
                extension = ".jpg";
            }else if(contentType.contains("image/png")) {
                extension = ".png";
            }else if(contentType.contains("image/gif")) {
                extension = ".gif";
            }else {
                throw new IOException("저장할 수 없는 확장자 입니다."); // 다른 확장자일 경우 처리 x
            }
        }

        return extension;
    }

    private void uploadToS3(MultipartFile multipartFile, String uploadFilePath) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try(InputStream input = multipartFile.getInputStream()){
            amazonS3Client.putObject(new PutObjectRequest(bucket, uploadFilePath, input, objectMetadata));
        }catch (IOException e){
            throw new IOException("파일 업로드에 실패하였습니다.");
        }
    }

    public void updateUserProfile(User user, MultipartFile newProfileImg) throws IOException {
        // 새로 업로드 하는 이미지가 존재하는 경우
        if(!newProfileImg.isEmpty()){
            // 기존 설정된 프로필 이미지의 경로
            String prevProfilePath = user.getU_sfile();

            String originFileName = newProfileImg.getOriginalFilename();
            String extension = validateExtension(newProfileImg);
            String uploadFileName = UUID.randomUUID() + extension;
            String uploadFilePath = FileType.USER.getPath() + "/" + uploadFileName;

            // S3에 이미지 업로드 후 유저 프로필 이미지 정보 업데이트
            uploadToS3(newProfileImg, uploadFilePath);
            user.updateProfileImg(originFileName, uploadFileName);

            // 기존 저장된 프로필 이미지를 S3에서 삭제
            deleteImageFromS3(FileType.USER, prevProfilePath);
        }
    }

    public void resetUserProfile(User user) throws IOException {
        if(user.getU_sfile() != null && !user.getU_sfile().isEmpty()){
            String prevProfilePath = user.getU_sfile();
            // 기존 저장된 프로필 이미지를 S3에서 삭제 한 후 프로필 이미지 정보를 모두 null로 업데이트
            deleteImageFromS3(FileType.USER, prevProfilePath);
            user.updateProfileImg(null, null);
        }
    }

    public void updateBookCover(ReviewBook reviewBook, MultipartFile newBookCover) throws IOException {
        // 새로 업로드 하는 이미지가 존재하는 경우
        if(!newBookCover.isEmpty()){
            // 기존 설정된 북커버 이미지의 경로
            String prevBookCoverPath = reviewBook.getCoverFilePath();

            String originFileName = newBookCover.getOriginalFilename();
            String extension = validateExtension(newBookCover);
            String uploadFileName = UUID.randomUUID() + extension;
            String uploadFilePath = FileType.REVIEW_BOOK.getPath() + "/" + uploadFileName;

            // S3에 이미지 업로드 후 북커버 이미지 정보 업데이트
            uploadToS3(newBookCover, uploadFilePath);
            reviewBook.updateCover(originFileName, uploadFileName);

            // 기존 저장된 북커버 이미지를 S3에서 삭제
            deleteImageFromS3(FileType.REVIEW_BOOK, prevBookCoverPath);
        }
    }

    public void resetBookCover(ReviewBook reviewBook) throws IOException {
        if(reviewBook.getCoverFilePath() != null && !reviewBook.getCoverFilePath().isEmpty()){
            String prevBookCoverPath = reviewBook.getCoverFilePath();
            // 기존 저장된 북커버 이미지를 S3에서 삭제 한 후 북커버 이미지 정보를 모두 null로 업데이트
            deleteImageFromS3(FileType.REVIEW_BOOK, prevBookCoverPath);
            reviewBook.updateCover(null, null);
        }
    }

    public List<ReviewPhoto> parseReviewPhoto(Review review, List<MultipartFile> multipartFiles) throws IOException {
        // 반환할 파일 리스트
        List<ReviewPhoto> fileList = new ArrayList<>();

        // 전달되어 온 파일이 존재할 경우
        if(!CollectionUtils.isEmpty(multipartFiles)) {
            // 세부 폴더명을 업로드 한 날짜로 변환하여 저장
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String detailPath = now.format(dateTimeFormatter);

            String folderPath = FileType.REVIEW.getPath() + "/" + detailPath;

            for (MultipartFile multipartFile : multipartFiles) {
                String extension = validateExtension(multipartFile);
                String uploadFileName = UUID.randomUUID() + extension;
                String uploadFilePath = folderPath + "/" + uploadFileName;

                // S3에 이미지 업로드
                uploadToS3(multipartFile, uploadFilePath);

                ReviewPhoto photo = ReviewPhoto.builder()
                        .review(review)
                        .ofile(multipartFile.getOriginalFilename())
                        .filePath(detailPath + "/" + uploadFileName)
                        .fileSize(multipartFile.getSize())
                        .build();

                // ReviewPhoto 생성 후 리스트에 추가
                fileList.add(photo);
            }
        }

        return fileList;
    }

    public void deleteImageFromS3(FileType fileType, String filePath) throws IOException {
        try{
            String uploadedFilePath = fileType.getPath() + "/" + filePath;
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, uploadedFilePath));
        }catch (Exception e){
            throw new IOException("파일 삭제에 실패했습니다.");
        }
    }

}

package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "review_photo")
public class ReviewPhoto{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @Column(name = "o_file", nullable = false)
    private String ofile;  // 파일 원본명

    @Column(name = "file_path", nullable = false)
    private String filePath;  // 파일 저장 경로

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @CreatedDate
    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;
    @Builder
    public ReviewPhoto(String ofile, String filePath, Long fileSize){
        this.ofile = ofile;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    // Review 정보 저장
    public void setReview(Review review){
        this.review = review;

        // 게시글에 현재 파일이 존재하지 않는다면
        if(!review.getReviewPhotoList().contains(this))
            // 파일 추가
            review.getReviewPhotoList().add(this);
    }
}

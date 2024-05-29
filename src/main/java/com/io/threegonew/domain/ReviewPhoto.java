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
@NoArgsConstructor
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
    public ReviewPhoto(Review review, String ofile, String filePath, Long fileSize){
        this.review = review;
        this.ofile = ofile;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
}

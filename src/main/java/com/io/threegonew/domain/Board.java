package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "b_id", updatable = false)
    private Integer bId;

    @Column(name = "u_id", nullable = false)
    private String userId;

    @Column(name = "u_name", nullable = false)
    private String userName;

    @Column(name = "b_title", nullable = false)
    private String bTitle;

    @Column(name = "b_content", nullable = false)
    private String bContent;

    @Column(name = "b_postdate")
    @CreatedDate
    private LocalDate bPostdate;

    @Column(name = "b_ofile")
    private String originalFile;

    @Column(name = "b_sfile")
    private String savedFile;

    @Column(name = "b_visitcount")
    private double visitCount;

    @Builder
    public Board(String userId, String userName, String bTitle, String bContent) {
        this.userId = userId;
        this.userName = userName;
        this.bTitle = bTitle;
        this.bContent = bContent;
    }

    public void update(String bTitle, String bContent) {
        this.bTitle = bTitle;
        this.bContent = bContent;
    }
}

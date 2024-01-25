package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "b_id", updatable = false)
    private Integer bid;

    @Column(name = "u_id", nullable = true)
    private String userid;

    @Column(name = "u_name", nullable = false)
    private String username;

    @Column(name = "b_title", nullable = false)
    private String btitle;

    @Column(name = "b_content", nullable = false)
    private String bcontent;

    @Column(name = "b_postdate")
    @CreatedDate
    private LocalDateTime bpostdate;

//    @Column(name = "b_ofile")
//    private String bofile;
//
//    @Column(name = "b_sfile")
//    private String bsfile;

    @Column(name = "b_visitcount",nullable = false)
    private Integer bvisitcount = 0;


    @Builder
    public Board(String userid, String username, String btitle, String bcontent) {
        this.userid = userid;
        this.username = username;
        this.btitle = btitle;
        this.bcontent = bcontent;
        this.bpostdate = LocalDateTime.now();
    }

    public void update(String btitle, String bcontent) {
        this.btitle = btitle;
        this.bcontent = bcontent;
    }

    // 조회수 증가
    public void updateVisitCount(Integer bvisitcount) {
        this.bvisitcount = bvisitcount;
    }

    // 파일 첨부 관련
//    public void addFile(BoardFile boardFile) {
//        this.boardFile.a
//    }
}

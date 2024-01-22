package com.io.threegonew.dto;

import com.io.threegonew.domain.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class BoardViewResponse {
    private String userId;
    private String userName;
    private Integer bId;
    private String bTitle;
    private String bContent;
    private LocalDate bPostdate;
    private String originalFile;
    private String savedFile;
    private double visitCount;

    public BoardViewResponse(Board board) {
        this.userId = board.getUserId();
        this.userName = board.getUserName();
        this.bId = board.getBId();
        this.bTitle = board.getBTitle();
        this.bContent = board.getBContent();
        this.bPostdate = board.getBPostdate();
        this.originalFile = board.getOriginalFile();
        this.savedFile = board.getSavedFile();
        this.visitCount = board.getVisitCount();

    }
}

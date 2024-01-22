package com.io.threegonew.dto;

import com.io.threegonew.domain.Board;
import lombok.Getter;

@Getter
public class BoardResponse {
    private String userId;
    private String userName;
    private String bTitle;
    private String bContent;

    public BoardResponse(Board board) {
        this.userId = board.getUserId();
        this.userName = board.getUserName();
        this.bTitle = board.getBTitle();
        this.bContent = board.getBContent();
    }
}

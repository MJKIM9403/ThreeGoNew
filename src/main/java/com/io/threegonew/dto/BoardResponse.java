package com.io.threegonew.dto;

import com.io.threegonew.domain.Board;
import lombok.Getter;

@Getter
public class BoardResponse {
    private String userid;
    private String username;
    private String btitle;
    private String bcontent;

    public BoardResponse(Board board) {
        this.userid = board.getUserid();
        this.username = board.getUsername();
        this.btitle = board.getBtitle();
        this.bcontent = board.getBcontent();
    }
}

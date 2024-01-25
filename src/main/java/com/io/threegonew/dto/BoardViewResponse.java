package com.io.threegonew.dto;

import com.io.threegonew.domain.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardViewResponse {
    private String userid;
    private String username;
    private Integer bid;
    private String btitle;
    private String bcontent;
    private LocalDateTime bpostdate;
    private Integer bvisitcount;

    public BoardViewResponse(Board board) {
        this.userid = board.getUserid();
        this.username = board.getUsername();
        this.bid = board.getBid();
        this.btitle = board.getBtitle();
        this.bcontent = board.getBcontent();
        this.bpostdate = board.getBpostdate();
        this.bvisitcount = board.getBvisitcount();
    }
}

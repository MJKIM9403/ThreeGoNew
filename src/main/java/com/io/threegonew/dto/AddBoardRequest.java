package com.io.threegonew.dto;

import com.io.threegonew.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class AddBoardRequest {
    private String userid;
    private String username;
    private String btitle;
    private String bcontent;

    public Board toEntity() {
        return Board.builder()
                .userid(userid)
                .username(username)
                .btitle(btitle)
                .bcontent(bcontent)
                .build();
    }
}

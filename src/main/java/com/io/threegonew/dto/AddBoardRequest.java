package com.io.threegonew.dto;

import com.io.threegonew.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class AddBoardRequest {
//    private String userId;
    private String userName;
    private String bTitle;
    private String bContent;

    public Board toEntity() {
        return Board.builder()
//                .userId(userId)
                .userName(userName)
                .bTitle(bTitle)
                .bContent(bContent)
                .build();
    }
}

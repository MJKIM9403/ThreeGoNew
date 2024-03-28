package com.io.threegonew.dto;

import com.io.threegonew.domain.User;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowDTO {
    private User toUser;
    private User fromUser;
    private int followState;
}

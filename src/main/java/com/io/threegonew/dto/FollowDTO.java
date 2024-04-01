package com.io.threegonew.dto;

import com.io.threegonew.domain.User;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowDTO {
    private Long id;
    private UserInfoResponse toUser;
    private UserInfoResponse fromUser;
    private int followState;
}

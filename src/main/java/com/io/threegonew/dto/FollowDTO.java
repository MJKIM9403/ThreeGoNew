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
    private int followState; // 내가 팔로우 중인지
    private int youFollowMeState; // 나를 팔로우 중인지
    private int followCount; // 팔로우 중인 숫자
}

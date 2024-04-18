package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserWithFollowStateResponse {
    private String id;
    private String name;
    private String profileImg;
    private String about;
    private Integer followingState; // 이 사람을 팔로우 하는지
    private Integer followedState; // 이 사람이 팔로우 하는지
}

package com.io.threegonew.dto;

import com.io.threegonew.domain.User;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowDTO {
    private Long id;
    private String toUser;
    private String fromUser;
    private String listName;
    private String listSfile;
    private int followingState; // 내가 팔로우 중인지
    private int sameUserState; // 같은 유저인지
    private int followedState; // 그 유저가 나를 팔로우 중인지

}

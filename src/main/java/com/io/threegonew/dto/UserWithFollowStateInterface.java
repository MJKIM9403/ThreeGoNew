package com.io.threegonew.dto;

public interface UserWithFollowStateInterface {
    String getId();
    String getName();
    String getProfileImg();
    String getAbout();
    Integer getFollowingState(); // 이 사람을 팔로우 하는지
    Integer getFollowedState();
}

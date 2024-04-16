package com.io.threegonew.dto;

import com.io.threegonew.domain.User;

public interface FollowProjection {
    Long getId();
    String getToUser();
    String getFromUser();
    Integer getFollowState();
    Integer getSameUserState();
}

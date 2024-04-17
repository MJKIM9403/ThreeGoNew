package com.io.threegonew.dto;

import com.io.threegonew.domain.User;

public interface FollowProjection {
    Long getId();
    String getToUser();
    String getFromUser();
    String getListName();
    String getListSfile();
    Integer getFollowingState();
    Integer getSameUserState();
    Integer getFollowedState();

}

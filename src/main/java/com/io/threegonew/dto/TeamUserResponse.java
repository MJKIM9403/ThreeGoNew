package com.io.threegonew.dto;

import com.sun.jna.platform.win32.Netapi32Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamUserResponse {
    private Long teamId;
    private Long plannerId;
    private UserInfoResponse user;
    private Integer teamLevel;
}

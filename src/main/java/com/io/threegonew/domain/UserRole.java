package com.io.threegonew.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

//    UserRole(String value){
//        this.value = value ;
//    }

    private String value;

}

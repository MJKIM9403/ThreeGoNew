package com.io.threegonew.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    private String id;
    private String u_pw;
    private String u_name;
    private String u_email;
    private String u_ofile;
    private String u_sfile;
    private String u_about;

}

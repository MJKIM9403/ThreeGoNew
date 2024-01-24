package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateBoardRequest {
    private String btitle;
    private String bcontent;
    private String bofile;
    private String bsfile;
}

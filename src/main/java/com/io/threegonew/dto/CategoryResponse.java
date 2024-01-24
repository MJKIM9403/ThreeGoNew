package com.io.threegonew.dto;

import com.io.threegonew.domain.Cat2;
import com.io.threegonew.domain.Cat3;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryResponse {
    private List<Cat2> cat2List;
    private List<Cat3> cat3List;

    @Builder
    public CategoryResponse(List<Cat2> cat2List, List<Cat3> cat3List) {
        this.cat2List = cat2List;
        this.cat3List = cat3List;
    }
}

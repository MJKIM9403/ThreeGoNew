package com.io.threegonew.controller;

import com.io.threegonew.data.InsertData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class insertDataController {
    private final InsertData insertData;

    @GetMapping("/insertData")
    public void insert(){
        insertData.insertMainTable("Area");
        insertData.insertMainTable("Category");
        insertData.insertMainTable("TourItem");
    }
}

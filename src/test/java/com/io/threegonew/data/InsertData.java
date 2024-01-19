package com.io.threegonew.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.threegonew.ApiKey;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.repository.TourItemRepository;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@SpringBootTest
public class InsertData {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TourItemRepository tourItemRepository;

    //TOURITEM 데이터 추가
    @Test
    public void insertTourItem(){

        String searchType = "areaBasedList1";

        int totalData = 52346;

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl("https://apis.data.go.kr/B551011/KorService1/")
                .path(searchType)
                .queryParam("numOfRows",totalData)
                .queryParam("pageNo", 1)
                .queryParam("MobileOS","ETC")
                .queryParam("MobileApp","THREEGO")
                .queryParam("serviceKey",ApiKey.TOURAPI_KEY_1)
                .queryParam("_type","json")
                .build();

        System.out.println(uri);

        String result = "";

        try {
            URL url = new URL(uri.toUriString());
            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");

            for(Object itemObj : item){
                String itemStr = itemObj.toString();
                TourItem tourItem = objectMapper.readValue(itemStr, TourItem.class);
                tourItemRepository.save(tourItem);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}

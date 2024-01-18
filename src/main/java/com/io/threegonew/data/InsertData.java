package com.io.threegonew.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.io.threegonew.ApiKey;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertData {

    //TOURITEM 데이터 추가
    public void insertTourItem() throws JsonProcessingException {

        String searchType = "areaBasedList1";
        //Map<String, Object> result = new HashMap<>();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);

        int totalData = 52302;

        UriComponents url = UriComponentsBuilder.newInstance()
                .path("https://apis.data.go.kr/B551011/KorService1/")
                .path(searchType)
                .queryParam("numOfRows",totalData)
                .queryParam("pageNo", 1)
                .queryParam("MobileOS","ETC")
                .queryParam("MobileApp","THREEGO")
                .queryParam("serviceKey",ApiKey.TOURAPI_KEY_1)
                .queryParam("_type","json")
                .build();

        System.out.println(url);

        ResponseEntity<Object> resultMap = restTemplate.exchange(url.toUriString(), HttpMethod.GET, entity, Object.class);

//        ObjectMapper mapper = new ObjectMapper();
//        String result = mapper.writeValueAsString(resultMap.getBody());
//
//        System.out.println(result);

    }
}

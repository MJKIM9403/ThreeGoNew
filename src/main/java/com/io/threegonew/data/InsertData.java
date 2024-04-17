package com.io.threegonew.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.threegonew.Key;
import com.io.threegonew.domain.*;
import com.io.threegonew.domain.pk.SigunguPk;
import com.io.threegonew.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class InsertData {

    ObjectMapper objectMapper;
    TourItemRepository tourItemRepository;
    AreaRepository areaRepository;
    Cat1Repository cat1Repository;
    Cat2Repository cat2Repository;
    Cat3Repository cat3Repository;
    SigunguRepository sigunguRepository;


    private String makeURI(String table, Map<String, Object> params){
        String searchType = "";
        if(table.equals("TourItem")){
            searchType = "areaBasedList1";
        }else if(table.equals("Area")){
            searchType = "areaCode1";
        }else if(table.equals("Category")){
            searchType = "categoryCode1";
        }

        int totalData = 100000;

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl("https://apis.data.go.kr/B551011/KorService1/")
                .path(searchType)
                .queryParam("numOfRows",totalData)
                .queryParam("pageNo", 1)
                .queryParam("MobileOS","ETC")
                .queryParam("MobileApp","THREEGO");

        if(params.containsKey("Cat1")){
            uriBuilder.queryParam("cat1", ((Cat1) params.get("Cat1")).getCat1());
        }
        if(params.containsKey("Cat2")
                && ((((Cat2)params.get("Cat2")).getCat2()).startsWith(((Cat1) params.get("Cat1")).getCat1()))){
            uriBuilder.queryParam("cat2",((Cat2)params.get("Cat2")).getCat2());
        }
        if(params.containsKey("Area")){
            uriBuilder.queryParam("areaCode",params.get("Area"));
        }

        uriBuilder.queryParam("serviceKey", Key.TOURAPI_KEY_1)
                .queryParam("_type","json")
                .build();

        UriComponents uri = uriBuilder.build();

        return uri.toUriString();
    }

    //TOURITEM 데이터 추가
    @Transactional
    public void insertMainTable(String table){
        String searchType = "";
        Map<String, Object> params = new HashMap<>();

        if(table.equals("TourItem")){
            searchType = "areaBasedList1";
        }else if(table.equals("Area")){
            searchType = "areaCode1";
        }else if(table.equals("Category")){
            searchType = "categoryCode1";
        }

        int totalData = 100000;


        String result = "";

        try {
            URL url = new URL(makeURI(table,params));
            System.out.println(url);
            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");

            for(Object itemObj : item){
                JSONObject itemJson = (JSONObject) itemObj;
                String itemStr = itemObj.toString();
                if(table.equals("TourItem")){
                    TourItem tourItem = objectMapper.readValue(itemStr, TourItem.class);
                    if(tourItemRepository.existsById(tourItem.getContentid())){
                        TourItem existingTourItem = tourItemRepository.findById(tourItem.getContentid()).get();
                        existingTourItem.updateEntityFromApiResponse(tourItem);
                    }else {
                        tourItemRepository.save(tourItem);
                    }
                }else if(table.equals("Area")){
                    Area area = Area.builder()
                            .areaCode(Integer.parseInt(itemJson.get("code").toString()))
                            .areaName(itemJson.get("name").toString())
                            .build();
                    if(areaRepository.existsById(area.getAreaCode())){
                        Area existingArea = areaRepository.findById(area.getAreaCode()).get();
                        existingArea.updateEntityFromApiResponse(area);
                    }else {
                        areaRepository.save(area);
                    }
                    params.put("Area", area.getAreaCode());
                    insertSigungu(makeURI(table,params), params);
                }else if(table.equals("Category")){
                    Cat1 cat1 = Cat1.builder()
                                    .cat1(itemJson.get("code").toString())
                                    .cat1Name(itemJson.get("name").toString())
                                    .build();
                    if(cat1Repository.existsById(cat1.getCat1())){
                        Cat1 existingCat1 = cat1Repository.findById(cat1.getCat1()).get();
                        existingCat1.updateEntityFromApiResponse(cat1);
                    }else {
                        cat1Repository.save(cat1);
                    }
                    params.put("Cat1",cat1);
                    insertCat2(makeURI(table,params), params);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void insertSigungu(String url, Map<String,Object> params){
        String result = "";
        try {
            URL newUrl = new URL(url);
            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(newUrl.openStream(), StandardCharsets.UTF_8));

            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");

            for(Object itemObj : item){
                JSONObject itemJson = (JSONObject) itemObj;
                Sigungu sigungu = Sigungu.builder()
                                .sigunguCode(Integer.parseInt(itemJson.get("code").toString()))
                                .areaCode(Integer.parseInt(params.get("Area").toString()))
                                .sigunguName(itemJson.get("name").toString())
                                .build();
                SigunguPk pk = SigunguPk.builder()
                                .sigunguCode(sigungu.getSigunguCode())
                                .areaCode(sigungu.getAreaCode())
                                .build();

                if(sigunguRepository.existsById(pk)){
                    Sigungu existingSigungu = sigunguRepository.findById(pk).get();
                    existingSigungu.updateEntityFromApiResponse(sigungu);
                }else {
                    sigunguRepository.save(sigungu);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void insertCat2(String url, Map<String, Object> params){
        System.out.println(url);
        String result = "";
        try {
            URL newUrl = new URL(url);
            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(newUrl.openStream(), StandardCharsets.UTF_8));

            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");

            for(Object itemObj : item){
                JSONObject itemJson = (JSONObject) itemObj;
                Cat2 cat2 = Cat2.builder()
                                .cat2(itemJson.get("code").toString())
                                .cat2Name(itemJson.get("name").toString())
                                .cat1((Cat1) params.get("Cat1"))
                                .build();
                if(cat2Repository.existsById(cat2.getCat2())){
                    Cat2 existingCat2 = cat2Repository.findById(cat2.getCat2()).get();
                    existingCat2.updateEntityFromApiResponse(cat2);
                }else {
                    cat2Repository.save(cat2);
                }
                params.put("Cat2",cat2);
                insertCat3(makeURI("Category",params), params);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void insertCat3(String url, Map<String, Object> params){
        String result = "";
        System.out.println(url);
        try {
            URL newUrl = new URL(url);
            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(newUrl.openStream(), StandardCharsets.UTF_8));

            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");

            for(Object itemObj : item){
                JSONObject itemJson = (JSONObject) itemObj;
                Cat3 cat3 = Cat3.builder()
                        .cat3(itemJson.get("code").toString())
                        .cat3Name(itemJson.get("name").toString())
                        .cat1((Cat1) params.get("Cat1"))
                        .cat2((Cat2) params.get("Cat2"))
                        .build();
                if(cat3Repository.existsById(cat3.getCat3())){
                    Cat3 existingCat3 = cat3Repository.findById(cat3.getCat3()).get();
                    existingCat3.updateEntityFromApiResponse(cat3);
                }else {
                    cat3Repository.save(cat3);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


}

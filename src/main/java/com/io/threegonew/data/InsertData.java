package com.io.threegonew.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.threegonew.ApiKey;
import com.io.threegonew.domain.*;
import com.io.threegonew.repository.*;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class InsertData {

    ObjectMapper objectMapper;
    TourItemRepository tourItemRepository;
    AreaRepository areaRepository;
    Cat1Repository cat1Repository;
    Cat2Repository cat2Repository;
    Cat3Repository cat3Repository;
    SigunguRepository sigunguRepository;


    private String makeUri(String table, Map<String, Object> params){
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
            uriBuilder.queryParam("cat1", params.get("Cat1"));
        }
        if(params.containsKey("Cat2")){
            uriBuilder.queryParam("cat2",params.get("Cat2"));
        }
        if(params.containsKey("Area")){
            uriBuilder.queryParam("areaCode",params.get("Area"));
        }

        uriBuilder.queryParam("serviceKey",ApiKey.TOURAPI_KEY_1)
                .queryParam("_type","json")
                .build();

        UriComponents uri = uriBuilder.build();

        return uri.toUriString();
    }

    //TOURITEM 데이터 추가
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

//        UriComponents uri = UriComponentsBuilder
//                .fromHttpUrl("https://apis.data.go.kr/B551011/KorService1/")
//                .path(searchType)
//                .queryParam("numOfRows",totalData)
//                .queryParam("pageNo", 1)
//                .queryParam("MobileOS","ETC")
//                .queryParam("MobileApp","THREEGO")
//                .queryParam("serviceKey",ApiKey.TOURAPI_KEY_1)
//                .queryParam("_type","json")
//                .build();

        String result = "";

        try {
            URL url = new URL(makeUri(table,params));
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
                if(table.equals("TourItem")){
                    TourItem tourItem = objectMapper.readValue(itemStr, TourItem.class);
                    tourItemRepository.save(tourItem);
                }else if(table.equals("Area")){
                    Area area = objectMapper.readValue(itemStr, Area.class);
                    areaRepository.save(area);
                    params.put("Area", area.getJ_areacode());
                    insertSigungu(makeUri(table,params));
                }else if(table.equals("Category")){
                    Cat1 cat1 = objectMapper.readValue(itemStr, Cat1.class);
                    cat1Repository.save(cat1);
                    params.put("Cat1",cat1.getCat1());
                    insertCat2(makeUri(table,params), params);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void insertSigungu(String url){
        String result = "";
        try {
            URL newUrl = new URL(url);
            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(newUrl.openStream(), "UTF-8"));

            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");

            for(Object itemObj : item){
                String itemStr = itemObj.toString();
                Sigungu sigungu = objectMapper.readValue(itemStr, Sigungu.class);
                sigunguRepository.save(sigungu);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void insertCat2(String url, Map<String, Object> params){
        String result = "";
        try {
            URL newUrl = new URL(url);
            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(newUrl.openStream(), "UTF-8"));

            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");

            for(Object itemObj : item){
                String itemStr = itemObj.toString();
                Cat2 cat2 = objectMapper.readValue(itemStr, Cat2.class);
                cat2Repository.save(cat2);

                params.put("Cat2",cat2.getCat2());
                insertCat3(makeUri("Category",params));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void insertCat3(String url){
        String result = "";
        try {
            URL newUrl = new URL(url);
            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(newUrl.openStream(), "UTF-8"));

            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");

            for(Object itemObj : item){
                String itemStr = itemObj.toString();
                Cat3 cat3 = objectMapper.readValue(itemStr, Cat3.class);
                cat3Repository.save(cat3);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


}

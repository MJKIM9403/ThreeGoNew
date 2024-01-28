package com.io.threegonew.service;

import com.io.threegonew.ApiKey;
import com.io.threegonew.dto.TourItemResponse;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TourItemContentService {

    /* URI path */
    private final String COMMON = "detailCommon1"; // 공용정보, overview
    private final String IMAGES = "detailImage1"; // 이미지 정보 조회
    private final String INFO = "detailInfo1"; // 상세 정보 조회

//    public TourItemContentResponse getContentInfo(TourItemResponse tourItemResponse){
//        TourItemContentResponse contentResponse = TourItemContentResponse.builder()
//    }


    private String makeURI(TourItemResponse tourItemResponse, String path){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl("https://apis.data.go.kr/B551011/KorService1/")
                .path(path)
                .queryParam("MobileOS","ETC")
                .queryParam("MobileApp","THREEGO");

        if(path.equals(IMAGES)){
            uriBuilder.queryParam("numOfRows",10)
                        .queryParam("pageNo", 1)
                        .queryParam("subImageYN","Y");
        }
        if(path.equals(COMMON) || path.equals(INFO)){
            uriBuilder.queryParam("contentTypeId", tourItemResponse.getContenttypeid());
        }
        if(path.equals(COMMON)){
            uriBuilder.queryParam("overviewYN","Y");
        }

        uriBuilder.queryParam("contentId",tourItemResponse.getContentid())
                .queryParam("serviceKey", ApiKey.TOURAPI_KEY_1)
                .queryParam("_type","json")
                .build();

        UriComponents uri = uriBuilder.build();

        return uri.toUriString();
    }

    private JSONArray parseItem(String jsonStr){
        JSONArray item = null;

        try{
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonStr);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            item = (JSONArray) items.get("item");
        }catch(ParseException e){
            e.printStackTrace();
        }

        return item;
    }

    public List<String> getImagesURL(TourItemResponse tourItemResponse){
        List<String> imagesURL = new ArrayList<>();
        String result = "";

        try{
            URL url = new URL(makeURI(tourItemResponse, IMAGES));
            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

            result = bf.readLine();

            JSONArray item = parseItem(result);

            for(Object itemObj : item) {
                JSONObject itemJson = (JSONObject) itemObj;

                imagesURL.add((String) itemJson.get("originimgurl"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return imagesURL;
    }

    public String getOverview(TourItemResponse tourItemResponse){
        String overview = "";
        String result = "";

        try{
            URL url = new URL(makeURI(tourItemResponse, IMAGES));
            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

            result = bf.readLine();

            JSONArray item = parseItem(result);
            JSONObject itemJson = (JSONObject) item.get(0);

            overview = (String) itemJson.get("overview");


        }catch (Exception e){
            e.printStackTrace();
        }

        return overview;
    }
}

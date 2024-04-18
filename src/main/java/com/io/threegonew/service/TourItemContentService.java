package com.io.threegonew.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.threegonew.Key;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.dto.MoreTourItemDTO;
import com.io.threegonew.dto.TourItemContentResponse;
import com.io.threegonew.dto.TourItemResponse;
import com.io.threegonew.repository.TourItemRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourItemContentService {

    private final TourItemRepository tourItemRepository;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    /* URI path */
    private final String COMMON = "detailCommon1"; // 공용정보, overview
    private final String IMAGES = "detailImage1"; // 이미지 정보 조회
    private final String INFO = "detailIntro1"; // 상세 정보 조회
    private final String COURSE = "detailInfo1";

    private String homepage;

    public TourItemContentResponse getContentInfo(TourItemResponse tourItemResponse){
        TourItemContentResponse contentResponse = TourItemContentResponse.builder()
                .tourItemResponse(tourItemResponse)
                .imagesURL(getImagesURL(tourItemResponse))
                .overview(getOverview(tourItemResponse))
                .detailInfo(getDetailInfo(tourItemResponse))
                .moreTourItems(getMoreItems(tourItemResponse))
                .bookmarkCount(tourItemResponse.getBookmarkCount())
                .build();

        return contentResponse;
    }

//    private String makeURL(TourItemResponse tourItemResponse, String path) {
//        UriComponentsBuilder uriBuilder = UriComponentsBuilder
//                .fromHttpUrl("https://apis.data.go.kr/B551011/KorService1/")
//                .path(path)
//                .queryParam("MobileOS","ETC")
//                .queryParam("MobileApp","THREEGO");
//
//        if(path.equals(IMAGES)){
//            uriBuilder.queryParam("numOfRows",10)
//                    .queryParam("pageNo", 1)
//                    .queryParam("subImageYN","Y");
//        }
//        if(path.equals(COURSE)){
//            uriBuilder.queryParam("numOfRows",100)
//                    .queryParam("pageNo", 1);
//        }
//        if(path.equals(COMMON) || path.equals(INFO) || path.equals(COURSE)){
//            uriBuilder.queryParam("contentTypeId", tourItemResponse.getContenttypeid());
//        }
//        if(path.equals(COMMON)){
//            uriBuilder.queryParam("overviewYN","Y");
//        }
//
//        uriBuilder.queryParam("contentId",tourItemResponse.getContentid())
//                .queryParam("serviceKey", ApiKey.TOURAPI_KEY_1)
//                .queryParam("_type","json")
//                .build();
//
//        UriComponents uri = uriBuilder.build();
//
//        return uri.toUriString();
//    }

//    private ResponseEntity<List<String>> getImagesURL2(TourItemResponse tourItemResponse){
//        List<CompletableFuture<Void>> futures = new ArrayList<>();
//        try{
//            URL url = new URL(makeURL(tourItemResponse, IMAGES))
//        }
//    }


    private String convertJsonToStr(TourItemResponse tourItemResponse, String path){
        String jsonToStr = "";

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
        if(path.equals(COURSE)){
            uriBuilder.queryParam("numOfRows",100)
                    .queryParam("pageNo", 1);
        }
        if(path.equals(COMMON) || path.equals(INFO) || path.equals(COURSE)){
            uriBuilder.queryParam("contentTypeId", tourItemResponse.getContenttypeid());
        }
        if(path.equals(COMMON)){
            uriBuilder.queryParam("overviewYN","Y");
        }

        uriBuilder.queryParam("contentId",tourItemResponse.getContentid())
                .queryParam("serviceKey", Key.TOURAPI_KEY_1)
                .queryParam("_type","json")
                .build();

        UriComponents uri = uriBuilder.build();


        System.out.println(uri.toUriString());

        try {
            URL url = new URL(uri.toUriString());
            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

            jsonToStr = bf.readLine();

        }catch (Exception e){
            e.printStackTrace();
        }


        return jsonToStr;
    }

    private JSONArray parseItem(String jsonStr){
        JSONArray item = new JSONArray();

        try{
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonStr);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            if(body.get("items").equals("")) return item;
            JSONObject items = (JSONObject) body.get("items");
            item = (JSONArray) items.get("item");

        }catch(ParseException e){
            e.printStackTrace();
        }

        return item;
    }

    private List<String> getImagesURL(TourItemResponse tourItemResponse){
        List<String> imagesURL = new ArrayList<>();
        String result = "";

        try{
            result = convertJsonToStr(tourItemResponse, IMAGES);

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

    private String getOverview(TourItemResponse tourItemResponse){
        String result = "";
        String overview = "";

        try{
            result = convertJsonToStr(tourItemResponse, COMMON);

            JSONArray item = parseItem(result);
            JSONObject itemJson = (JSONObject) item.get(0);

            homepage = (String) itemJson.get("homepage");
            overview = (String) itemJson.get("overview");


        }catch (Exception e){
            e.printStackTrace();
        }

        return overview;
    }

    private Map<String, String> getDetailInfo(TourItemResponse tourItemResponse){
        Map<String, String> detailInfo = new HashMap<>();
        String result = "";

        try{
            result = convertJsonToStr(tourItemResponse, INFO);

            JSONArray item = parseItem(result);
            JSONObject itemJson = (JSONObject) item.get(0);

            String restDate = "";
            String parking = "";

            switch (tourItemResponse.getContenttypeid()){
                case "12" :
                    restDate = itemJson.get("restdate").equals("") ? "연중무휴" : (String) itemJson.get("restdate");
                    parking = itemJson.get("parking").equals("") ? "주차 불가" : (String) itemJson.get("parking");
                    String guide = itemJson.get("expguide").equals("") ? "없음" : (String) itemJson.get("expguide");
                    String ageRange = itemJson.get("expagerange").equals("") ? "없음" : (String) itemJson.get("expagerange");

                    detailInfo.put("관광안내원",guide);
                    detailInfo.put("이용문의",(String) itemJson.get("infocenter"));
                    detailInfo.put("입장제한연령",ageRange);
                    detailInfo.put("이용시간",(String) itemJson.get("usetime"));
                    detailInfo.put("수용인원", (String) itemJson.get("accomcount"));
                    detailInfo.put("휴무일", restDate);
                    detailInfo.put("주차여부", parking);
                    detailInfo.put("홈페이지", homepage);
                    break;
                case "14" :
                    restDate = itemJson.get("restdateculture").equals("") ? "연중무휴" : (String) itemJson.get("restdateculture");
                    parking = itemJson.get("parkingculture").equals("") ? "주차 불가" : (String) itemJson.get("parkingculture");

                    detailInfo.put("이용요금",(String) itemJson.get("usefee"));
                    detailInfo.put("이용문의",(String) itemJson.get("infocenterculture"));
                    detailInfo.put("관람소요시간",(String) itemJson.get("spendtime"));
                    detailInfo.put("이용시간",(String) itemJson.get("usetimeculture"));
                    detailInfo.put("수용인원",(String) itemJson.get("accomcountculture"));
                    detailInfo.put("휴무일",restDate);
                    detailInfo.put("주차여부", parking);
                    detailInfo.put("홈페이지", homepage);
                    break;
                case "15" :
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date today = new Date();
                    Date startDay = dateFormat.parse((String) itemJson.get("eventstartdate"));
                    Date endDay = dateFormat.parse((String) itemJson.get("eventenddate"));

                    String period = newDateFormat.format(startDay) + " ~ " + newDateFormat.format(endDay);

                    String state = "";

                    if(startDay.compareTo(today) > 0){
                        state = "D-" + calcDay(startDay, today);
                    }else if(startDay.compareTo(today) <= 0 && endDay.compareTo(today) >= 0){
                        state = "행사 " + calcDay(today, startDay) + 1 + "일차";
                    }else {
                        state = "종료된 행사";
                    }

                    String tel = "";
                    if(!((String)itemJson.get("sponsor1tel")).isEmpty()) tel += "[주관] " + itemJson.get("sponsor1tel");
                    if(!((String)itemJson.get("sponsor1tel")).isEmpty() && !((String)itemJson.get("sponsor2tel")).isEmpty()) tel += "<br/>";
                    if(!((String)itemJson.get("sponsor2tel")).isEmpty()) tel += "[주최] " + itemJson.get("sponsor2tel");

                    detailInfo.put("진행상태",state);
                    detailInfo.put("행사기간", period);
                    detailInfo.put("공연시간",(String) itemJson.get("playtime"));
                    detailInfo.put("장소",itemJson.get("eventplace") + "<br/>(" + tourItemResponse.getAddress() + ")");
                    detailInfo.put("이용요금",(String) itemJson.get("usetimefestival"));
                    detailInfo.put("주관",(String) itemJson.get("sponsor1"));
                    detailInfo.put("입장제한연령",(String) itemJson.get("agelimit"));
                    detailInfo.put("주최",(String) itemJson.get("sponsor2"));
                    detailInfo.put("예매",(String) itemJson.get("bookingplace"));
                    detailInfo.put("이용문의",tel);
                    detailInfo.put("위치안내",(String) itemJson.get("placeinfo"));
                    detailInfo.put("홈페이지", homepage);
                    break;
                case "25" :
                    detailInfo.put("코스 총 거리",(String) itemJson.get("distance"));
                    detailInfo.put("소요시간",(String) itemJson.get("taketime"));
                    detailInfo.put("문의 및 안내",(String) itemJson.get("infocentertourcourse"));
                    break;
                case "28" :
                    restDate = itemJson.get("restdateleports").equals("") ? "연중무휴" : (String) itemJson.get("restdateleports");
                    parking = itemJson.get("parkingleports").equals("") ? "주차 불가" : (String) itemJson.get("parkingleports");

                    detailInfo.put("문의 및 안내",(String) itemJson.get("infocenterleports"));
                    detailInfo.put("이용요금",(String) itemJson.get("usefeeleports"));
                    detailInfo.put("이용시간",(String) itemJson.get("usetimeleports"));
                    detailInfo.put("휴무일", restDate);
                    detailInfo.put("주차여부", parking);
                    detailInfo.put("홈페이지", homepage);
                    break;
                case "32" :
                    parking = itemJson.get("parkinglodging").equals("") ? "주차 불가" : (String) itemJson.get("parkinglodging");

                    detailInfo.put("객실유형",(String) itemJson.get("roomtype"));
                    detailInfo.put("문의 및 안내",(String) itemJson.get("infocenterlodging"));
                    detailInfo.put("체크인",(String) itemJson.get("checkintime"));
                    detailInfo.put("예약안내",(String) itemJson.get("reservationlodging"));
                    detailInfo.put("체크아웃",(String) itemJson.get("checkouttime"));
                    detailInfo.put("부대시설",(String) itemJson.get("subfacility"));
                    detailInfo.put("주차여부", parking);
                    detailInfo.put("홈페이지", homepage);
                    break;
                case "38" :
                    restDate = itemJson.get("restdateshopping").equals("") ? "연중무휴" : (String) itemJson.get("restdateshopping");
                    parking = itemJson.get("parkingshopping").equals("") ? "주차 불가" : (String) itemJson.get("parkingshopping");

                    detailInfo.put("판매품목",(String) itemJson.get("saleitem"));
                    detailInfo.put("운영시간",(String) itemJson.get("opentime"));
                    detailInfo.put("전화번호",(String) itemJson.get("infocentershopping"));
                    detailInfo.put("휴무일", restDate);
                    detailInfo.put("주차여부", parking);
                    detailInfo.put("홈페이지", homepage);
                    break;
                case "39" :
                    restDate = itemJson.get("restdatefood").equals("") ? "연중무휴" : (String) itemJson.get("restdatefood");
                    parking = itemJson.get("parkingfood").equals("") ? "주차 불가" : (String) itemJson.get("parkingfood");

                    detailInfo.put("포장",(String) itemJson.get("packing"));
                    detailInfo.put("대표메뉴",(String) itemJson.get("firstmenu"));
                    detailInfo.put("영업시간",(String) itemJson.get("opentimefood"));
                    detailInfo.put("메뉴",(String) itemJson.get("treatmenu"));
                    detailInfo.put("카드사용여부",(String) itemJson.get("chkcreditcardfood"));
                    detailInfo.put("전화번호",(String) itemJson.get("infocenterfood"));
                    detailInfo.put("휴무일", restDate);
                    detailInfo.put("주차여부", parking);
                    detailInfo.put("홈페이지", homepage);
                    break;



            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return detailInfo;
    }

    private List<MoreTourItemDTO> getMoreItems(TourItemResponse tourItemResponse){
        String type = tourItemResponse.getContenttypeid();
        List<MoreTourItemDTO> moreItems = new ArrayList<>();

        switch (type){
            case "25" :
                String result = "";

                try{
                    result = convertJsonToStr(tourItemResponse, COURSE);

                    JSONArray item = parseItem(result);

                    for(Object itemObj : item) {
                        JSONObject itemJson = (JSONObject) itemObj;
                        String subContentId = (String) itemJson.get("subcontentid");
                        TourItem subTourItem = tourItemRepository.findById(subContentId)
                                .orElseThrow(()-> new IllegalArgumentException("not found : SubTourItem"));

                        MoreTourItemDTO dto = MoreTourItemDTO.builder()
                                                .contentid(subTourItem.getContentid())
                                                .title(subTourItem.getTitle())
                                                .firstimage(subTourItem.getFirstimage())
                                                .mapx(subTourItem.getMapx())
                                                .mapy(subTourItem.getMapy())
                                                .build();

                        moreItems.add(dto);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            default:
                moreItems = tourItemRepository.findSubItemByCat3OrderByDistance(tourItemResponse).stream()
                        .map(moreTourItemInterface -> modelMapper.map(moreTourItemInterface, MoreTourItemDTO.class))
                        .collect(Collectors.toList());
        }

        return moreItems;
    }

    private int calcDay(Date date1, Date date2){
        long diff = date1.getTime() - date2.getTime();

        return (int)(Math.ceil((double) diff / (1000 * 60 * 60 * 24)));
    }
}

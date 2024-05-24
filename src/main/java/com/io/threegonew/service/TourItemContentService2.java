package com.io.threegonew.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.io.threegonew.domain.TourItem;
import com.io.threegonew.dto.MoreTourItemDTO;
import com.io.threegonew.dto.TourItemContentResponse;
import com.io.threegonew.dto.TourItemResponse;
import com.io.threegonew.repository.TourItemRepository;
import lombok.RequiredArgsConstructor;

import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple4;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourItemContentService2 {
    private final TourItemRepository tourItemRepository;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    /* URI path */
    private final String COMMON = "detailCommon1"; // 공용정보, overview
    private final String IMAGES = "detailImage1"; // 이미지 정보 조회
    private final String INFO = "detailIntro1"; // 상세 정보 조회
    private final String COURSE = "detailInfo1";

    @Value("${keys.api.tour}")
    private String TOUR_API_KEY;

    private String homepage;

    public TourItemContentResponse getContentInfo(TourItemResponse tourItemResponse){
        Mono<JSONObject> imagesResponse = getResponse(tourItemResponse, IMAGES).subscribeOn(Schedulers.boundedElastic());
        Mono<JSONObject> overviewResponse = getResponse(tourItemResponse, COMMON).subscribeOn(Schedulers.boundedElastic());
        Mono<JSONObject> detailResponse = getResponse(tourItemResponse, INFO).subscribeOn(Schedulers.boundedElastic());
        Mono<JSONObject> moreResponse = getResponse(tourItemResponse, COURSE).subscribeOn(Schedulers.boundedElastic());

        Tuple4<JSONObject, JSONObject, JSONObject, JSONObject> tuple =
                Mono.zip(imagesResponse, overviewResponse, detailResponse, moreResponse).block();

        JSONObject imagesResponseObj = tuple.getT1();
        JSONObject overviewResponseObj = tuple.getT2();
        JSONObject detailResponseObj = tuple.getT3();
        JSONObject moreResponseObj = tuple.getT4();

        return combineContentResponse(tourItemResponse,
                imagesResponseObj,
                overviewResponseObj,
                detailResponseObj,
                moreResponseObj);
    }

    private TourItemContentResponse combineContentResponse(TourItemResponse tourItemResponse,
                                                       JSONObject imagesResponse,
                                                       JSONObject overviewResponse,
                                                       JSONObject detailResponse,
                                                       JSONObject moreResponse)
    {
        List<Map<String,String>> imagesItem = getItemFromJson(imagesResponse);
        List<Map<String,String>> overviewItem = getItemFromJson(overviewResponse);
        List<Map<String,String>> detailItem = getItemFromJson(detailResponse);
        List<Map<String,String>> moreItem = getItemFromJson(moreResponse);

        TourItemContentResponse contentResponse = TourItemContentResponse.builder()
                .tourItemResponse(tourItemResponse)
                .imagesURL(getImagesURL(imagesItem))
                .overview(getOverview(overviewItem))
                .detailInfo(getDetailInfo(detailItem, tourItemResponse))
                .moreTourItems(getMoreItems(moreItem, tourItemResponse))
                .bookmarkCount(tourItemResponse.getBookmarkCount())
                .build();

        return contentResponse;
    }

    private Mono<JSONObject> getResponse(TourItemResponse tourItemResponse, String path){
        System.out.println(path + " :: start");
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .build();

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
                .queryParam("serviceKey", TOUR_API_KEY)
                .queryParam("_type","json")
                .build();

        UriComponents uri = uriBuilder.build();

//        System.out.println(uri.toUriString());
        System.out.println(path + " :: end");

        return webClient.get()
               .uri(uri.toUriString())
               .retrieve()
               .bodyToMono(JSONObject.class);
    }

    private List<Map<String, String>> getItemFromJson(JSONObject jsonObject){
        List item = new ArrayList<>();

        try{
            Map<String, Object> responseMap = objectMapper.convertValue(jsonObject.get("response"), Map.class);
            Map<String, Object> bodyMap = objectMapper.convertValue(responseMap.get("body"), Map.class);
            if(bodyMap.get("items").equals("")) return item;
            Map<String, String> itemsMap = objectMapper.convertValue(bodyMap.get("items"), Map.class);

            item = objectMapper.convertValue(itemsMap.get("item"), List.class);
        }catch (Exception e){
            e.printStackTrace();
        }


        return item;
    }

    private List<String> getImagesURL(List<Map<String,String>> item){
        List<String> imagesURL = new ArrayList<>();

        try{
            for(Map<String,String> itemMap : item) {
                imagesURL.add(itemMap.get("originimgurl"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return imagesURL;
    }

    private String getOverview(List<Map<String,String>> item){
        String overview = "";

        try{
            Map<String,String> itemMap = item.get(0);

            homepage = itemMap.get("homepage");
            overview = itemMap.get("overview");


        }catch (Exception e){
            e.printStackTrace();
        }

        return overview;
    }

    private Map<String, String> getDetailInfo(List<Map<String,String>> item, TourItemResponse tourItemResponse){
        Map<String, String> detailInfo = new HashMap<>();

        try{
            Map<String,String> itemMap = item.get(0);

            String restDate = "";
            String parking = "";

            switch (tourItemResponse.getContenttypeid()){
                case "12" :
                    restDate = itemMap.get("restdate").equals("") ? "연중무휴" : itemMap.get("restdate");
                    parking = itemMap.get("parking").equals("") ? "주차 불가" : itemMap.get("parking");
                    String guide = itemMap.get("expguide").equals("") ? "없음" : itemMap.get("expguide");
                    String ageRange = itemMap.get("expagerange").equals("") ? "없음" : itemMap.get("expagerange");

                    detailInfo.put("관광안내원", guide);
                    detailInfo.put("이용문의", itemMap.get("infocenter"));
                    detailInfo.put("입장제한연령",ageRange);
                    detailInfo.put("이용시간", itemMap.get("usetime"));
                    detailInfo.put("수용인원", itemMap.get("accomcount"));
                    detailInfo.put("휴무일", restDate);
                    detailInfo.put("주차여부", parking);
                    detailInfo.put("홈페이지", homepage);
                    break;
                case "14" :
                    restDate = itemMap.get("restdateculture").equals("") ? "연중무휴" : itemMap.get("restdateculture");
                    parking = itemMap.get("parkingculture").equals("") ? "주차 불가" : itemMap.get("parkingculture");

                    detailInfo.put("이용요금", itemMap.get("usefee"));
                    detailInfo.put("이용문의", itemMap.get("infocenterculture"));
                    detailInfo.put("관람소요시간", itemMap.get("spendtime"));
                    detailInfo.put("이용시간",itemMap.get("usetimeculture"));
                    detailInfo.put("수용인원",itemMap.get("accomcountculture"));
                    detailInfo.put("휴무일",restDate);
                    detailInfo.put("주차여부", parking);
                    detailInfo.put("홈페이지", homepage);
                    break;
                case "15" :
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date today = new Date();
                    Date startDay = dateFormat.parse(itemMap.get("eventstartdate"));
                    Date endDay = dateFormat.parse(itemMap.get("eventenddate"));

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
                    if(!(itemMap.get("sponsor1tel")).isEmpty()) tel += "[주관] " + itemMap.get("sponsor1tel");
                    if(!(itemMap.get("sponsor1tel")).isEmpty() && !(itemMap.get("sponsor2tel")).isEmpty()) tel += "<br/>";
                    if(!(itemMap.get("sponsor2tel")).isEmpty()) tel += "[주최] " + itemMap.get("sponsor2tel");

                    detailInfo.put("진행상태",state);
                    detailInfo.put("행사기간", period);
                    detailInfo.put("공연시간",itemMap.get("playtime"));
                    detailInfo.put("장소",itemMap.get("eventplace") + "<br/>(" + tourItemResponse.getAddress() + ")");
                    detailInfo.put("이용요금",itemMap.get("usetimefestival"));
                    detailInfo.put("주관",itemMap.get("sponsor1"));
                    detailInfo.put("입장제한연령",itemMap.get("agelimit"));
                    detailInfo.put("주최",itemMap.get("sponsor2"));
                    detailInfo.put("예매",itemMap.get("bookingplace"));
                    detailInfo.put("이용문의",tel);
                    detailInfo.put("위치안내",itemMap.get("placeinfo"));
                    detailInfo.put("홈페이지", homepage);
                    break;
                case "25" :
                    detailInfo.put("코스 총 거리",itemMap.get("distance"));
                    detailInfo.put("소요시간",itemMap.get("taketime"));
                    detailInfo.put("문의 및 안내",itemMap.get("infocentertourcourse"));
                    break;
                case "28" :
                    restDate = itemMap.get("restdateleports").equals("") ? "연중무휴" : itemMap.get("restdateleports");
                    parking = itemMap.get("parkingleports").equals("") ? "주차 불가" : itemMap.get("parkingleports");

                    detailInfo.put("문의 및 안내",itemMap.get("infocenterleports"));
                    detailInfo.put("이용요금",itemMap.get("usefeeleports"));
                    detailInfo.put("이용시간",itemMap.get("usetimeleports"));
                    detailInfo.put("휴무일", restDate);
                    detailInfo.put("주차여부", parking);
                    detailInfo.put("홈페이지", homepage);
                    break;
                case "32" :
                    parking = itemMap.get("parkinglodging").equals("") ? "주차 불가" : itemMap.get("parkinglodging");

                    detailInfo.put("객실유형",itemMap.get("roomtype"));
                    detailInfo.put("문의 및 안내",itemMap.get("infocenterlodging"));
                    detailInfo.put("체크인",itemMap.get("checkintime"));
                    detailInfo.put("예약안내",itemMap.get("reservationlodging"));
                    detailInfo.put("체크아웃",itemMap.get("checkouttime"));
                    detailInfo.put("부대시설",itemMap.get("subfacility"));
                    detailInfo.put("주차여부", parking);
                    detailInfo.put("홈페이지", homepage);
                    break;
                case "38" :
                    restDate = itemMap.get("restdateshopping").equals("") ? "연중무휴" : itemMap.get("restdateshopping");
                    parking = itemMap.get("parkingshopping").equals("") ? "주차 불가" : itemMap.get("parkingshopping");

                    detailInfo.put("판매품목",itemMap.get("saleitem"));
                    detailInfo.put("운영시간",itemMap.get("opentime"));
                    detailInfo.put("전화번호",itemMap.get("infocentershopping"));
                    detailInfo.put("휴무일", restDate);
                    detailInfo.put("주차여부", parking);
                    detailInfo.put("홈페이지", homepage);
                    break;
                case "39" :
                    restDate = itemMap.get("restdatefood").equals("") ? "연중무휴" : itemMap.get("restdatefood");
                    parking = itemMap.get("parkingfood").equals("") ? "주차 불가" : itemMap.get("parkingfood");

                    detailInfo.put("포장",itemMap.get("packing"));
                    detailInfo.put("대표메뉴",itemMap.get("firstmenu"));
                    detailInfo.put("영업시간",itemMap.get("opentimefood"));
                    detailInfo.put("메뉴",itemMap.get("treatmenu"));
                    detailInfo.put("카드사용여부",itemMap.get("chkcreditcardfood"));
                    detailInfo.put("전화번호",itemMap.get("infocenterfood"));
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

    private List<MoreTourItemDTO> getMoreItems(List<Map<String,String>> item, TourItemResponse tourItemResponse){
        String type = tourItemResponse.getContenttypeid();
        List<MoreTourItemDTO> moreItems = new ArrayList<>();

        switch (type){
            case "25" :
                try{
                    for(Map<String,String> itemMap : item) {
                        String subContentId = itemMap.get("subcontentid");
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

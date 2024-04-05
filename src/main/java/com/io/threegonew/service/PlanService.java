package com.io.threegonew.service;

import com.io.threegonew.domain.*;
import com.io.threegonew.dto.*;
import com.io.threegonew.repository.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final Cat3Repository cat3Repository;
    private final PlannerRepository plannerRepository;
    private final PlanRepository planRepository;
    private final AreaRepository areaRepository;
    private final SigunguRepository sigunguRepository;
    private final TourItemRepository tourItemRepository;
    private final ModelMapper modelMapper;

    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    public List<Plan> findByPlannerId(Long plannerId) {
        return planRepository.findByPlannerId(plannerId);
    }

    public List<SelectPlanResponse> findPlanListByPlannerId(Long plannerId) {
        List<SelectPlanResponse> planList = planRepository.findByPlannerId(plannerId).stream()
                .map(plan -> selectPlanMapper(plan))
                .collect(Collectors.toList());
        return planList;
    }

    private SelectPlanResponse selectPlanMapper(Plan plan){
        return SelectPlanResponse.builder()
                .planId(plan.getPlanId())
                .plannerId(plan.getPlannerId())
                .day(plan.getDay())
                .tourItem(TourItemSimpleResponse.builder()
                                    .contentid(plan.getTourItem().getContentid())
                                    .title(plan.getTourItem().getTitle())
                                    .build())
                .build();
    }


    public TreeMap<String, List<Plan>> findByPlannerGroupByDay(Long plannerId) {
        List<Plan> planList = planRepository.findByPlannerId(plannerId);

        int day = 0;
        String key = "";
        TreeMap<String, List<Plan>> planMap = new TreeMap<>();
        List<Plan> dayPlanList = new ArrayList<>();

        for(Plan plan : planList){
            if(day != plan.getDay()){
                if(day > 0){
                    planMap.put(key, dayPlanList);
                }
                day = plan.getDay();
                key = day + "day";
                dayPlanList = new ArrayList<>();
            }
            dayPlanList.add(plan);
        }

        if(!dayPlanList.isEmpty()) {
            planMap.put(key, dayPlanList);
        }

        return planMap;
    }

    public List<PlanDTO<TourItemResponse>> findByPlannerIdAndDay(PlanRequest planRequest) {
        Planner planner = plannerRepository.findByPlannerId(planRequest.getPlannerId())
                .orElseThrow(() -> new IllegalArgumentException("Planner not found"));
        List<Plan> plans = planRepository.findByPlannerIdAndDay(planner.getPlannerId(), planRequest.getDay());


        List<PlanDTO<TourItemResponse>> planDTOs = plans.stream().map(plan -> {
            // TourItemResponse 정보를 받아와서 dtoList에 추가
            List<TourItemResponse> tourItemResponse = Collections.singletonList(tourItemMapper(plan.getTourItem()));

            return PlanDTO.<TourItemResponse>builder()
                    .planId(plan.getPlanId())
                    .plannerId(plan.getPlannerId())
                    .day(plan.getDay())
                    .order(plan.getOrder())
                    .dtoList(tourItemResponse)
                    .build();
        }).collect(Collectors.toList());

        return planDTOs;
    }



    public List<Area> findAllAreas() {
        return areaRepository.findAll();
    }

    public List<Sigungu> findSigunguByAreaCode(Integer areaCode) {
        return sigunguRepository.findByAreaCode(areaCode);
    }

    public Plan save(AddPlanRequest dto, String userId) {
        // tourItem을 찾아서 설정
        TourItem tourItem = tourItemRepository.findById(dto.getContentId()).orElse(null);

        Plan plan = planRepository.save(Plan.builder()
                .userId(userId)
                .plannerId(dto.getPlannerId())
                .day(dto.getDay())
                .order(dto.getOrder())
                .tourItem(tourItem)
                .build());


        return plan;
    }


    // TourItem을 TourItemResponse로 매핑하는 메서드
    private TourItemResponse tourItemMapper(TourItem tourItem) {
        String defaultImage = "../assets/img/no_img.jpg"; // 기본 이미지 경로 설정

        return TourItemResponse.builder()
                .contentid(tourItem.getContentid())
                .cat1(tourItem.getCat1())
                .cat2(tourItem.getCat2())
                .cat3(tourItem.getCat3())
                .fullCategoryName(getFullCategoryName(tourItem.getCat3()))
                .areacode(tourItem.getAreacode())
                .contenttypeid(tourItem.getContenttypeid())
                .address(getAddress(tourItem))
                .firstimage(tourItem.getFirstimage() != null && !tourItem.getFirstimage().isEmpty() ? tourItem.getFirstimage() : defaultImage)
                .mapx(tourItem.getMapx())
                .mapy(tourItem.getMapy())
                .mlevel(tourItem.getMlevel())
                .sigungucode(tourItem.getSigungucode())
                .tel(tourItem.getTel())
                .title(cropTitle(tourItem.getTitle()))
                .bookmarkCount((long) tourItem.getBookmarkList().size())
                .build();
    }

    private String getFullCategoryName(String cat3Code){
        Cat3 cat3 = cat3Repository.findById(cat3Code)
                .orElseThrow(()-> new IllegalArgumentException("not found : cat3"));

        return cat3.getCat2().getCat1().getCat1Name() + " > " + cat3.getCat2().getCat2Name() + " > " + cat3.getCat3Name();
    }

    private String getAddress(TourItem tourItem){
        StringBuilder address = new StringBuilder();
        if(tourItem.getAddr1() != null){
            address.append(tourItem.getAddr1());
        }
        if(tourItem.getAddr2() != null){
            address.append(" ");
            address.append(tourItem.getAddr2());
        }
        return address.toString();
    }

    private String cropTitle(String title){
        if(title.contains("[한국")){
            title = title.substring(0,title.lastIndexOf("[한국"));
        }
        return title;
    }
}

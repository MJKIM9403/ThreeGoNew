package com.io.threegonew.service;

import com.io.threegonew.domain.*;
import com.io.threegonew.dto.TourItemResponse;
import com.io.threegonew.dto.TourItemSelectRequest;
import com.io.threegonew.repository.*;
import com.io.threegonew.repository.spec.TourItemSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourItemService {
    private final Cat1Repository cat1Repository;
    private final Cat2Repository cat2Repository;
    private final Cat3Repository cat3Repository;
    private final SigunguRepository sigunguRepository;
    private final ContentTypeRepository contentTypeRepository;
    private final AreaRepository areaRepository;
    private final TourItemRepository tourItemRepository;
    public List<Cat1> findCat1List(){
        return cat1Repository.findAll();
    }

    public List<Cat2> findCat2List(String cat1Code){
        Cat1 cat1 = cat1Repository.findById(cat1Code)
                .orElseThrow(()-> new IllegalArgumentException("not found : cat1"));

        return cat1.getCat2List();
    }

    public List<Cat3> findCat3List(String cat2Code){
        Cat2 cat2 = cat2Repository.findById(cat2Code)
                .orElseThrow(()-> new IllegalArgumentException("not found : cat2"));

        return cat2.getCat3List();
    }

    public String getFullCategoryName(String cat3Code){
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

    public List<Sigungu> findSigunguList(Integer areaCode){
        return sigunguRepository.findByAreaCode(areaCode);
    }

    public List<ContentType> findContentTypeList(){
        return contentTypeRepository.findAll();
    }

    public List<Area> findAreaList(){
        return areaRepository.findAll();
    }

    public Area findArea(Integer areaCode){
        return areaRepository.findById(areaCode)
                .orElseThrow(()-> new IllegalArgumentException("not found : Area"));
    }

    @Transactional(readOnly = true)
    public Page<TourItemResponse> findSelectedTourItemList(TourItemSelectRequest request){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Specification<TourItem> spec = (root, query, criteriaBuilder) -> null;

        if(request.getAreaCode() != null && !request.getAreaCode().isEmpty()){
            spec = spec.and(TourItemSpecification.equalAreacode(request.getAreaCode()));
            System.out.println("area: " + request.getAreaCode());
        }
        if(request.getSigunguCode() != null && !request.getSigunguCode().isEmpty()){
            spec = spec.and(TourItemSpecification.equalSigungucode(request.getSigunguCode()));
            System.out.println("sigungu: " + request.getSigunguCode());
        }
        if(request.getCat1() != null && !request.getCat1().isEmpty()){
            spec = spec.and(TourItemSpecification.equalCat1(request.getCat1()));
            System.out.println("cat1: " + request.getCat1());
        }
        if(request.getCat2() != null && !request.getCat2().isEmpty()){
            spec = spec.and(TourItemSpecification.equalCat2(request.getCat2()));
            System.out.println("cat2: " + request.getCat2());
        }
        if(request.getCat3() != null && !request.getCat3().isEmpty()){
            spec = spec.and(TourItemSpecification.equalCat3(request.getCat3()));
            System.out.println("cat3: " + request.getCat3());
        }
        if(request.getContentTypeId() != null && !request.getContentTypeId().isEmpty()){
            spec = spec.and(TourItemSpecification.equalContenttypeid(request.getContentTypeId()));
            System.out.println("contentTypeId: " + request.getContentTypeId());
        }

        Page<TourItemResponse> tourItemResponseList = tourItemRepository.findAll(spec, pageable)
                .map(tourItem -> TourItemResponse.builder()
                                .contentid(tourItem.getContentid())
                                .cat1(tourItem.getCat1())
                                .cat2(tourItem.getCat2())
                                .cat3(tourItem.getCat3())
                                .fullCategoryName(getFullCategoryName(tourItem.getCat3()))
                                .areacode(tourItem.getAreacode())
                                .contenttypeid(tourItem.getContenttypeid())
                                .address(getAddress(tourItem))
                                .firstimage(tourItem.getFirstimage())
                                .mapx(tourItem.getMapx())
                                .mapy(tourItem.getMapy())
                                .mlevel(tourItem.getMlevel())
                                .sigungucode(tourItem.getSigungucode())
                                .tel(tourItem.getTel())
                                .title(tourItem.getTitle())
                                .build()
                        );

        return tourItemResponseList;
    }
}

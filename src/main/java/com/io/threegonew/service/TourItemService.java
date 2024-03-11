package com.io.threegonew.service;

import com.io.threegonew.domain.*;
import com.io.threegonew.dto.*;
import com.io.threegonew.repository.*;
import com.io.threegonew.repository.spec.TourItemSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final BookmarkRepository bookmarkRepository;
    private final ModelMapper modelMapper;

    public TourItemResponse findTourItem(String contentid){
        TourItem selectedTourItem = tourItemRepository.findById(contentid)
                .orElseThrow(()-> new IllegalArgumentException("not found : touritem"));

        return tourItemMapper(selectedTourItem);
    }
    public List<Cat1Response> findCat1List(){
        List<Cat1Response> cat1List = cat1Repository.findAll().stream()
                .map(cat1 -> modelMapper.map(cat1, Cat1Response.class))
                .collect(Collectors.toList());

        return cat1List;
    }

    public List<Cat2Response> findCat2List(String cat1Code){
        Cat1 cat1 = cat1Repository.findById(cat1Code)
                .orElseThrow(()-> new IllegalArgumentException("not found : cat1"));

        List<Cat2Response> cat2List = cat1.getCat2List().stream()
                .map(cat2 -> modelMapper.map(cat2, Cat2Response.class))
                .collect(Collectors.toList());

        return cat2List;
    }

    public List<Cat3Response> findCat3List(String cat2Code){
        Cat2 cat2 = cat2Repository.findById(cat2Code)
                .orElseThrow(()-> new IllegalArgumentException("not found : cat2"));

        List<Cat3Response> cat3List = cat2.getCat3List().stream()
                .map(cat3 -> modelMapper.map(cat3, Cat3Response.class))
                .collect(Collectors.toList());

        return cat3List;
    }

    public List<SigunguResponse> findSigunguList(Integer areaCode){
        List<SigunguResponse> sigunguList = sigunguRepository.findByAreaCode(areaCode).stream()
                .map(sigungu -> modelMapper.map(sigungu, SigunguResponse.class))
                .collect(Collectors.toList());
        return sigunguList;
    }

    public List<ContentTypeResponse> findContentTypeList(){
        List<ContentTypeResponse> contentTypeList = contentTypeRepository.findAll().stream()
                .map(contentType -> modelMapper.map(contentType, ContentTypeResponse.class))
                .collect(Collectors.toList());
        return contentTypeList;
    }

    public List<AreaResponse> findAreaList(){
        List<AreaResponse> areaList = areaRepository.findAll().stream()
                .map(area -> modelMapper.map(area, AreaResponse.class))
                .collect(Collectors.toList());
        return areaList;
    }

    public AreaResponse findArea(Integer areaCode){
        Area area = areaRepository.findById(areaCode)
                .orElseThrow(()-> new IllegalArgumentException("not found : Area"));

        return new AreaResponse(area);
    }

    @Transactional(readOnly = true)
    public PageResponse findSelectedTourItemList(TourItemSelectRequest request){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Specification<TourItem> spec = (root, query, criteriaBuilder) -> null;

        if(request.getAreaCode() != null && !request.getAreaCode().isEmpty()){
            spec = spec.and(TourItemSpecification.equalAreacode(request.getAreaCode()));
        }
        if(request.getSigunguCode() != null && !request.getSigunguCode().isEmpty()){
            spec = spec.and(TourItemSpecification.equalSigungucode(request.getSigunguCode()));
        }
        if(request.getCat1() != null && !request.getCat1().isEmpty()){
            spec = spec.and(TourItemSpecification.equalCat1(request.getCat1()));
        }
        if(request.getCat2() != null && !request.getCat2().isEmpty()){
            spec = spec.and(TourItemSpecification.equalCat2(request.getCat2()));
        }
        if(request.getCat3() != null && !request.getCat3().isEmpty()){
            spec = spec.and(TourItemSpecification.equalCat3(request.getCat3()));
        }
        if(request.getContentTypeId() != null && !request.getContentTypeId().isEmpty()){
            spec = spec.and(TourItemSpecification.equalContenttypeid(request.getContentTypeId()));
        }

        Page<TourItemResponse> page = tourItemRepository.findAll(spec, pageable)
                .map(this::tourItemMapper);

        PageResponse<TourItemResponse> pageResponse = PageResponse.<TourItemResponse>withAll()
                .dtoList(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .total(page.getTotalElements())
                .build();

        return pageResponse;
    }



    /* TourItemResponse 매핑을 위한 메소드*/

    private TourItemResponse tourItemMapper(TourItem tourItem){
        return TourItemResponse.builder()
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

package com.io.threegonew.service;

import com.io.threegonew.domain.*;
import com.io.threegonew.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TourItemService {
    private final Cat1Repository cat1Repository;
    private final Cat2Repository cat2Repository;
    private final Cat3Repository cat3Repository;
    private final SigunguRepository sigunguRepository;
    private final ContentTypeRepository contentTypeRepository;
    private final AreaRepository areaRepository;
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
}

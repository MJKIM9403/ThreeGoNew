package com.io.threegonew.repository.spec;

import com.io.threegonew.domain.TourItem;
import org.springframework.data.jpa.domain.Specification;

public class TourItemSpecification {

    // 키워드 추가 시작
    public static Specification<TourItem> equalKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + keyword + "%");
    }
    // 키워드 추가 끝

    public static Specification<TourItem> equalCat1(String cat1){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cat1"), cat1);
    }
    public static Specification<TourItem> equalCat2(String cat2){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cat2"), cat2);
    }
    public static Specification<TourItem> equalCat3(String cat3){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cat3"), cat3);
    }

    public static Specification<TourItem> equalAreacode(String areacode){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("areacode"), areacode);
    }
    public static Specification<TourItem> equalSigungucode(String sigungucode){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("sigungucode"), sigungucode);
    }

    public static Specification<TourItem> equalContenttypeid(String contenttypeid){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("contenttypeid"), contenttypeid);
    }

}

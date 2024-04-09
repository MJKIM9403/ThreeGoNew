package com.io.threegonew.repository.spec;

import com.io.threegonew.domain.TourItem;
import org.springframework.data.jpa.domain.Specification;

public class TourItemSpecification {


//    public static Specification<TourItem> equalUser(String userId) {
//        return (root, query, criteriaBuilder) -> {
//            // TourItem과 Bookmark를 조인합니다.
//            Join<TourItem, Bookmark> tourItemBookmarkJoin = root.join("bookmarkList", JoinType.INNER);
//            // Bookmark와 User를 조인합니다.
//            Join<Bookmark, User> bookmarkUserJoin = tourItemBookmarkJoin.join("user", JoinType.INNER);
//            // User의 ID를 기준으로 필터링합니다.
//            return criteriaBuilder.equal(bookmarkUserJoin.get("id"), userId);
//        };
//    }


    // 조인을 굳이..할 필요가 있나용?
    public static Specification<TourItem> equalUser(String userId) {
        return (root, query, criteriaBuilder) -> {
            // TourItem과 Bookmark를 조인하지 않고 사용자 ID를 기준으로 필터링합니다.
            return criteriaBuilder.equal(root.get("bookmarkList").get("user").get("id"), userId);
        };
    }

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

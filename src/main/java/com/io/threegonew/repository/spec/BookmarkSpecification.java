package com.io.threegonew.repository.spec;

import com.io.threegonew.domain.Bookmark;
import org.springframework.data.jpa.domain.Specification;

public class BookmarkSpecification {
    // 어떻게 써야할지 몰라서...일단 적어둠
    public static Specification<Bookmark> equalUser(String userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), userId);
    }
}

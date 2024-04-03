package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
@Table(name = "like",
        uniqueConstraints={
        @UniqueConstraint(
                name="contstraintName",
                columnNames={"unique_one", "unique_two"} // DB 상의 column name 을 작성해야한다. (변수명X)
        )}
)
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long likeId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "review_id")
    private Long reviewId;

    @CreatedDate
    @Column(name = "reg_date")
    private LocalDateTime regDate;
}

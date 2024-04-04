package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "likes",
        uniqueConstraints={
        @UniqueConstraint(
                name="like_un",
                columnNames={"user_id", "review_id"}
        )}
)
public class Likes {
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

    @Builder
    public Likes(String userId, Long reviewId) {
        this.userId = userId;
        this.reviewId = reviewId;
    }
}

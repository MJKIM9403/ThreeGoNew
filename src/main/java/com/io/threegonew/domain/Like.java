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
@Table(name = "like",
        uniqueConstraints={
        @UniqueConstraint(
                name="like_un",
                columnNames={"user_id", "review_id"}
        )}
)
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @CreatedDate
    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Builder
    public Like(User user, Review review) {
        this.user = user;
        this.review = review;
    }
}

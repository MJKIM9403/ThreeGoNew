package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
@Table(name = "review")
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, targetEntity = ReviewBook.class)
    @JoinColumn(name = "book_id")
    private ReviewBook reviewBook;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = TourItem.class)
    @JoinColumn(name = "touritem_id")
    private TourItem tourItem;

    @Column(name = "touritem_title", nullable = false)
    private String tourItemTitle;

    @Column(name = "review_content", nullable = false)
    private String reviewContent;

    @Column(name = "view_count")
    @ColumnDefault("0")
    private Long viewCount;

    @OneToMany(
            mappedBy = "review",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<ReviewPhoto> reviewPhotoList = new ArrayList<>();

    @Builder
    public Review(ReviewBook reviewBook, User user, TourItem tourItem, String tourItemTitle, String reviewContent) {
        this.reviewBook = reviewBook;
        this.user = user;
        this.tourItem = tourItem;
        this.tourItemTitle = tourItemTitle;
        this.reviewContent = reviewContent;
    }

    @PrePersist
    public void prePersist() {
        this.viewCount = this.viewCount == null ? 0 : this.viewCount;
    }

    public void addPhoto(ReviewPhoto photo){
        this.reviewPhotoList.add(photo);
    }

    public Long viewCountUp(){
        this.viewCount += 1;
        return viewCount;
    }
}

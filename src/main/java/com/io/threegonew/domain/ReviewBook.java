package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "review_book")
public class ReviewBook extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Planner.class)
    @JoinColumn(name = "planner_id", nullable = false)
    private Planner planner;

    @Column(name = "book_title", nullable = false)
    private String bookTitle;

    @Column(name = "book_content")
    private String bookContent;

    @Column(name = "cover_o_file")
    private String coverOfile;

    @Column(name = "cover_file_path")
    private String coverFilePath;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "reviewBook",
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    private List<Review> reviewList = new ArrayList<>();

    @Builder
    public ReviewBook(User user, Planner planner, String bookTitle, String bookContent, String coverOfile, String coverFilePath, List<Review> reviewList) {
        this.user = user;
        this.planner = planner;
        this.bookTitle = bookTitle;
        this.bookContent = bookContent;
        this.coverOfile = coverOfile;
        this.coverFilePath = coverFilePath;
        this.reviewList = reviewList;
    }

    public void update(Planner planner, String bookTitle, String bookContent){
        this.planner = planner;
        this.bookTitle = bookTitle;
        this.bookContent = bookContent;
    }

    public void updateCover(String coverOfile, String coverFilePath){
        this.coverOfile = coverOfile;
        this.coverFilePath = coverFilePath;
    }
}

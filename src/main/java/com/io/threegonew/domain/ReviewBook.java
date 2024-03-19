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

    @Column(name = "book_title", nullable = false)
    private String bookTitle;

    @Column(name = "book_content", nullable = false)
    private String bookContent;

    @Column(name = "cover_o_file")
    private String coverOfile;

    @Column(name = "cover_file_path")
    private String coverFilePath;

    @OneToMany(
            mappedBy = "reviewBook",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<Review> reviewList = new ArrayList<>();

    @Builder
    public ReviewBook(User user, String bookTitle, String bookContent, String coverOfile, String coverFilePath, List<Review> reviewList) {
        this.user = user;
        this.bookTitle = bookTitle;
        this.bookContent = bookContent;
        this.coverOfile = coverOfile;
        this.coverFilePath = coverFilePath;
        this.reviewList = reviewList;
    }
}

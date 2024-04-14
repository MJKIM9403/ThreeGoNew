package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="comment")
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @Column(name = "cmt_content")
    private String content;

    @Column(name = "cmt_group")
    private Integer group;

    @Column(name = "cmt_order")
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    @Column(name = "cmt_del", columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    private Boolean cmtDel;

    @Builder
    public Comment(Long reviewId, User writer, String content, Integer group, Integer order) {
        this.reviewId = reviewId;
        this.writer = writer;
        this.content = content;
        this.group = group;
        this.order = order;
    }

    @PrePersist
    public void prePersist(){
        this.cmtDel = this.cmtDel == null ? false : this.cmtDel;
    }

    public void setParent(Comment parent) {
        if(this.parent == null){
            this.parent = parent;
        }
        if(this.parent != null){
            addChildComment(parent, this);
        }
    }

    private void addChildComment(Comment ancestor, Comment child) {
        ancestor.children.add(child);
        if(ancestor.parent != null){
            addChildComment(ancestor.parent, child);
        }
    }

    public void deleteComment() {
        this.cmtDel = true;
    }

    public void updateComment(String content){
        this.content = content;
    }
}

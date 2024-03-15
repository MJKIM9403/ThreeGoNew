package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id", updatable = false)
    private Long planId;

    @Column(name = "u_id")
    private String userId;

    @Column(name = "p_id", nullable = false)
    private Long plannerId;

    @Column(name = "plan_day", nullable = false)
    private Integer day;

    @Column(name = "plan_order", nullable = false)
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "content_id")
    private TourItem tourItem;

    @Builder
    public Plan(Long plannerId, String userId, Integer day, Integer order, TourItem tourItem) {
        this.plannerId = plannerId;
        this.userId = userId;
        this.day = day;
        this.order = order;
        this.tourItem = tourItem;
    }

    public void update(Integer day, Integer order) {
        this.day = day;
        this.order = order;
    }
}

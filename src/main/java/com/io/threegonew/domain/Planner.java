package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.*;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor
public class Planner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id", updatable = false)
    private Long plannerId;

    @Column(name = "u_id")
    private String userId;

    @Column(name = "p_name", nullable = false)
    private String plannerName;

    @Column(name = "start_date", nullable = false)
//    @Temporal(TemporalType.DATE)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
//    @Temporal(TemporalType.DATE)
    private LocalDate endDate;

    @Column(name = "p_del", nullable = false)
    private Boolean plannerDelete;

    @OneToMany(mappedBy = "planner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Team> sharedUsers = new ArrayList<>();


    @Builder
    public Planner(String userId, String plannerName, LocalDate startDate, LocalDate endDate, Boolean plannerDelete) {
        this.userId = userId;
        this.plannerName = plannerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.plannerDelete = plannerDelete;
    }

    public void update(String plannerName) {
        this.plannerName = plannerName;
    }

    public void updateDelete() {
        this.plannerDelete = true;
    }
}

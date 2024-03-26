package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @OneToMany(mappedBy = "planner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Team> sharedUsers = new HashSet<>();

    @Builder
    public Planner(String userId, String plannerName, Date startDate, Date endDate) {
        this.userId = userId;
        this.plannerName = plannerName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void update(String plannerName) {
        this.plannerName = plannerName;
    }
}

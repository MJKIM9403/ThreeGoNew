package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

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

    @Column(name = "j_areacode")
    private Integer areaCode;

    @Column(name = "p_name", nullable = false)
    private String plannerName;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Builder
    public Planner(String userId, Integer areaCode, String plannerName, Date startDate, Date endDate) {
        this.areaCode = areaCode;
        this.userId = userId;
        this.plannerName = plannerName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void update(String plannerName) {
        this.plannerName = plannerName;
    }
}

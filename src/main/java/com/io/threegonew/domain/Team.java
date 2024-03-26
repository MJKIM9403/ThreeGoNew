package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "t_id", updatable = false)
    private Long teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_id")
    private Planner planner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "u_id")
    private User user;

    private Integer teamLevel;

    public Team(Planner planner, User user, Integer teamLevel) {
        this.planner = planner;
        this.user = user;
        this.teamLevel = teamLevel;
    }
}

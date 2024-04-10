package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
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

    @Column(name = "t_del", nullable = false)
    private Boolean teamDelete;

    @Builder
    public Team(Planner planner, User user, Integer teamLevel, Boolean teamDelete) {
        this.planner = planner;
        this.user = user;
        this.teamLevel = teamLevel;
        this.teamDelete = teamDelete;
    }

    public void updateDelete() {
        this.teamDelete = true;
    }

}

package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_cat3")
public class Cat3 {
    @Id
    @Column(name = "cat3")
    private String cat3;
    @Column(name = "cat3_name")
    private String cat3Name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat2")
    private Cat2 cat2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat1")
    private Cat1 cat1;
}

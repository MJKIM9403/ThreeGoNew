package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_cat2")
public class Cat2 {
    @Id
    @Column(name = "cat2")
    private String cat2;
    @Column(name = "cat2_name")
    private String cat2Name;

    @ManyToOne
    @JoinColumn(name = "cat1")
    private Cat1 cat1;

    @OneToMany(mappedBy = "cat2")
    private List<Cat3> cat3List = new ArrayList<>();
}

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
@Table(name = "t_cat1")
public class Cat1 {
    @Id
    @Column(name = "cat1")
    private String cat1;
    @Column(name = "cat1_name")
    private String cat1Name;

    @OneToMany(mappedBy = "cat1")
    private List<Cat2> cat2List = new ArrayList<>();
}

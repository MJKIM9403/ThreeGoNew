package com.io.threegonew.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "t_cat2")
public class Cat2 {
    @Id
    @Column(name = "cat2")
    private String cat2;
    @Column(name = "cat1")
    private String cat1;
    @Column(name = "cat2_name")
    private String cat2_name;
}

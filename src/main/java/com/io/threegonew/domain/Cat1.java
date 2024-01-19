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
@Table(name = "t_cat1")
public class Cat1 {
    @Id
    @Column(name = "cat1")
    private String cat1;
    @Column(name = "cat1_name")
    private String cat1_name;
}

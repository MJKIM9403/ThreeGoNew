package com.io.threegonew.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Column(name = "cat1")
    private String cat1;
    @Column(name = "cat2")
    private String cat2;
    @Column(name = "cat3_name")
    private String cat3Name;
}

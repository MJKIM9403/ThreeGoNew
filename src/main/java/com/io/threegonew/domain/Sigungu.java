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
@Table(name = "t_sigungu")
public class Sigungu {
    @Id
    @Column(name = "s_sigungucode")
    private int s_sigungucode;
    @Column(name = "s_areacode")
    private int s_areacode;
    @Column(name = "sigungu_name")
    private String sigungu_name;
}

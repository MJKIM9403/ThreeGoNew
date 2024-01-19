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
@Table(name = "t_area")
public class Area {
    @Id
    @Column(name = "j_areacode")
    private int j_areacode;
    @Column(name = "j_area_name")
    private String j_area_name;
}

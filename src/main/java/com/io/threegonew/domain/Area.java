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
@Table(name = "t_area")
public class Area {
    @Id
    @Column(name = "j_areacode")
    private int areaCode;
    @Column(name = "j_area_name")
    private String areaName;
}

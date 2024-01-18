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
@Table(name = "touritem")
public class TourItem {
    @Id
    @Column(name = "contentid")
    private String contentid;
    @Column(name = "cat1")
    private String cat1;
    @Column(name = "cat2")
    private String cat2;
    @Column(name = "cat3")
    private String cat3;
    @Column(name = "areacode")
    private String areacode;
    @Column(name = "contenttypeid")
    private String contenttypeid;
    @Column(name = "addr1")
    private String addr1;
    @Column(name = "addr2")
    private String addr2;
    @Column(name = "firstimage")
    private String firstimage;
    @Column(name = "mapx")
    private String mapx;
    @Column(name = "mapy")
    private String mapy;
    @Column(name = "mlevel")
    private String mlevel;
    @Column(name = "sigungucode")
    private String sigungucode;
    @Column(name = "tel")
    private String tel;
    @Column(name = "title")
    private String title;
}

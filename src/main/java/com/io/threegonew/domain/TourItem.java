package com.io.threegonew.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"bookmarkList"})
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

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "tourItem"
    )
    private List<Bookmark> bookmarkList = new ArrayList<>();

    public void updateEntityFromApiResponse(TourItem newTourItem){
        if(this.equals(newTourItem)){
            this.cat1 = newTourItem.cat1;
            this.cat2 = newTourItem.cat2;
            this.cat3 = newTourItem.cat3;
            this.areacode = newTourItem.areacode;
            this.contenttypeid = newTourItem.contenttypeid;
            this.addr1 = newTourItem.addr1;
            this.addr2 = newTourItem.addr2;
            this.firstimage = newTourItem.firstimage;
            this.mapx = newTourItem.mapx;
            this.mapy = newTourItem.mapy;
            this.mlevel = newTourItem.mlevel;
            this.sigungucode = newTourItem.sigungucode;
            this.tel = newTourItem.tel;
            this.title = newTourItem.title;
        }
    }
}

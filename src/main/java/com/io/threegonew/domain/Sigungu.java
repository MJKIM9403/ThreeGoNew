package com.io.threegonew.domain;

import com.io.threegonew.domain.pk.SigunguPk;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_sigungu")
@EqualsAndHashCode
@IdClass(SigunguPk.class)
public class Sigungu {
    @Id
    @Column(name = "s_sigungucode")
    private Integer sigunguCode;
    @Id
    @Column(name = "s_areacode")
    private Integer areaCode;
    @Column(name = "sigungu_name")
    private String sigunguName;

    public void updateEntityFromApiResponse(Sigungu newSigungu){
        if(this.equals(newSigungu)){
            this.sigunguCode = newSigungu.sigunguCode;
            this.areaCode = newSigungu.areaCode;
            this.sigunguName = newSigungu.sigunguName;
        }
    }
}

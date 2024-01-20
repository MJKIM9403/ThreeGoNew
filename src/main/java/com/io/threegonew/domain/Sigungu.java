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
public class Sigungu {
    @EmbeddedId
    private SigunguPk sigunguPk;
    @Column(name = "sigungu_name")
    private String sigunguName;
}

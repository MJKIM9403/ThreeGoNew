package com.io.threegonew.domain.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SigunguPk implements Serializable {
    @Column(name = "s_sigungucode")
    private int sigunguCode;
    @Column(name = "s_areacode")
    private int areaCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SigunguPk sigunguPk = (SigunguPk) o;
        return sigunguCode == sigunguPk.sigunguCode && areaCode == sigunguPk.areaCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sigunguCode, areaCode);
    }

}

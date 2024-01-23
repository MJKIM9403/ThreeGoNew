package com.io.threegonew.repository;

import com.io.threegonew.domain.Sigungu;
import com.io.threegonew.domain.pk.SigunguPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SigunguRepository extends JpaRepository<Sigungu, SigunguPk> {
    List<Sigungu> findByAreaCode(Integer areaCode);
}

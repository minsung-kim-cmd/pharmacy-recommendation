package com.project.pharmacyrecommendation.direction.repository;

import com.project.pharmacyrecommendation.direction.entity.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectionRepository extends JpaRepository<Direction, Long> {
}

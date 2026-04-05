package com.simbiosys.repository;

import com.simbiosys.model.entity.Prediction;
import com.simbiosys.model.enums.VulnerabilityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    Optional<Prediction> findByRegionId(Long regionId);

    List<Prediction> findByGrowthTrend(VulnerabilityLevel growthTrend);

    List<Prediction> findByIncreaseProbabilityGreaterThan(Double threshold);
}

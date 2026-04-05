package com.simbiosys.repository;

import com.simbiosys.model.entity.Region;
import com.simbiosys.model.enums.VulnerabilityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findByNameIgnoreCase(String name);

    List<Region> findByVulnerabilityLevel(VulnerabilityLevel level);

    List<Region> findBySubprefectureIgnoreCase(String subprefecture);

    @Query("""
            SELECT r FROM Region r
            WHERE (:vulnerabilityLevel IS NULL OR r.vulnerabilityLevel = :vulnerabilityLevel)
            AND (:subprefecture IS NULL OR LOWER(r.subprefecture) = LOWER(:subprefecture))
            ORDER BY r.totalPopulation DESC
            """)
    List<Region> findByFilters(
            @Param("vulnerabilityLevel") VulnerabilityLevel vulnerabilityLevel,
            @Param("subprefecture") String subprefecture
    );

    @Query("SELECT r FROM Region r ORDER BY r.totalPopulation DESC")
    List<Region> findAllOrderByPopulationDesc();
}

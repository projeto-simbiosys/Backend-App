package com.simbiosys.repository;

import com.simbiosys.model.entity.Person;
import com.simbiosys.model.enums.AgeRange;
import com.simbiosys.model.enums.Gender;
import com.simbiosys.model.enums.TimeOnStreet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByRegionId(Long regionId);

    long countByRegionId(Long regionId);

    long countByRegionIdAndOnStreetTrue(Long regionId);

    @Query("""
            SELECT p FROM Person p
            WHERE p.region.id = :regionId
            AND (:gender IS NULL OR p.gender = :gender)
            AND (:timeOnStreet IS NULL OR p.timeOnStreet = :timeOnStreet)
            AND (
                :ageRange IS NULL
                OR (:ageRange = 'EIGHTEEN_TO_THIRTY' AND p.age BETWEEN 18 AND 30)
                OR (:ageRange = 'THIRTY_TO_FIFTY' AND p.age BETWEEN 30 AND 50)
                OR (:ageRange = 'ABOVE_FIFTY' AND p.age > 50)
                OR (:ageRange = 'ALL')
            )
            """)
    List<Person> findByRegionAndFilters(
            @Param("regionId") Long regionId,
            @Param("gender") Gender gender,
            @Param("timeOnStreet") TimeOnStreet timeOnStreet,
            @Param("ageRange") String ageRange
    );

    @Query("""
            SELECT p FROM Person p
            WHERE (:gender IS NULL OR p.gender = :gender)
            AND (:timeOnStreet IS NULL OR p.timeOnStreet = :timeOnStreet)
            """)
    List<Person> findByGlobalFilters(
            @Param("gender") Gender gender,
            @Param("timeOnStreet") TimeOnStreet timeOnStreet
    );
}

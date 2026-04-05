package com.simbiosys.model.entity;

import com.simbiosys.model.enums.VulnerabilityLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "regions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String subprefecture;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(name = "total_population")
    private Integer totalPopulation;

    @Column(name = "average_age")
    private Double averageAge;

    @Column(name = "unemployment_rate")
    private Double unemploymentRate;

    @Column(name = "average_time_on_street_years")
    private Double averageTimeOnStreetYears;

    @Column(name = "male_percentage")
    private Double malePercentage;

    @Column(name = "female_percentage")
    private Double femalePercentage;

    @Column(name = "shelter_capacity")
    private Integer shelterCapacity;

    @Column(name = "sheltered_count")
    private Integer shelteredCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "vulnerability_level")
    private VulnerabilityLevel vulnerabilityLevel;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Person> persons;

    @OneToOne(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Prediction prediction;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

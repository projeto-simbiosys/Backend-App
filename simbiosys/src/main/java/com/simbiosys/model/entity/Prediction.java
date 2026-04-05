package com.simbiosys.model.entity;

import com.simbiosys.model.enums.VulnerabilityLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "predictions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false, unique = true)
    private Region region;

    @Column(name = "increase_probability")
    private Double increaseProbability;

    @Enumerated(EnumType.STRING)
    @Column(name = "growth_trend")
    private VulnerabilityLevel growthTrend;

    @Column(name = "ai_justification", length = 2000)
    private String aiJustification;

    @Column(name = "key_factors", length = 1000)
    private String keyFactors;

    @Column(name = "projected_population_6m")
    private Integer projectedPopulation6Months;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @PrePersist
    @PreUpdate
    protected void onGenerate() {
        generatedAt = LocalDateTime.now();
    }
}

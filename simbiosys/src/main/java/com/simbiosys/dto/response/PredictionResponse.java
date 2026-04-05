package com.simbiosys.dto.response;

import com.simbiosys.model.enums.VulnerabilityLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PredictionResponse {

    private Long id;
    private Long regionId;
    private String regionName;

    // Probabilidade de aumento (0-100%)
    private Double increaseProbability;

    // Tendência de crescimento: HIGH, MEDIUM, LOW
    private VulnerabilityLevel growthTrend;

    // Justificativa automática da IA
    private String aiJustification;

    // Fatores chave que influenciaram a previsão
    private String keyFactors;

    // Projeção para 6 meses
    private Integer projectedPopulation6Months;

    private LocalDateTime generatedAt;
}

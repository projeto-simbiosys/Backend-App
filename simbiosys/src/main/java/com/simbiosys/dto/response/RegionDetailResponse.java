package com.simbiosys.dto.response;

import com.simbiosys.model.enums.VulnerabilityLevel;
import lombok.Builder;
import lombok.Data;

// DTO completo para tela de "Detalhes da Região"
@Data
@Builder
public class RegionDetailResponse {

    private Long id;
    private String name;
    private String subprefecture;
    private Double latitude;
    private Double longitude;

    // Estatísticas exibidas na tela do Figma
    private Integer totalPopulation;
    private Double averageAge;
    private Double unemploymentRate;
    private Double averageTimeOnStreetYears;
    private Double malePercentage;
    private Double femalePercentage;
    private Integer shelterCapacity;
    private Integer shelteredCount;

    private VulnerabilityLevel vulnerabilityLevel;

    // Análise textual da região (gerada dinamicamente)
    private String regionAnalysis;

    // Previsão de IA (embutida no detalhe da região)
    private PredictionResponse prediction;
}

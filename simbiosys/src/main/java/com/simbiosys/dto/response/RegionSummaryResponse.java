package com.simbiosys.dto.response;

import com.simbiosys.model.enums.VulnerabilityLevel;
import lombok.Builder;
import lombok.Data;

// DTO resumido para listagem no mapa
@Data
@Builder
public class RegionSummaryResponse {

    private Long id;
    private String name;
    private String subprefecture;
    private Double latitude;
    private Double longitude;
    private Integer totalPopulation;
    private Double unemploymentRate;
    private Double averageTimeOnStreetYears;
    private VulnerabilityLevel vulnerabilityLevel;
}

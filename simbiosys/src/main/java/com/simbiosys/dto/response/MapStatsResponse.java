package com.simbiosys.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

// Resposta do mapa com estatísticas globais + lista de regiões
@Data
@Builder
public class MapStatsResponse {

    private Integer totalStreetPopulation;
    private Integer totalRegions;
    private Integer highVulnerabilityRegions;
    private Double averageUnemploymentRate;

    private List<RegionSummaryResponse> regions;
}

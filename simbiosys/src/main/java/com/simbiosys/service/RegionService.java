package com.simbiosys.service;

import com.simbiosys.dto.request.RegionFilterRequest;
import com.simbiosys.dto.response.*;
import com.simbiosys.exception.ResourceNotFoundException;
import com.simbiosys.model.entity.Region;
import com.simbiosys.model.enums.VulnerabilityLevel;
import com.simbiosys.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;
    private final PredictionService predictionService;

    public MapStatsResponse getMapStats() {
        List<Region> regions = regionRepository.findAllOrderByPopulationDesc();

        int totalPopulation = regions.stream().mapToInt(r -> r.getTotalPopulation() != null ? r.getTotalPopulation() : 0).sum();
        long highVulnerability = regions.stream().filter(r -> VulnerabilityLevel.HIGH.equals(r.getVulnerabilityLevel())).count();
        double avgUnemployment = regions.stream()
                .filter(r -> r.getUnemploymentRate() != null)
                .mapToDouble(Region::getUnemploymentRate)
                .average().orElse(0.0);

        List<RegionSummaryResponse> summaries = regions.stream()
                .map(this::toSummary)
                .collect(Collectors.toList());

        return MapStatsResponse.builder()
                .totalStreetPopulation(totalPopulation)
                .totalRegions(regions.size())
                .highVulnerabilityRegions((int) highVulnerability)
                .averageUnemploymentRate(Math.round(avgUnemployment * 10.0) / 10.0)
                .regions(summaries)
                .build();
    }

    public List<RegionSummaryResponse> getRegions(RegionFilterRequest filter) {
        List<Region> regions = regionRepository.findByFilters(
                filter.getVulnerabilityLevel(),
                filter.getSubprefecture()
        );
        return regions.stream().map(this::toSummary).collect(Collectors.toList());
    }

    public RegionDetailResponse getRegionDetail(Long id) {
        Region region = findById(id);

        PredictionResponse prediction = null;
        try {
            prediction = predictionService.getPredictionByRegion(id);
        } catch (ResourceNotFoundException ignored) {}

        String analysis = buildAnalysisText(region);

        return RegionDetailResponse.builder()
                .id(region.getId())
                .name(region.getName())
                .subprefecture(region.getSubprefecture())
                .latitude(region.getLatitude())
                .longitude(region.getLongitude())
                .totalPopulation(region.getTotalPopulation())
                .averageAge(region.getAverageAge())
                .unemploymentRate(region.getUnemploymentRate())
                .averageTimeOnStreetYears(region.getAverageTimeOnStreetYears())
                .malePercentage(region.getMalePercentage())
                .femalePercentage(region.getFemalePercentage())
                .shelterCapacity(region.getShelterCapacity())
                .shelteredCount(region.getShelteredCount())
                .vulnerabilityLevel(region.getVulnerabilityLevel())
                .regionAnalysis(analysis)
                .prediction(prediction)
                .build();
    }

    private String buildAnalysisText(Region r) {
        return String.format(
                "%d pessoas em situação de rua nesta região. A idade média é de %.0f anos, " +
                "com tempo médio de %.1f anos nas ruas. A taxa de desemprego de %.0f%% " +
                "indica uma situação que requer atenção especial.",
                r.getTotalPopulation() != null ? r.getTotalPopulation() : 0,
                r.getAverageAge() != null ? r.getAverageAge() : 0.0,
                r.getAverageTimeOnStreetYears() != null ? r.getAverageTimeOnStreetYears() : 0.0,
                r.getUnemploymentRate() != null ? r.getUnemploymentRate() : 0.0
        );
    }

    public Region findById(Long id) {
        return regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Região não encontrada com id: " + id));
    }

    private RegionSummaryResponse toSummary(Region r) {
        return RegionSummaryResponse.builder()
                .id(r.getId())
                .name(r.getName())
                .subprefecture(r.getSubprefecture())
                .latitude(r.getLatitude())
                .longitude(r.getLongitude())
                .totalPopulation(r.getTotalPopulation())
                .unemploymentRate(r.getUnemploymentRate())
                .averageTimeOnStreetYears(r.getAverageTimeOnStreetYears())
                .vulnerabilityLevel(r.getVulnerabilityLevel())
                .build();
    }
}

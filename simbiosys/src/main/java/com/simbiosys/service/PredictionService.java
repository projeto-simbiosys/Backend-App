package com.simbiosys.service;

import com.simbiosys.dto.response.PredictionResponse;
import com.simbiosys.exception.ResourceNotFoundException;
import com.simbiosys.model.entity.Prediction;
import com.simbiosys.model.entity.Region;
import com.simbiosys.model.enums.VulnerabilityLevel;
import com.simbiosys.repository.PredictionRepository;
import com.simbiosys.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final PredictionRepository predictionRepository;
    private final RegionRepository regionRepository;

    public PredictionResponse getPredictionByRegion(Long regionId) {
        Prediction prediction = predictionRepository.findByRegionId(regionId)
                .orElseThrow(() -> new ResourceNotFoundException("Previsão não encontrada para região id: " + regionId));
        return toResponse(prediction);
    }

    public List<PredictionResponse> getAllPredictions() {
        return predictionRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<PredictionResponse> getHighRiskPredictions() {
        return predictionRepository.findByIncreaseProbabilityGreaterThan(70.0).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Gera (ou regenera) a previsão de IA para uma região.
     * A lógica aqui é uma simulação baseada em regras.
     * Em produção, substituir pela chamada ao modelo de ML (Random Forest / Regressão).
     */
    public PredictionResponse generatePrediction(Long regionId) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new ResourceNotFoundException("Região não encontrada com id: " + regionId));

        double probability = calculateIncreaseProbability(region);
        VulnerabilityLevel trend = classifyTrend(probability);
        String justification = buildJustification(region, probability);
        String keyFactors = buildKeyFactors(region);
        int projected = calculateProjectedPopulation(region, probability);

        Prediction prediction = predictionRepository.findByRegionId(regionId)
                .orElse(Prediction.builder().region(region).build());

        prediction.setIncreaseProbability(probability);
        prediction.setGrowthTrend(trend);
        prediction.setAiJustification(justification);
        prediction.setKeyFactors(keyFactors);
        prediction.setProjectedPopulation6Months(projected);

        return toResponse(predictionRepository.save(prediction));
    }

    // --- Lógica de simulação da IA ---

    private double calculateIncreaseProbability(Region region) {
        double score = 0.0;

        // Desemprego alto → maior risco (peso 40%)
        if (region.getUnemploymentRate() != null) {
            score += (region.getUnemploymentRate() / 100.0) * 40.0;
        }

        // Tempo médio nas ruas alto → tendência de permanência (peso 30%)
        if (region.getAverageTimeOnStreetYears() != null) {
            double timeScore = Math.min(region.getAverageTimeOnStreetYears() / 5.0, 1.0) * 30.0;
            score += timeScore;
        }

        // Capacidade de abrigo insuficiente (peso 30%)
        if (region.getShelterCapacity() != null && region.getTotalPopulation() != null
                && region.getShelterCapacity() > 0) {
            double shelterRatio = (double) region.getShelteredCount() / region.getShelterCapacity();
            if (shelterRatio > 0.8) score += 30.0; // abrigos quase lotados
            else if (shelterRatio > 0.5) score += 15.0;
        } else {
            score += 20.0; // sem dados de abrigo = risco médio
        }

        return Math.min(Math.round(score * 10.0) / 10.0, 100.0);
    }

    private VulnerabilityLevel classifyTrend(double probability) {
        if (probability >= 70) return VulnerabilityLevel.HIGH;
        if (probability >= 40) return VulnerabilityLevel.MEDIUM;
        return VulnerabilityLevel.LOW;
    }

    private String buildJustification(Region region, double probability) {
        String trend = probability >= 70 ? "alta" : probability >= 40 ? "moderada" : "baixa";
        return String.format(
                "Com base nos dados históricos da região %s, o modelo identificou probabilidade de %.1f%% " +
                "de aumento da população em situação de rua nos próximos 6 meses. " +
                "A tendência de crescimento é classificada como %s, principalmente " +
                "influenciada pela taxa de desemprego de %.0f%% e tempo médio de %.1f anos nas ruas.",
                region.getName(),
                probability,
                trend,
                region.getUnemploymentRate() != null ? region.getUnemploymentRate() : 0,
                region.getAverageTimeOnStreetYears() != null ? region.getAverageTimeOnStreetYears() : 0
        );
    }

    private String buildKeyFactors(Region region) {
        StringBuilder sb = new StringBuilder();
        if (region.getUnemploymentRate() != null && region.getUnemploymentRate() > 50)
            sb.append("Alta taxa de desemprego; ");
        if (region.getAverageTimeOnStreetYears() != null && region.getAverageTimeOnStreetYears() > 3)
            sb.append("Longo tempo médio de permanência nas ruas; ");
        if (region.getShelterCapacity() != null && region.getShelteredCount() != null
                && region.getShelterCapacity() > 0
                && (double) region.getShelteredCount() / region.getShelterCapacity() > 0.8)
            sb.append("Capacidade de abrigos próxima do limite; ");
        return sb.isEmpty() ? "Dados insuficientes para análise detalhada." : sb.toString().trim();
    }

    private int calculateProjectedPopulation(Region region, double probability) {
        int current = region.getTotalPopulation() != null ? region.getTotalPopulation() : 0;
        double growthFactor = 1.0 + (probability / 100.0) * 0.3; // até 30% de aumento
        return (int) Math.round(current * growthFactor);
    }

    private PredictionResponse toResponse(Prediction p) {
        return PredictionResponse.builder()
                .id(p.getId())
                .regionId(p.getRegion().getId())
                .regionName(p.getRegion().getName())
                .increaseProbability(p.getIncreaseProbability())
                .growthTrend(p.getGrowthTrend())
                .aiJustification(p.getAiJustification())
                .keyFactors(p.getKeyFactors())
                .projectedPopulation6Months(p.getProjectedPopulation6Months())
                .generatedAt(p.getGeneratedAt())
                .build();
    }
}

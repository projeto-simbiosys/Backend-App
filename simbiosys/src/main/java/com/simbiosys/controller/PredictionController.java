package com.simbiosys.controller;

import com.simbiosys.dto.response.PredictionResponse;
import com.simbiosys.service.PredictionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Previsões de IA", description = "Endpoints de análise preditiva por região")
public class PredictionController {

    private final PredictionService predictionService;

    /**
     * GET /api/v1/regions/{regionId}/prediction
     * Retorna a previsão de IA para uma região específica.
     */
    @GetMapping("/regions/{regionId}/prediction")
    @Operation(
        summary = "Previsão de IA por região",
        description = "Retorna a probabilidade de aumento da população em situação de rua, tendência de crescimento e justificativa gerada pela IA."
    )
    public ResponseEntity<PredictionResponse> getPredictionByRegion(
            @Parameter(description = "ID da região") @PathVariable Long regionId) {
        return ResponseEntity.ok(predictionService.getPredictionByRegion(regionId));
    }

    /**
     * GET /api/v1/predictions
     * Lista todas as previsões.
     */
    @GetMapping("/predictions")
    @Operation(
        summary = "Lista todas as previsões",
        description = "Retorna as previsões de todas as regiões cadastradas."
    )
    public ResponseEntity<List<PredictionResponse>> getAllPredictions() {
        return ResponseEntity.ok(predictionService.getAllPredictions());
    }

    /**
     * GET /api/v1/predictions/high-risk
     * Lista previsões com probabilidade acima de 70% (regiões críticas).
     */
    @GetMapping("/predictions/high-risk")
    @Operation(
        summary = "Regiões de alto risco",
        description = "Retorna somente as regiões com probabilidade de aumento acima de 70%."
    )
    public ResponseEntity<List<PredictionResponse>> getHighRiskPredictions() {
        return ResponseEntity.ok(predictionService.getHighRiskPredictions());
    }

    /**
     * POST /api/v1/predictions/{regionId}/generate
     * Gera ou regenera a previsão de IA para uma região.
     */
    @PostMapping("/predictions/{regionId}/generate")
    @Operation(
        summary = "Gerar previsão de IA",
        description = "Executa o modelo preditivo para a região informada e persiste o resultado. Pode ser usado para atualizar previsões após novos dados de censo."
    )
    public ResponseEntity<PredictionResponse> generatePrediction(
            @Parameter(description = "ID da região") @PathVariable Long regionId) {
        return ResponseEntity.ok(predictionService.generatePrediction(regionId));
    }
}

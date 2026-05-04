package com.simbiosys.controller;

import com.simbiosys.dto.response.AiReportResponse;
import com.simbiosys.service.AiReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "IA Generativa", description = "Relatórios narrativos gerados pelo Gemini (Google AI Studio)")
public class AiReportController {

    private final AiReportService aiReportService;

    /**
     * POST /api/v1/regions/{regionId}/ai-report
     *
     * Gera um relatório narrativo completo para a região usando o Gemini.
     * Retorna:
     * - narrativeReport         → análise da situação em linguagem natural
     * - predictionJustification → explicação do risco de aumento
     * - actionSuggestion        → sugestões de ação para gestores
     * - urgencyLevel            → CRITICO / ALTO / MODERADO / BAIXO
     */
    @PostMapping("/regions/{regionId}/ai-report")
    @Operation(
        summary = "Gerar relatório de IA para uma região",
        description = "Usa o Gemini (Google AI Studio) para gerar análise narrativa da região " +
                      "com base nos dados do censo, justificativa do risco e sugestões de ação."
    )
    public ResponseEntity<AiReportResponse> generateReport(@PathVariable Long regionId) {
        return ResponseEntity.ok(aiReportService.generateReportForRegion(regionId));
    }
}

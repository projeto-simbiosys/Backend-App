package com.simbiosys.controller;

import com.simbiosys.dto.request.RegionFilterRequest;
import com.simbiosys.dto.response.MapStatsResponse;
import com.simbiosys.dto.response.RegionDetailResponse;
import com.simbiosys.dto.response.RegionSummaryResponse;
import com.simbiosys.model.enums.AgeRange;
import com.simbiosys.model.enums.Gender;
import com.simbiosys.model.enums.TimeOnStreet;
import com.simbiosys.model.enums.VulnerabilityLevel;
import com.simbiosys.service.RegionService;
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
@Tag(name = "Regiões", description = "Endpoints do Mapa Inteligente e detalhes por região")
public class RegionController {

    private final RegionService regionService;

    /**
     * GET /api/v1/map
     * Retorna estatísticas globais + todos os pins do mapa.
     * Usado na tela inicial "Mapa Inteligente".
     */
    @GetMapping("/map")
    @Operation(
        summary = "Estatísticas globais do mapa",
        description = "Retorna contadores globais (total de pessoas, regiões críticas) e a lista de todas as regiões para exibição no mapa."
    )
    public ResponseEntity<MapStatsResponse> getMapStats() {
        return ResponseEntity.ok(regionService.getMapStats());
    }

    /**
     * GET /api/v1/regions
     * Lista regiões com filtros opcionais.
     * Usado na tela "Filtros Avançados": Gênero, Faixa Etária, Tempo em Situação de Rua.
     */
    @GetMapping("/regions")
    @Operation(
        summary = "Lista regiões com filtros",
        description = "Retorna regiões filtradas por gênero, faixa etária, tempo em situação de rua e/ou nível de vulnerabilidade."
    )
    public ResponseEntity<List<RegionSummaryResponse>> getRegions(
            @Parameter(description = "Filtro por gênero: MALE, FEMALE, ALL")
            @RequestParam(required = false) Gender gender,

            @Parameter(description = "Faixa etária: ALL, EIGHTEEN_TO_THIRTY, THIRTY_TO_FIFTY, ABOVE_FIFTY")
            @RequestParam(required = false) AgeRange ageRange,

            @Parameter(description = "Tempo em situação de rua: LESS_THAN_ONE_YEAR, ONE_TO_THREE_YEARS, THREE_TO_FIVE_YEARS, MORE_THAN_FIVE_YEARS")
            @RequestParam(required = false) TimeOnStreet timeOnStreet,

            @Parameter(description = "Nível de vulnerabilidade: HIGH, MEDIUM, LOW")
            @RequestParam(required = false) VulnerabilityLevel vulnerabilityLevel,

            @Parameter(description = "Nome da subprefeitura")
            @RequestParam(required = false) String subprefecture
    ) {
        RegionFilterRequest filter = new RegionFilterRequest();
        filter.setGender(gender);
        filter.setAgeRange(ageRange);
        filter.setTimeOnStreet(timeOnStreet);
        filter.setVulnerabilityLevel(vulnerabilityLevel);
        filter.setSubprefecture(subprefecture);

        return ResponseEntity.ok(regionService.getRegions(filter));
    }

    /**
     * GET /api/v1/regions/{id}
     * Detalhe completo de uma região.
     * Usado na tela "Detalhes da Região" com análise e previsão embutida.
     */
    @GetMapping("/regions/{id}")
    @Operation(
        summary = "Detalhe de uma região",
        description = "Retorna dados completos da região: estatísticas, análise textual e previsão da IA."
    )
    public ResponseEntity<RegionDetailResponse> getRegionDetail(
            @Parameter(description = "ID da região") @PathVariable Long id) {
        return ResponseEntity.ok(regionService.getRegionDetail(id));
    }
}

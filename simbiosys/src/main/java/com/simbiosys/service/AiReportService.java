package com.simbiosys.service;

import com.simbiosys.dto.response.AiReportResponse;
import com.simbiosys.exception.ResourceNotFoundException;
import com.simbiosys.model.entity.Prediction;
import com.simbiosys.model.entity.Region;
import com.simbiosys.repository.PredictionRepository;
import com.simbiosys.repository.RegionRepository;
import com.simbiosys.service.ai.GeminiAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiReportService {

    private final RegionRepository regionRepository;
    private final PredictionRepository predictionRepository;
    private final GeminiAiService geminiAiService;

    public AiReportResponse generateReportForRegion(Long regionId) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new ResourceNotFoundException("Regiao nao encontrada: " + regionId));
        Prediction prediction = predictionRepository.findByRegionId(regionId).orElse(null);
        return geminiAiService.generateRegionReport(region, prediction);
    }
}

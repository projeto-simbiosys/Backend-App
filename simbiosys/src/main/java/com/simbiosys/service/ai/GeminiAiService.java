package com.simbiosys.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.simbiosys.dto.response.AiReportResponse;
import com.simbiosys.model.entity.Prediction;
import com.simbiosys.model.entity.Region;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiAiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AiReportResponse generateRegionReport(Region region, Prediction prediction) {
        log.info("Gerando relatorio com Gemini para regiao: {}", region.getName());
        String prompt = buildPrompt(region, prediction);
        String rawResponse = callGeminiApi(prompt);
        return parseResponse(rawResponse, region);
    }

    private String buildPrompt(Region region, Prediction prediction) {
        StringBuilder sb = new StringBuilder();
        sb.append("Voce e um analista de politicas publicas da Prefeitura de Sao Paulo. ")
          .append("Responda em portugues brasileiro, tecnico e acessivel para gestores.\n\n");
        sb.append("Analise os dados da regiao ").append(region.getName()).append(":\n");
        sb.append("Populacao em situacao de rua: ").append(region.getTotalPopulation()).append("\n");
        sb.append("Idade media: ").append(region.getAverageAge()).append(" anos\n");
        sb.append("Taxa de desemprego: ").append(region.getUnemploymentRate()).append("%\n");
        sb.append("Tempo medio nas ruas: ").append(region.getAverageTimeOnStreetYears()).append(" anos\n");
        sb.append("Genero: ").append(region.getMalePercentage()).append("% masc, ")
          .append(region.getFemalePercentage()).append("% fem\n");
        sb.append("Abrigos: ").append(region.getShelteredCount()).append("/")
          .append(region.getShelterCapacity()).append(" vagas\n");
        sb.append("Vulnerabilidade: ").append(region.getVulnerabilityLevel()).append("\n");

        if (prediction != null) {
            sb.append("Probabilidade de aumento: ").append(prediction.getIncreaseProbability()).append("%\n");
            sb.append("Tendencia: ").append(prediction.getGrowthTrend()).append("\n");
            sb.append("Projecao 6 meses: ").append(prediction.getProjectedPopulation6Months()).append(" pessoas\n");
        }

        sb.append("\nResponda EXCLUSIVAMENTE com JSON valido neste formato:\n");
        sb.append("{\"narrativeReport\":\"<2-3 paragrafos sobre situacao atual>\",");
        sb.append("\"predictionJustification\":\"<1 paragrafo sobre fatores de risco>\",");
        sb.append("\"actionSuggestion\":\"<1 paragrafo com sugestoes para gestores>\",");
        sb.append("\"urgencyLevel\":\"<CRITICO, ALTO, MODERADO ou BAIXO>\"}");
        return sb.toString();
    }

    private String callGeminiApi(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectNode body = objectMapper.createObjectNode();
            ArrayNode contents = objectMapper.createArrayNode();
            ObjectNode content = objectMapper.createObjectNode();
            ArrayNode parts = objectMapper.createArrayNode();
            ObjectNode part = objectMapper.createObjectNode();
            part.put("text", prompt);
            parts.add(part);
            content.set("parts", parts);
            contents.add(content);
            body.set("contents", contents);

            HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    GEMINI_API_URL + apiKey, HttpMethod.POST, request, String.class);

            JsonNode json = objectMapper.readTree(response.getBody());
            return json.path("candidates").get(0)
                       .path("content").path("parts").get(0)
                       .path("text").asText();

        } catch (Exception e) {
            log.error("Erro ao chamar API do Gemini: {}", e.getMessage());
            return "{\"narrativeReport\":\"Servico de IA indisponivel.\","
                 + "\"predictionJustification\":\"\","
                 + "\"actionSuggestion\":\"\","
                 + "\"urgencyLevel\":\"MODERADO\"}";
        }
    }

    private AiReportResponse parseResponse(String rawText, Region region) {
        try {
            String cleaned = rawText.replace("```json", "").replace("```", "").trim();
            JsonNode json = objectMapper.readTree(cleaned);
            return AiReportResponse.builder()
                    .regionId(region.getId())
                    .regionName(region.getName())
                    .narrativeReport(json.path("narrativeReport").asText())
                    .predictionJustification(json.path("predictionJustification").asText())
                    .actionSuggestion(json.path("actionSuggestion").asText())
                    .urgencyLevel(json.path("urgencyLevel").asText())
                    .generatedAt(LocalDateTime.now())
                    .build();
        } catch (Exception e) {
            log.error("Erro ao parsear resposta do Gemini: {}", e.getMessage());
            return AiReportResponse.builder()
                    .regionId(region.getId())
                    .regionName(region.getName())
                    .narrativeReport(rawText)
                    .generatedAt(LocalDateTime.now())
                    .build();
        }
    }
}

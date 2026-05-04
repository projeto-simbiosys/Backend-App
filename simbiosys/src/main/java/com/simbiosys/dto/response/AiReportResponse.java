package com.simbiosys.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AiReportResponse {
    private Long regionId;
    private String regionName;
    private String narrativeReport;
    private String predictionJustification;
    private String actionSuggestion;
    private String urgencyLevel;
    private LocalDateTime generatedAt;
}

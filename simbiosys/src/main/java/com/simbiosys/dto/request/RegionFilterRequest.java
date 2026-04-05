package com.simbiosys.dto.request;

import com.simbiosys.model.enums.AgeRange;
import com.simbiosys.model.enums.Gender;
import com.simbiosys.model.enums.TimeOnStreet;
import com.simbiosys.model.enums.VulnerabilityLevel;
import lombok.Data;

// Filtros da tela "Filtros Avançados" do Figma
@Data
public class RegionFilterRequest {

    // Filtros do mapa
    private Gender gender;           // Todos / Masculino / Feminino
    private AgeRange ageRange;       // Todos / 18-30 / 30-50 / >50
    private TimeOnStreet timeOnStreet; // Todos / <1 ano / 1-3 anos / >3 anos
    private VulnerabilityLevel vulnerabilityLevel;
    private String subprefecture;
}

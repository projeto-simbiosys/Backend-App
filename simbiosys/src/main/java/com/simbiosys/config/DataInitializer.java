package com.simbiosys.config;

import com.simbiosys.model.entity.Region;
import com.simbiosys.model.enums.VulnerabilityLevel;
import com.simbiosys.repository.RegionRepository;
import com.simbiosys.service.PredictionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(RegionRepository regionRepository, PredictionService predictionService) {
        return args -> {
            if (regionRepository.count() > 0) return;

            log.info("Populando dados iniciais de regioes de Sao Paulo...");

            List<Region> regions = List.of(
                Region.builder()
                        .name("Se")
                        .subprefecture("Se")
                        .latitude(-23.5505)
                        .longitude(-46.6333)
                        .totalPopulation(850)
                        .averageAge(37.0)
                        .unemploymentRate(64.0)
                        .averageTimeOnStreetYears(3.4)
                        .malePercentage(72.0)
                        .femalePercentage(28.0)
                        .shelterCapacity(300)
                        .shelteredCount(280)
                        .vulnerabilityLevel(VulnerabilityLevel.HIGH)
                        .build(),

                Region.builder()
                        .name("Liberdade")
                        .subprefecture("Se")
                        .latitude(-23.5588)
                        .longitude(-46.6308)
                        .totalPopulation(420)
                        .averageAge(41.0)
                        .unemploymentRate(52.0)
                        .averageTimeOnStreetYears(2.8)
                        .malePercentage(68.0)
                        .femalePercentage(32.0)
                        .shelterCapacity(200)
                        .shelteredCount(150)
                        .vulnerabilityLevel(VulnerabilityLevel.MEDIUM)
                        .build(),

                Region.builder()
                        .name("Bom Retiro")
                        .subprefecture("Se")
                        .latitude(-23.5278)
                        .longitude(-46.6381)
                        .totalPopulation(310)
                        .averageAge(39.0)
                        .unemploymentRate(58.0)
                        .averageTimeOnStreetYears(2.1)
                        .malePercentage(75.0)
                        .femalePercentage(25.0)
                        .shelterCapacity(150)
                        .shelteredCount(140)
                        .vulnerabilityLevel(VulnerabilityLevel.HIGH)
                        .build(),

                Region.builder()
                        .name("Consolacao")
                        .subprefecture("Se")
                        .latitude(-23.5479)
                        .longitude(-46.6568)
                        .totalPopulation(190)
                        .averageAge(35.0)
                        .unemploymentRate(43.0)
                        .averageTimeOnStreetYears(1.5)
                        .malePercentage(65.0)
                        .femalePercentage(35.0)
                        .shelterCapacity(100)
                        .shelteredCount(60)
                        .vulnerabilityLevel(VulnerabilityLevel.MEDIUM)
                        .build(),

                Region.builder()
                        .name("Pinheiros")
                        .subprefecture("Pinheiros")
                        .latitude(-23.5614)
                        .longitude(-46.6830)
                        .totalPopulation(120)
                        .averageAge(33.0)
                        .unemploymentRate(31.0)
                        .averageTimeOnStreetYears(1.2)
                        .malePercentage(60.0)
                        .femalePercentage(40.0)
                        .shelterCapacity(80)
                        .shelteredCount(40)
                        .vulnerabilityLevel(VulnerabilityLevel.LOW)
                        .build(),

                Region.builder()
                        .name("Penha")
                        .subprefecture("Penha")
                        .latitude(-23.5273)
                        .longitude(-46.5374)
                        .totalPopulation(280)
                        .averageAge(42.0)
                        .unemploymentRate(49.0)
                        .averageTimeOnStreetYears(2.5)
                        .malePercentage(70.0)
                        .femalePercentage(30.0)
                        .shelterCapacity(120)
                        .shelteredCount(100)
                        .vulnerabilityLevel(VulnerabilityLevel.MEDIUM)
                        .build(),

                Region.builder()
                        .name("Santo Amaro")
                        .subprefecture("Santo Amaro")
                        .latitude(-23.6531)
                        .longitude(-46.7066)
                        .totalPopulation(95)
                        .averageAge(38.0)
                        .unemploymentRate(36.0)
                        .averageTimeOnStreetYears(1.0)
                        .malePercentage(63.0)
                        .femalePercentage(37.0)
                        .shelterCapacity(60)
                        .shelteredCount(30)
                        .vulnerabilityLevel(VulnerabilityLevel.LOW)
                        .build()
            );

            regionRepository.saveAll(regions);
            log.info("{} regioes salvas.", regions.size());

            regions.forEach(r -> {
                try {
                    predictionService.generatePrediction(r.getId());
                    log.info("Previsao gerada para: {}", r.getName());
                } catch (Exception e) {
                    log.warn("Erro ao gerar previsao para {}: {}", r.getName(), e.getMessage());
                }
            });

            log.info("Dados iniciais carregados com sucesso.");
        };
    }
}

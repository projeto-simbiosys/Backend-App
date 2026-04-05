# SIMBIOSYS API

Backend Spring Boot para o projeto SIMBIOSYS — sistema de análise e previsão de vulnerabilidade social para a SMADS São Paulo.

## Stack

- Java 21
- Spring Boot 3.2
- Spring Data JPA
- H2 (dev) / PostgreSQL (produção)
- Springdoc OpenAPI (Swagger UI)
- Lombok

## Como rodar

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

## Swagger UI

```
http://localhost:8080/swagger-ui.html
```

## H2 Console (dev)

```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:simbiosysdb
User: sa  |  Password: (vazio)
```

## Endpoints

### Mapa

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/v1/map` | Stats globais + pins do mapa |
| GET | `/api/v1/regions` | Lista regiões (com filtros opcionais) |
| GET | `/api/v1/regions/{id}` | Detalhe completo de uma região |

### Filtros disponíveis em `GET /api/v1/regions`

| Parâmetro | Valores | Tela no Figma |
|-----------|---------|---------------|
| `gender` | `MALE`, `FEMALE`, `ALL` | Gênero |
| `ageRange` | `ALL`, `EIGHTEEN_TO_THIRTY`, `THIRTY_TO_FIFTY`, `ABOVE_FIFTY` | Faixa Etária |
| `timeOnStreet` | `LESS_THAN_ONE_YEAR`, `ONE_TO_THREE_YEARS`, `THREE_TO_FIVE_YEARS`, `MORE_THAN_FIVE_YEARS` | Tempo em Situação de Rua |
| `vulnerabilityLevel` | `HIGH`, `MEDIUM`, `LOW` | — |
| `subprefecture` | string | — |

### Previsões de IA

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/v1/regions/{id}/prediction` | Previsão da IA para uma região |
| GET | `/api/v1/predictions` | Todas as previsões |
| GET | `/api/v1/predictions/high-risk` | Regiões com risco > 70% |
| POST | `/api/v1/predictions/{regionId}/generate` | Gerar/regenerar previsão |

## Configuração para produção (PostgreSQL)

Em `application.yml`, descomente o bloco PostgreSQL e configure as variáveis de ambiente:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/simbiosys
    username: ${DB_USER}
    password: ${DB_PASS}
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: validate
```

## Estrutura do projeto

```
src/main/java/com/simbiosys/
├── config/
│   ├── DataInitializer.java     # Seed com regiões de SP
│   └── OpenApiConfig.java
├── controller/
│   ├── RegionController.java
│   └── PredictionController.java
├── service/
│   ├── RegionService.java
│   └── PredictionService.java   # Lógica preditiva (substituir por ML em produção)
├── repository/
│   ├── RegionRepository.java
│   ├── PersonRepository.java
│   └── PredictionRepository.java
├── model/
│   ├── entity/
│   │   ├── Region.java
│   │   ├── Person.java
│   │   └── Prediction.java
│   └── enums/
│       ├── VulnerabilityLevel.java
│       ├── Gender.java
│       ├── AgeRange.java
│       └── TimeOnStreet.java
├── dto/
│   ├── request/RegionFilterRequest.java
│   └── response/
│       ├── MapStatsResponse.java
│       ├── RegionSummaryResponse.java
│       ├── RegionDetailResponse.java
│       └── PredictionResponse.java
└── exception/
    ├── ResourceNotFoundException.java
    └── GlobalExceptionHandler.java
```

## Integração com modelo de ML

O método `PredictionService.generatePrediction()` hoje usa lógica baseada em regras (simulação).
Para integrar um modelo real de Machine Learning (Random Forest / Regressão), substitua o conteúdo desse método por uma chamada REST ao serviço Python de ML:

```java
// Exemplo de integração futura
RestTemplate restTemplate = new RestTemplate();
PredictionResponse mlResult = restTemplate.postForObject(
    "http://ml-service/predict",
    region,
    PredictionResponse.class
);
```

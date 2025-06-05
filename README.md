# GlobalSight API

A GlobalSight API fornece endpoints para gerenciar e simular eventos de desastres naturais. Ela permite que usuários se registrem, façam login e, em seguida, criem simulações de desastres, obtenham previsões para elas e visualizem dados históricos de desastres.

**Observação:** Os testes para esta API passaram com sucesso após adicionar a propriedade `python.ia.service.url` ao arquivo `application.properties`. Esta propriedade deve ser configurada para apontar para a URL correta do serviço de IA em Python.

**Observação sobre Endpoints de Documentação:** Os endpoints da Swagger UI (`/api/swagger-ui/**`) e da especificação OpenAPI (`/api/v1/api-docs/**`) são públicos e não requerem autenticação.

**Grupo** # [RM555317 - Fernando Pacheco] # [RM556814 - Guilherme Jardim]

## Autenticação JWT

Esta API utiliza JSON Web Tokens (JWT) para proteger seus endpoints. Para acessar os endpoints protegidos, os clientes devem primeiro obter um token.

**Como obter um Token:**

1.  Envie uma requisição `POST` para o endpoint `/auth/login`.
2.  No corpo da requisição, forneça um `username` e `password` válidos.
3.  Se as credenciais estiverem corretas, a API retornará um token JWT na resposta.

**Como usar o Token:**

1.  Após obter o token, inclua-o no cabeçalho `Authorization` de todas as requisições subsequentes para endpoints protegidos.
2.  Utilize o esquema `Bearer` para incluir o token.

    **Exemplo:**
    ```
    Authorization: Bearer <seu_token_jwt_aqui>
    ```

**Expiração do Token:**

Os tokens JWT possuem um tempo de expiração. Nesta API, conforme configurado em `application.properties` (`jwt.expiration.ms=3600000`), os tokens são tipicamente válidos por 1 hora (3.600.000 milissegundos). Após a expiração do token, será necessário realizar o login novamente para obter um novo token e continuar acessando os recursos protegidos.

## Endpoints de Autenticação

**Caminho Base:** `/auth`

**Controlador:** `AuthController.java`

**Tags:** Autenticação

### `POST /auth/register`

*   **Resumo:** Registra um novo usuário
*   **Descrição:** Cria um novo usuário no sistema com o papel padrão ROLE_USER.
*   **Segurança:** Público
*   **Corpo da Requisição:** `UserRegistrationDto`
    *   `username` (string, obrigatório): Nome de usuário.
    *   `password` (string, obrigatório): Senha.
    *   `email` (string, obrigatório, formato de email): Endereço de e-mail.
    *   `completeName` (string, opcional): Nome completo do usuário.
*   **Exemplo de Requisição:**
    ```json
    {
      "username": "novousuario",
      "password": "senha123",
      "email": "novousuario@example.com",
      "completeName": "Novo Usuário"
    }
    ```
*   **Respostas:**
    *   `201 Created`: Usuário registrado com sucesso.
        *   Conteúdo: `UserDetailsDto`
            *   `id` (long)
            *   `username` (string)
            *   `enabled` (boolean)
            *   `roles` (array de strings)
        *   **Exemplo de Resposta:**
            ```json
            {
              "id": 1,
              "username": "novousuario",
              "enabled": true,
              "roles": ["ROLE_USER"]
            }
            ```
    *   `400 Bad Request`: Dados de entrada inválidos ou nome de usuário já existe.

### `POST /auth/login`

*   **Resumo:** Autentica um usuário
*   **Descrição:** Autentica um usuário e retorna um token JWT se as credenciais forem válidas.
*   **Segurança:** Público
*   **Corpo da Requisição:** `AuthRequestDto`
    *   `username` (string, obrigatório): Nome de usuário.
    *   `password` (string, obrigatório): Senha.
*   **Exemplo de Requisição:**
    ```json
    {
      "username": "usuarioexistente",
      "password": "senha123"
    }
    ```
*   **Respostas:**
    *   `200 OK`: Login bem-sucedido, token JWT retornado.
        *   Conteúdo: `AuthResponseDto`
            *   `token` (string): Token JWT.
            *   `userId` (long)
            *   `username` (string)
            *   `email` (string)
            *   `roles` (array de strings)
        *   **Exemplo de Resposta:**
            ```json
            {
              "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGlzdGluZ3VzZXIiLCJpYXQiOjE2NzgzNzYxMzUsImV4cCI6MTY3ODM3OTczNX0.exampleToken",
              "userId": 2,
              "username": "usuarioexistente",
              "email": "usuarioexistente@example.com",
              "roles": ["ROLE_USER"]
            }
            ```
    *   `400 Bad Request`: Requisição de login inválida.
    *   `401 Unauthorized`: Credenciais inválidas.

## Endpoints de Histórico de Eventos de Desastre

**Caminho Base:** `/api/history`

**Controlador:** `DisasterEventHistoryController.java`

**Tags:** Histórico de Desastres

### `POST /api/history`

*   **Resumo:** Cria um novo evento histórico de desastre
*   **Descrição:** Adiciona um novo registro ao histórico de desastres.
*   **Segurança:** Requer autenticação JWT e papel `ROLE_ADMIN`.
*   **Corpo da Requisição:** `DisasterEventHistoryDto` (detalhes dos campos dependem da definição do DTO)
*   **Exemplo de Requisição:**
    ```json
    {
      "disNo": "2023-0001-PHL",
      "yearEvent": 2023,
      "seq": 1,
      "disasterGroup": "Natural",
      "disasterSubgroup": "Geophysical",
      "disasterType": "Earthquake",
      "disasterSubtype": "Ground movement",
      "disasterSubsubtype": null,
      "eventName": "Luzon Earthquake",
      "entryCriteria": "Kill >= 10",
      "iso": "PHL",
      "country": "Philippines (the)",
      "region": "Asia",
      "continent": "Asia",
      "location": "Luzon Island",
      "origin": "Fault rupture",
      "associatedDis": null,
      "associatedDis2": null,
      "ofdaResponse": true,
      "appeal": "Yes",
      "declaration": "Yes",
      "aidContribution": 1000000,
      "disMagValue": 7,
      "disMagScale": "Richter",
      "latitude": "15.8 N",
      "longitude": "120.7 E",
      "localTime": "10:30",
      "riverBasin": null,
      "coordinateX": 123.45,
      "coordinateZ": 678.90,
      "startYear": 2023,
      "startMonth": 3,
      "startDay": 15,
      "endYear": 2023,
      "endMonth": 3,
      "endDay": 15,
      "totalDeaths": 25,
      "noInjured": 100,
      "noAffected": 5000,
      "noHomeless": 1000,
      "totalAffectedCombined": 6125,
      "reconstructionCosts000Usd": 5000000,
      "insuredDamages000Usd": 1000000,
      "totalDamages000Usd": 20000000,
      "cpi": 1.2
    }
    ```

##### Exemplos Adicionais de Requisição:

**Example 1: Volcanic Eruption in Indonesia**
```json
{
  "disNo": "2024-0002-IDN",
  "yearEvent": 2024,
  "disasterGroup": "Natural",
  "disasterSubgroup": "Geophysical",
  "disasterType": "Volcanic activity",
  "disasterSubtype": "Ash fall",
  "eventName": "Mount Rinjani Eruption",
  "iso": "IDN",
  "country": "Indonesia",
  "region": "Asia",
  "continent": "Asia",
  "location": "Lombok, West Nusa Tenggara",
  "origin": "Volcanic eruption",
  "ofdaResponse": true,
  "appeal": "No",
  "declaration": "Yes",
  "aidContribution": 500000,
  "disMagValue": 4.0,
  "disMagScale": "VEI",
  "latitude": "-8.42 S",
  "longitude": "116.47 E",
  "localTime": "03:15",
  "coordinateX": -8.4200,
  "coordinateZ": 116.4700,
  "startYear": 2024,
  "startMonth": 8,
  "startDay": 20,
  "endYear": 2024,
  "endMonth": 8,
  "endDay": 22,
  "totalDeaths": 5,
  "noInjured": 50,
  "noAffected": 10000,
  "noHomeless": 2000,
  "totalAffectedCombined": 12055,
  "reconstructionCosts000Usd": 1200000,
  "insuredDamages000Usd": 300000,
  "totalDamages000Usd": 2000000,
  "cpi": 1.3
}
```

**Example 2: Hurricane in the Caribbean**
```json
{
  "disNo": "2024-0003-JAM",
  "yearEvent": 2024,
  "disasterGroup": "Natural",
  "disasterSubgroup": "Meteorological",
  "disasterType": "Storm",
  "disasterSubtype": "Tropical cyclone",
  "eventName": "Hurricane Zeta",
  "iso": "JAM",
  "country": "Jamaica",
  "region": "Caribbean",
  "continent": "Americas",
  "location": "Kingston and surrounding areas",
  "origin": "Tropical depression",
  "ofdaResponse": true,
  "appeal": "Yes",
  "declaration": "Yes",
  "aidContribution": 2500000,
  "disMagValue": 3.0,
  "disMagScale": "Saffir-Simpson",
  "latitude": "17.97 N",
  "longitude": "-76.79 W",
  "localTime": "17:00",
  "coordinateX": 17.9700,
  "coordinateZ": -76.7900,
  "startYear": 2024,
  "startMonth": 9,
  "startDay": 5,
  "endYear": 2024,
  "endMonth": 9,
  "endDay": 7,
  "totalDeaths": 12,
  "noInjured": 200,
  "noAffected": 50000,
  "noHomeless": 5000,
  "totalAffectedCombined": 55212,
  "reconstructionCosts000Usd": 15000000,
  "insuredDamages000Usd": 7000000,
  "totalDamages000Usd": 25000000,
  "cpi": 1.25
}
```

**Example 3: Wildfire in Australia**
```json
{
  "disNo": "2024-0004-AUS",
  "yearEvent": 2024,
  "disasterGroup": "Natural",
  "disasterSubgroup": "Climatological",
  "disasterType": "Wildfire",
  "disasterSubtype": "Forest fire",
  "eventName": "Blue Mountains Bushfire",
  "iso": "AUS",
  "country": "Australia",
  "region": "Oceania",
  "continent": "Oceania",
  "location": "New South Wales",
  "origin": "Undetermined (possible human activity)",
  "ofdaResponse": false,
  "appeal": "Yes",
  "declaration": "Yes",
  "aidContribution": 800000,
  "disMagValue": null,
  "disMagScale": "Area burned (ha)",
  "latitude": "-33.6 S",
  "longitude": "150.3 E",
  "localTime": "14:30",
  "coordinateX": -33.6000,
  "coordinateZ": 150.3000,
  "startYear": 2024,
  "startMonth": 11,
  "startDay": 15,
  "endYear": 2024,
  "endMonth": 12,
  "endDay": 5,
  "totalDeaths": 2,
  "noInjured": 30,
  "noAffected": 1500,
  "noHomeless": 200,
  "totalAffectedCombined": 1732,
  "reconstructionCosts000Usd": 20000000,
  "insuredDamages000Usd": 5000000,
  "totalDamages000Usd": 30000000,
  "cpi": 1.18
}
```
*   **Respostas:**
    *   `201 Created`: Evento histórico criado com sucesso.
        *   Conteúdo: `DisasterEventHistoryDto`
        *   **Exemplo de Resposta:** (Similar à requisição, com campos potencialmente gerados como um ID se não for `disNo`)
            ```json
            {
              "disNo": "2023-0001-PHL",
              "yearEvent": 2023,
              "seq": 1,
              // ... (resto dos campos)
              "totalDeaths": 25,
              "totalAffected": 6125,
              "totalDamages": 20000000
            }
            ```
    *   `400 Bad Request`: Dados de entrada inválidos ou evento já existe.
    *   `401 Unauthorized`: Não autenticado.
    *   `403 Forbidden`: Não autorizado.

### `GET /api/history/{disNo}`

*   **Resumo:** Busca um evento histórico por DisNo (ID)
*   **Descrição:** Retorna os detalhes de um evento histórico específico.
*   **Segurança:** Requer autenticação JWT.
*   **Parâmetros de Caminho:**
    *   `disNo` (string, obrigatório): DisNo (ID EMDAT) do evento histórico.
*   **Exemplo de Requisição (Caminho):** `/api/history/2023-0001-PHL`
*   **Respostas:**
    *   `200 OK`: Evento histórico encontrado.
        *   Conteúdo: `DisasterEventHistoryDto`
        *   **Exemplo de Resposta:**
            ```json
            {
              "disNo": "2023-0001-PHL",
              "yearEvent": 2023,
              "seq": 1,
              "disasterGroup": "Natural",
              "disasterType": "Earthquake",
              // ... (resto dos campos)
              "totalDeaths": 25,
              "totalAffected": 6125
            }
            ```
    *   `404 Not Found`: Evento histórico não encontrado.

### `GET /api/history`

*   **Resumo:** Lista eventos históricos de desastre
*   **Descrição:** Retorna uma lista paginada de eventos históricos, com filtros opcionais.
*   **Segurança:** Requer autenticação JWT.
*   **Parâmetros de Query (Pageable):**
    *   `page` (integer, opcional, padrão: 0): Número da página.
    *   `size` (integer, opcional, padrão: 10): Tamanho da página.
    *   `sort` (string, opcional, padrão: "yearEvent,desc"): Campo para ordenação e direção (asc/desc).
*   **Parâmetros de Query (Filtro - `DisasterEventHistoryFilterDto`):** (Campos de filtro específicos dependem da definição do DTO, ex: `year`, `country`, `disasterType`)
*   **Exemplo de Requisição (Query):** `/api/history?yearEvent=2023&country=Philippines%20(the)&page=0&size=5`
*   **Respostas:**
    *   `200 OK`: Lista de eventos históricos.
        *   Conteúdo: `Page<DisasterEventHistoryDto>`
        *   **Exemplo de Resposta:**
            ```json
            {
              "content": [
                {
                  "disNo": "2023-0001-PHL",
                  "yearEvent": 2023,
                  "disasterType": "Earthquake",
                  // ...
                  "totalDeaths": 25
                }
                // ... (outros eventos)
              ],
              "pageable": {
                "pageNumber": 0,
                "pageSize": 5,
                "sort": {
                  "sorted": true,
                  "unsorted": false,
                  "empty": false
                },
                "offset": 0,
                "paged": true,
                "unpaged": false
              },
              "totalPages": 1,
              "totalElements": 1,
              "last": true,
              "size": 5,
              "number": 0,
              "sort": {
                "sorted": true,
                "unsorted": false,
                "empty": false
              },
              "numberOfElements": 1,
              "first": true,
              "empty": false
            }
            ```

### `PUT /api/history/{disNo}`

*   **Resumo:** Atualiza um evento histórico de desastre
*   **Descrição:** Atualiza os dados de um evento histórico existente.
*   **Segurança:** Requer autenticação JWT e papel `ROLE_ADMIN`.
*   **Parâmetros de Caminho:**
    *   `disNo` (string, obrigatório): DisNo (ID EMDAT) do evento a ser atualizado.
*   **Corpo da Requisição:** `DisasterEventHistoryDto`
*   **Exemplo de Requisição (Caminho):** `/api/history/2023-0001-PHL`
*   **Exemplo de Requisição (Corpo):**
    ```json
    {
      "disNo": "2023-0001-PHL",
      "yearEvent": 2023,
      "seq": 1,
      "disasterGroup": "Natural",
      "disasterSubgroup": "Geophysical",
      "disasterType": "Earthquake",
      "eventName": "Luzon Earthquake (Atualizado)",
      // ... (outros campos atualizados)
      "totalDeaths": 30
    }
    ```
*   **Respostas:**
    *   `200 OK`: Evento histórico atualizado com sucesso.
        *   Conteúdo: `DisasterEventHistoryDto`
        *   **Exemplo de Resposta:**
            ```json
            {
              "disNo": "2023-0001-PHL",
              "yearEvent": 2023,
              "eventName": "Luzon Earthquake (Atualizado)",
              // ...
              "totalDeaths": 30
            }
            ```
    *   `400 Bad Request`: Dados de entrada inválidos.
    *   `404 Not Found`: Evento histórico não encontrado.
    *   `401 Unauthorized`: Não autenticado.
    *   `403 Forbidden`: Não autorizado.

### `DELETE /api/history/{disNo}`

*   **Resumo:** Deleta um evento histórico de desastre
*   **Descrição:** Remove um evento histórico do sistema.
*   **Segurança:** Requer autenticação JWT e papel `ROLE_ADMIN`.
*   **Parâmetros de Caminho:**
    *   `disNo` (string, obrigatório): DisNo (ID EMDAT) do evento a ser deletado.
*   **Exemplo de Requisição (Caminho):** `/api/history/2023-0001-PHL`
*   **Respostas:**
    *   `204 No Content`: Evento histórico deletado com sucesso. (Sem conteúdo no corpo)
    *   `404 Not Found`: Evento histórico não encontrado.
    *   `401 Unauthorized`: Não autenticado.
    *   `403 Forbidden`: Não autorizado.

## Endpoints de Simulação de Drone

**Caminho Base:** `/api/drone`

**Controlador:** `DroneController.java`

**Tags:** Simulação de Drone

### `POST /api/drone/dispatch/{simulationId}`

*   **Resumo:** Simula o despacho de drones para uma simulação
*   **Descrição:** Com base na predição de risco de uma simulação, gera e retorna uma resposta simulada de despacho de drone.
*   **Segurança:** Requer autenticação JWT e papel `ROLE_USER` ou `ROLE_ADMIN`.
*   **Parâmetros de Caminho:**
    *   `simulationId` (long, obrigatório): ID da simulação de desastre para a qual o drone será despachado.
*   **Exemplo de Requisição (Caminho):** `/api/drone/dispatch/123`
*   **Respostas:**
    *   `200 OK`: Simulação de despacho de drone bem-sucedida.
        *   Conteúdo: `SimulatedDroneResponseDto`
        *   **Exemplo de Resposta:**
            ```json
            {
              "simulationId": 123,
              "disasterType": "Flood",
              "predictedFatalityCategory": "High",
              "dronesDispatched": 5,
              "estimatedCoverageArea": "10 sq km",
              "missionNotes": "Prioridade para operações de resgate no setor 4."
            }
            ```
    *   `404 Not Found`: Simulação não encontrada.
    *   `400 Bad Request`: Não é possível despachar drone (ex: predição da IA não disponível).

## Endpoints de Simulações de Desastre

**Caminho Base:** `/api/simulations`

**Controlador:** `SimulatedDisasterController.java`

**Tags:** Simulações de Desastre

### `POST /api/simulations` - Criar uma Nova Simulação de Desastre

Este endpoint permite aos usuários submeter os parâmetros iniciais de um cenário de desastre. O sistema então poderá processar esta informação, potencialmente com um serviço de IA, para prever resultados como categorias de fatalidade e sugerir respostas apropriadas, como o envio de drones.
*   **Segurança:** Requer autenticação JWT e papel `ROLE_USER` ou `ROLE_ADMIN`.

#### Exemplos de Corpo da Requisição

Abaixo estão exemplos de payloads JSON para criar diferentes simulações de desastres.

##### Exemplo 1: Enchente na América do Sul

Este exemplo representa um evento de enchente ocorrendo na América do Sul.

```json
[
  {
    "inputYear": 2025,           // O ano em que o desastre começa
    "inputStartMonth": 7,        // O mês em que o desastre começa (1-12)
    "inputStartDay": 15,         // O dia em que o desastre começa
    "inputEndYear": 2025,          // O ano em que o desastre termina
    "inputEndMonth": 7,          // O mês em que o desastre termina (1-12)
    "inputEndDay": 18,           // O dia em que o desastre termina
    "inputDisasterGroup": "Natural", // Categoria ampla do desastre
    "inputDisasterSubgroup": "Hydrological", // Subgrupo mais específico (ex: Hidrológico, Geofísico)
    "inputDisasterType": "Flood",    // Tipo específico de desastre
    "inputContinent": "South America", // Continente onde o desastre ocorre
    "inputRegion": "South America",  // Região específica dentro do continente
    "inputDisMagScale": "Km2",       // Escala usada para magnitude (ex: Richter, Km2, Severidade)
    "inputDisMagValue": 500.0,     // Valor numérico da magnitude do desastre
    "inputLatitude": -23.5505,     // Latitude do epicentro/local principal do desastre
    "inputLongitude": -46.6333,    // Longitude do epicentro/local principal do desastre
    "inputCpi": 115.5              // Índice de Preços ao Consumidor na época do desastre, se disponível
  }
]
```

##### Exemplo 2: Terremoto na Ásia

Este exemplo representa um evento de terremoto no Sudeste Asiático.

```json
[
  {
    "inputYear": 2026,
    "inputStartMonth": 1,
    "inputStartDay": 10,
    "inputEndYear": 2026,
    "inputEndMonth": 1,
    "inputEndDay": 10,
    "inputDisasterGroup": "Natural",
    "inputDisasterSubgroup": "Geophysical", // Subgrupo diferente
    "inputDisasterType": "Earthquake",     // Tipo de desastre diferente
    "inputContinent": "Asia",
    "inputRegion": "South-Eastern Asia", // Região mais específica
    "inputDisMagScale": "Richter",      // Escala de magnitude específica para terremotos
    "inputDisMagValue": 7.2,           // Valor da magnitude na escala Richter
    "inputLatitude": 3.2028,
    "inputLongitude": 101.6028,
    "inputCpi": 120.0
  }
]
```

##### Exemplo 3: Seca na África

Este exemplo representa um evento de seca prolongada na África Oriental. Note que o desastre se estende por múltiplos meses.

```json
[
  {
    "inputYear": 2025,
    "inputStartMonth": 11,
    "inputStartDay": 1,
    "inputEndYear": 2026,            // Desastre se estende para o próximo ano
    "inputEndMonth": 2,
    "inputEndDay": 28,
    "inputDisasterGroup": "Natural",
    "inputDisasterSubgroup": "Climatological", // Subgrupo diferente
    "inputDisasterType": "Drought",        // Tipo de desastre diferente
    "inputContinent": "Africa",
    "inputRegion": "Eastern Africa",
    "inputDisMagScale": "Severity",     // Escala de magnitude para seca pode ser qualitativa
    "inputDisMagValue": null,          // Valor da magnitude pode não ser aplicável ou disponível para seca
    "inputLatitude": -1.2921,
    "inputLongitude": 36.8219,
    "inputCpi": 98.7
  }
]
```
*   **Respostas:**
    *   `201 Created`: Simulação inicial criada com sucesso.
        *   Conteúdo: `SimulatedDisasterResponseDto`
        *   **Exemplo de Resposta:**
            ```json
            {
              "id": 1,
              "userId": 2,
              "requestTimestamp": "2023-03-15T10:00:00Z",
              "disasterType": "Hurricane",
              "magnitude": 4,
              "latitude": "25.7617 N",
              "longitude": "80.1918 W",
              "affectedPopulation": 100000,
              "infrastructureDamage": "Severe",
              "countryIso": "USA",
              "iaProcessingStatus": "PENDING",
              "predictedFatalityCategory": null,
              "iaResponseTimestamp": null
            }
            ```
    *   `400 Bad Request`: Dados de entrada inválidos.
    *   `401 Unauthorized`: Não autenticado.
    *   `403 Forbidden`: Não autorizado.

### `POST /api/simulations/{simulationId}/predict`

*   **Resumo:** Processa uma simulação com a IA
*   **Descrição:** Envia os dados da simulação para a API Python para obter a predição da categoria de fatalidade.
*   **Segurança:** Requer autenticação JWT e papel `ROLE_USER` ou `ROLE_ADMIN`.
*   **Parâmetros de Caminho:**
    *   `simulationId` (long, obrigatório): ID da simulação a ser processada.
*   **Exemplo de Requisição (Caminho):** `/api/simulations/1/predict`
*   **Respostas:**
    *   `200 OK`: Processamento com IA concluído.
        *   Conteúdo: `SimulatedDisasterResponseDto`
        *   **Exemplo de Resposta:**
            ```json
            {
              "id": 1,
              "userId": 2,
              "requestTimestamp": "2023-03-15T10:00:00Z",
              "disasterType": "Hurricane",
              "magnitude": 4,
              "latitude": "25.7617 N",
              "longitude": "80.1918 W",
              "affectedPopulation": 100000,
              "infrastructureDamage": "Severe",
              "countryIso": "USA",
              "iaProcessingStatus": "COMPLETED",
              "predictedFatalityCategory": "CATEGORY_3",
              "iaResponseTimestamp": "2023-03-15T10:05:00Z"
            }
            ```
    *   `404 Not Found`: Simulação não encontrada.
    *   `500 Internal Server Error`: Erro ao comunicar com o serviço de IA ou erro interno.

### `GET /api/simulations/{simulationId}`

*   **Resumo:** Busca uma simulação por ID
*   **Descrição:** Retorna os detalhes de uma simulação de desastre específica.
*   **Segurança:** Requer autenticação JWT e papel `ROLE_USER` ou `ROLE_ADMIN`.
*   **Parâmetros de Caminho:**
    *   `simulationId` (long, obrigatório): ID da simulação.
*   **Exemplo de Requisição (Caminho):** `/api/simulations/1`
*   **Respostas:**
    *   `200 OK`: Simulação encontrada.
        *   Conteúdo: `SimulatedDisasterResponseDto`
        *   **Exemplo de Resposta:**
            ```json
            {
              "id": 1,
              "userId": 2,
              "requestTimestamp": "2023-03-15T10:00:00Z",
              "disasterType": "Hurricane",
              "magnitude": 4,
              // ... (outros campos)
              "predictedFatalityCategory": "CATEGORY_3"
            }
            ```
    *   `404 Not Found`: Simulação não encontrada.

### `GET /api/simulations`

*   **Resumo:** Lista simulações de desastre do usuário logado
*   **Descrição:** Retorna uma lista paginada de simulações criadas pelo usuário autenticado, com filtros opcionais.
*   **Segurança:** Requer autenticação JWT e papel `ROLE_USER` ou `ROLE_ADMIN`.
*   **Parâmetros de Query (Pageable):**
    *   `page` (integer, opcional, padrão: 0)
    *   `size` (integer, opcional, padrão: 10)
    *   `sort` (string, opcional, padrão: "requestTimestamp,desc")
*   **Parâmetros de Query (Filtro - `SimulatedDisasterFilterDto`):** (ex: `disasterType=Hurricane`, `countryIso=USA`)
*   **Exemplo de Requisição (Query):** `/api/simulations?disasterType=Hurricane&page=0&size=2`
*   **Respostas:**
    *   `200 OK`: Lista de simulações.
        *   Conteúdo: `Page<SimulatedDisasterResponseDto>`
        *   **Exemplo de Resposta:**
            ```json
            {
              "content": [
                {
                  "id": 1,
                  "userId": 2,
                  "disasterType": "Hurricane",
                  "predictedFatalityCategory": "CATEGORY_3",
                  // ...
                }
                // ... (outras simulações do usuário)
              ],
              "pageable": { /* ... */ },
              "totalPages": 1,
              "totalElements": 1,
              // ... (outros campos da paginação)
            }
            ```

### `GET /api/simulations/admin/all`

*   **Resumo:** Lista todas as simulações de desastre (Admin)
*   **Descrição:** Retorna uma lista paginada de todas as simulações no sistema, com filtros opcionais.
*   **Segurança:** Requer autenticação JWT e papel `ROLE_ADMIN`.
*   **Parâmetros de Query (Pageable):**
    *   `page` (integer, opcional, padrão: 0)
    *   `size` (integer, opcional, padrão: 10)
    *   `sort` (string, opcional, padrão: "requestTimestamp,desc")
*   **Parâmetros de Query (Filtro - `SimulatedDisasterFilterDto`):** (ex: `userId=2`, `iaProcessingStatus=COMPLETED`)
*   **Exemplo de Requisição (Query):** `/api/simulations/admin/all?page=0&size=10`
*   **Respostas:**
    *   `200 OK`: Lista de todas as simulações.
        *   Conteúdo: `Page<SimulatedDisasterResponseDto>`
        *   **Exemplo de Resposta:**
            ```json
            {
              "content": [
                {
                  "id": 1,
                  "userId": 2,
                  "disasterType": "Hurricane",
                  // ...
                },
                {
                  "id": 2,
                  "userId": 5,
                  "disasterType": "Earthquake",
                  // ...
                }
                // ... (todas as simulações no sistema)
              ],
              "pageable": { /* ... */ },
              // ... (outros campos da paginação)
            }
            ```

---

*Observação: Detalhes dos campos de DTO (por exemplo, para `DisasterEventHistoryDto`, `SimulatedDroneResponseDto`, `SimulatedDisasterInputDto`, `SimulatedDisasterResponseDto`, `SimulatedDisasterFilterDto`) não estão totalmente expandidos aqui e estariam disponíveis nas respectivas definições de classe DTO ou na UI Swagger ativa.*
=======
# GlobalSight API

A GlobalSight API fornece endpoints para gerenciar e simular eventos de desastres naturais. Ela permite que usuários se registrem, façam login e, em seguida, criem simulações de desastres, obtenham previsões para elas e visualizem dados históricos de desastres.

**Observação:** Os testes para esta API passaram com sucesso após adicionar a propriedade `python.ia.service.url` ao arquivo `application.properties`. Esta propriedade deve ser configurada para apontar para a URL correta do serviço de IA em Python.

## Endpoints de Autenticação

**Caminho Base:** `/auth`

**Controlador:** `AuthController.java`

**Tags:** Autenticação

### `POST /auth/register`

*   **Resumo:** Registra um novo usuário
*   **Descrição:** Cria um novo usuário no sistema com o papel padrão ROLE_USER.
*   **Corpo da Requisição:** `UserRegistrationDto`
    *   `username` (string, obrigatório): Nome de usuário.
    *   `password` (string, obrigatório): Senha.
    *   `email` (string, obrigatório, formato de email): Endereço de e-mail.
    *   `completeName` (string, opcional): Nome completo do usuário.
*   **Exemplo de Requisição:**
    ```json
    {
      "username": "novousuario",
      "password": "senha123",
      "email": "novousuario@example.com",
      "completeName": "Novo Usuário"
    }
    ```
*   **Respostas:**
    *   `201 Created`: Usuário registrado com sucesso.
        *   Conteúdo: `UserDetailsDto`
            *   `id` (long)
            *   `username` (string)
            *   `email` (string)
            *   `completeName` (string)
            *   `roles` (array de strings)
        *   **Exemplo de Resposta:**
            ```json
            {
              "id": 1,
              "username": "novousuario",
              "email": "novousuario@example.com",
              "completeName": "Novo Usuário",
              "roles": ["ROLE_USER"]
            }
            ```
    *   `400 Bad Request`: Dados de entrada inválidos ou nome de usuário já existe.

### `POST /auth/login`

*   **Resumo:** Autentica um usuário
*   **Descrição:** Autentica um usuário e retorna um token JWT se as credenciais forem válidas.
*   **Corpo da Requisição:** `AuthRequestDto`
    *   `username` (string, obrigatório): Nome de usuário.
    *   `password` (string, obrigatório): Senha.
*   **Exemplo de Requisição:**
    ```json
    {
      "username": "usuarioexistente",
      "password": "senha123"
    }
    ```
*   **Respostas:**
    *   `200 OK`: Login bem-sucedido, token JWT retornado.
        *   Conteúdo: `AuthResponseDto`
            *   `token` (string): Token JWT.
            *   `userId` (long)
            *   `username` (string)
            *   `email` (string)
            *   `roles` (array de strings)
        *   **Exemplo de Resposta:**
            ```json
            {
              "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGlzdGluZ3VzZXIiLCJpYXQiOjE2NzgzNzYxMzUsImV4cCI6MTY3ODM3OTczNX0.exampleToken",
              "userId": 2,
              "username": "usuarioexistente",
              "email": "usuarioexistente@example.com",
              "roles": ["ROLE_USER"]
            }
            ```
    *   `400 Bad Request`: Requisição de login inválida.
    *   `401 Unauthorized`: Credenciais inválidas.

## Endpoints de Histórico de Eventos de Desastre

**Caminho Base:** `/api/history`

**Controlador:** `DisasterEventHistoryController.java`

**Tags:** Histórico de Desastres

**Segurança:** Requer Autenticação JWT para endpoints marcados com `bearerAuth`.

### `POST /api/history`

*   **Resumo:** Cria um novo evento histórico de desastre
*   **Descrição:** Adiciona um novo registro ao histórico de desastres. Requer papel ADMIN.
    *   **Segurança:** Requer autenticação JWT e papel `ROLE_USER`.
    *   **Corpo da Requisição:** `DisasterEventHistoryDto` (inclui campos como `disNo`, `yearEvent`, `disasterGroup`, `country`, `totalDeaths`, `coordinateX`, `coordinateZ`, etc. Detalhes completos na definição do DTO.)
*   **Exemplo de Requisição:**
    ```json
    {
      "disNo": "2023-0001-PHL",
      "yearEvent": 2023,
      "seq": 1,
      "disasterGroup": "Natural",
      "disasterSubgroup": "Geophysical",
      "disasterType": "Earthquake",
      "disasterSubtype": "Ground movement",
      "disasterSubsubtype": null,
      "eventName": "Luzon Earthquake",
      "entryCriteria": "Kill >= 10",
      "iso": "PHL",
      "country": "Philippines (the)",
      "region": "Asia",
      "continent": "Asia",
      "location": "Luzon Island",
      "origin": "Fault rupture",
      "associatedDis": null,
      "associatedDis2": null,
      "ofdaResponse": "Yes",
      "appeal": "Yes",
      "declaration": "Yes",
      "aidContribution": 1000000,
      "disMagValue": 7,
      "disMagScale": "Richter",
      "latitude": "15.8 N",
      "longitude": "120.7 E",
      "localTime": "10:30",
      "riverBasin": null,
      "startYear": 2023,
      "startMonth": 3,
      "startDay": 15,
      "endYear": 2023,
      "endMonth": 3,
      "endDay": 15,
      "totalDeaths": 25,
      "noInjured": 100,
      "noAffected": 5000,
      "noHomeless": 1000,
      "totalAffected": 6125,
      "reconstructionCosts": 5000000,
      "insuredDamages": 1000000,
      "totalDamages": 20000000,
      "cpi": 1.2
    }
    ```
*   **Respostas:**
    *   `201 Created`: Evento histórico criado com sucesso.
        *   Conteúdo: `DisasterEventHistoryDto`
        *   **Exemplo de Resposta:** (Similar à requisição, com campos potencialmente gerados como um ID se não for `disNo`)
            ```json
            {
              "disNo": "2023-0001-PHL",
              "yearEvent": 2023,
              "seq": 1,
              // ... (resto dos campos)
              "totalDeaths": 25,
              "totalAffected": 6125,
              "totalDamages": 20000000
            }
            ```
    *   `400 Bad Request`: Dados de entrada inválidos ou evento já existe.
    *   `401 Unauthorized`: Não autenticado.
    *   `403 Forbidden`: Não autorizado (requer ADMIN).

### `GET /api/history/{disNo}`

*   **Resumo:** Busca um evento histórico por DisNo (ID)
*   **Descrição:** Retorna os detalhes de um evento histórico específico.
*   **Parâmetros de Caminho:**
    *   `disNo` (string, obrigatório): DisNo (ID EMDAT) do evento histórico.
*   **Exemplo de Requisição (Caminho):** `/api/history/2023-0001-PHL`
*   **Respostas:**
    *   `200 OK`: Evento histórico encontrado.
        *   Conteúdo: `DisasterEventHistoryDto`
        *   **Exemplo de Resposta:**
            ```json
            {
              "disNo": "2023-0001-PHL",
              "yearEvent": 2023,
              "seq": 1,
              "disasterGroup": "Natural",
              "disasterType": "Earthquake",
              // ... (resto dos campos)
              "totalDeaths": 25,
              "totalAffected": 6125
            }
            ```
    *   `404 Not Found`: Evento histórico não encontrado.

### `GET /api/history`

*   **Resumo:** Lista eventos históricos de desastre
*   **Descrição:** Retorna uma lista paginada de eventos históricos, com filtros opcionais.
*   **Parâmetros de Query (Pageable):**
    *   `page` (integer, opcional, padrão: 0): Número da página.
    *   `size` (integer, opcional, padrão: 10): Tamanho da página.
    *   `sort` (string, opcional, padrão: "yearEvent,desc"): Campo para ordenação e direção (asc/desc).
*   **Parâmetros de Query (Filtro - `DisasterEventHistoryFilterDto`):** (Campos de filtro específicos dependem da definição do DTO, ex: `year`, `country`, `disasterType`)
*   **Exemplo de Requisição (Query):** `/api/history?yearEvent=2023&country=Philippines%20(the)&page=0&size=5`
*   **Respostas:**
    *   `200 OK`: Lista de eventos históricos.
        *   Conteúdo: `Page<DisasterEventHistoryDto>`
        *   **Exemplo de Resposta:**
            ```json
            {
              "content": [
                {
                  "disNo": "2023-0001-PHL",
                  "yearEvent": 2023,
                  "disasterType": "Earthquake",
                  // ...
                  "totalDeaths": 25
                }
                // ... (outros eventos)
              ],
              "pageable": {
                "pageNumber": 0,
                "pageSize": 5,
                "sort": {
                  "sorted": true,
                  "unsorted": false,
                  "empty": false
                },
                "offset": 0,
                "paged": true,
                "unpaged": false
              },
              "totalPages": 1,
              "totalElements": 1,
              "last": true,
              "size": 5,
              "number": 0,
              "sort": {
                "sorted": true,
                "unsorted": false,
                "empty": false
              },
              "numberOfElements": 1,
              "first": true,
              "empty": false
            }
            ```

### `PUT /api/history/{disNo}`

*   **Resumo:** Atualiza um evento histórico de desastre
*   **Descrição:** Atualiza os dados de um evento histórico existente. Requer papel ADMIN.
    *   **Segurança:** Requer autenticação JWT e papel `ROLE_USER`.
*   **Parâmetros de Caminho:**
    *   `disNo` (string, obrigatório): DisNo (ID EMDAT) do evento a ser atualizado.
*   **Corpo da Requisição:** `DisasterEventHistoryDto`
*   **Exemplo de Requisição (Caminho):** `/api/history/2023-0001-PHL`
*   **Exemplo de Requisição (Corpo):**
    ```json
    {
      "disNo": "2023-0001-PHL",
      "yearEvent": 2023,
      "seq": 1,
      "disasterGroup": "Natural",
      "disasterSubgroup": "Geophysical",
      "disasterType": "Earthquake",
      "eventName": "Luzon Earthquake (Atualizado)",
      // ... (outros campos atualizados)
      "totalDeaths": 30
    }
    ```
*   **Respostas:**
    *   `200 OK`: Evento histórico atualizado com sucesso.
        *   Conteúdo: `DisasterEventHistoryDto`
        *   **Exemplo de Resposta:**
            ```json
            {
              "disNo": "2023-0001-PHL",
              "yearEvent": 2023,
              "eventName": "Luzon Earthquake (Atualizado)",
              // ...
              "totalDeaths": 30
            }
            ```
    *   `400 Bad Request`: Dados de entrada inválidos.
    *   `404 Not Found`: Evento histórico não encontrado.
    *   `401 Unauthorized`: Não autenticado.
    *   `403 Forbidden`: Não autorizado (requer ADMIN).

### `DELETE /api/history/{disNo}`

*   **Resumo:** Deleta um evento histórico de desastre
*   **Descrição:** Remove um evento histórico do sistema. Requer papel ADMIN.
    *   **Segurança:** Requer autenticação JWT e papel `ROLE_USER`.
*   **Parâmetros de Caminho:**
    *   `disNo` (string, obrigatório): DisNo (ID EMDAT) do evento a ser deletado.
*   **Exemplo de Requisição (Caminho):** `/api/history/2023-0001-PHL`
*   **Respostas:**
    *   `204 No Content`: Evento histórico deletado com sucesso. (Sem conteúdo no corpo)
    *   `404 Not Found`: Evento histórico não encontrado.
    *   `401 Unauthorized`: Não autenticado.
    *   `403 Forbidden`: Não autorizado (requer ADMIN).

## Endpoints de Simulação de Drone

**Caminho Base:** `/api/drone`

**Controlador:** `DroneController.java`

**Tags:** Simulação de Drone

**Segurança:** Requer Autenticação JWT para todos os endpoints (`bearerAuth`).

### `POST /api/drone/dispatch/{simulationId}`

*   **Resumo:** Simula o despacho de drones para uma simulação
*   **Descrição:** Com base na predição de risco de uma simulação, gera e retorna uma resposta simulada de despacho de drone.
*   **Segurança:** `bearerAuth`, `ROLE_USER` ou `ROLE_ADMIN`
*   **Parâmetros de Caminho:**
    *   `simulationId` (long, obrigatório): ID da simulação de desastre para a qual o drone será despachado.
*   **Exemplo de Requisição (Caminho):** `/api/drone/dispatch/123`
*   **Respostas:**
    *   `200 OK`: Simulação de despacho de drone bem-sucedida.
        *   Conteúdo: `SimulatedDroneResponseDto`
        *   **Exemplo de Resposta:**
            ```json
            {
              "simulationId": 123,
              "disasterType": "Flood",
              "predictedFatalityCategory": "High",
              "dronesDispatched": 5,
              "estimatedCoverageArea": "10 sq km",
              "missionNotes": "Prioridade para operações de resgate no setor 4."
            }
            ```
    *   `404 Not Found`: Simulação não encontrada.
    *   `400 Bad Request`: Não é possível despachar drone (ex: predição da IA não disponível).

## Endpoints de Simulações de Desastre

**Caminho Base:** `/api/simulations`

**Controlador:** `SimulatedDisasterController.java`

**Tags:** Simulações de Desastre

**Segurança:** Requer Autenticação JWT para todos os endpoints (`bearerAuth`).

### `POST /api/simulations` - Criar uma Nova Simulação de Desastre

Este endpoint permite aos usuários submeter os parâmetros iniciais de um cenário de desastre. O sistema então poderá processar esta informação, potencialmente com um serviço de IA, para prever resultados como categorias de fatalidade e sugerir respostas apropriadas, como o envio de drones.

#### Exemplos de Corpo da Requisição

Abaixo estão exemplos de payloads JSON para criar diferentes simulações de desastres.

##### Exemplo 1: Enchente na América do Sul

Este exemplo representa um evento de enchente ocorrendo na América do Sul.

```json
[
  {
    "inputYear": 2025,           // O ano em que o desastre começa
    "inputStartMonth": 7,        // O mês em que o desastre começa (1-12)
    "inputStartDay": 15,         // O dia em que o desastre começa
    "inputEndYear": 2025,          // O ano em que o desastre termina
    "inputEndMonth": 7,          // O mês em que o desastre termina (1-12)
    "inputEndDay": 18,           // O dia em que o desastre termina
    "inputDisasterGroup": "Natural", // Categoria ampla do desastre
    "inputDisasterSubgroup": "Hydrological", // Subgrupo mais específico (ex: Hidrológico, Geofísico)
    "inputDisasterType": "Flood",    // Tipo específico de desastre
    "inputContinent": "South America", // Continente onde o desastre ocorre
    "inputRegion": "South America",  // Região específica dentro do continente
    "inputDisMagScale": "Km2",       // Escala usada para magnitude (ex: Richter, Km2, Severidade)
    "inputDisMagValue": 500.0,     // Valor numérico da magnitude do desastre
    "inputLatitude": -23.5505,     // Latitude do epicentro/local principal do desastre
    "inputLongitude": -46.6333,    // Longitude do epicentro/local principal do desastre
    "inputCpi": 115.5              // Índice de Preços ao Consumidor na época do desastre, se disponível
  }
]
```

##### Exemplo 2: Terremoto na Ásia

Este exemplo representa um evento de terremoto no Sudeste Asiático.

```json
[
  {
    "inputYear": 2026,
    "inputStartMonth": 1,
    "inputStartDay": 10,
    "inputEndYear": 2026,
    "inputEndMonth": 1,
    "inputEndDay": 10,
    "inputDisasterGroup": "Natural",
    "inputDisasterSubgroup": "Geophysical", // Subgrupo diferente
    "inputDisasterType": "Earthquake",     // Tipo de desastre diferente
    "inputContinent": "Asia",
    "inputRegion": "South-Eastern Asia", // Região mais específica
    "inputDisMagScale": "Richter",      // Escala de magnitude específica para terremotos
    "inputDisMagValue": 7.2,           // Valor da magnitude na escala Richter
    "inputLatitude": 3.2028, 
    "inputLongitude": 101.6028,
    "inputCpi": 120.0 
  }
]
```

##### Exemplo 3: Seca na África

Este exemplo representa um evento de seca prolongada na África Oriental. Note que o desastre se estende por múltiplos meses.

```json
[
  {
    "inputYear": 2025,
    "inputStartMonth": 11,
    "inputStartDay": 1,
    "inputEndYear": 2026,            // Desastre se estende para o próximo ano
    "inputEndMonth": 2,
    "inputEndDay": 28,
    "inputDisasterGroup": "Natural",
    "inputDisasterSubgroup": "Climatological", // Subgrupo diferente
    "inputDisasterType": "Drought",        // Tipo de desastre diferente
    "inputContinent": "Africa",
    "inputRegion": "Eastern Africa",
    "inputDisMagScale": "Severity",     // Escala de magnitude para seca pode ser qualitativa
    "inputDisMagValue": null,          // Valor da magnitude pode não ser aplicável ou disponível para seca
    "inputLatitude": -1.2921, 
    "inputLongitude": 36.8219,
    "inputCpi": 98.7
  }
]
```
*   **Respostas:**
    *   `201 Created`: Simulação inicial criada com sucesso.
        *   Conteúdo: `SimulatedDisasterResponseDto`
        *   **Exemplo de Resposta:**
            ```json
            {
              "id": 1,
              "userId": 2,
              "requestTimestamp": "2023-03-15T10:00:00Z",
              "disasterType": "Hurricane",
              "magnitude": 4,
              "latitude": "25.7617 N",
              "longitude": "80.1918 W",
              "affectedPopulation": 100000,
              "infrastructureDamage": "Severe",
              "countryIso": "USA",
              "iaProcessingStatus": "PENDING",
              "predictedFatalityCategory": null,
              "iaResponseTimestamp": null
            }
            ```
    *   `400 Bad Request`: Dados de entrada inválidos.
    *   `401 Unauthorized`: Não autenticado.
    *   `403 Forbidden`: Não autorizado.

### `POST /api/simulations/{simulationId}/predict`

*   **Resumo:** Processa uma simulação com a IA
*   **Descrição:** Envia os dados da simulação para a API Python para obter a predição da categoria de fatalidade.
*   **Segurança:** `bearerAuth`, `ROLE_USER` ou `ROLE_ADMIN`
*   **Parâmetros de Caminho:**
    *   `simulationId` (long, obrigatório): ID da simulação a ser processada.
*   **Exemplo de Requisição (Caminho):** `/api/simulations/1/predict`
*   **Respostas:**
    *   `200 OK`: Processamento com IA concluído.
        *   Conteúdo: `SimulatedDisasterResponseDto`
        *   **Exemplo de Resposta:**
            ```json
            {
              "id": 1,
              "userId": 2,
              "requestTimestamp": "2023-03-15T10:00:00Z",
              "disasterType": "Hurricane",
              "magnitude": 4,
              "latitude": "25.7617 N",
              "longitude": "80.1918 W",
              "affectedPopulation": 100000,
              "infrastructureDamage": "Severe",
              "countryIso": "USA",
              "iaProcessingStatus": "COMPLETED",
              "predictedFatalityCategory": "CATEGORY_3",
              "iaResponseTimestamp": "2023-03-15T10:05:00Z"
            }
            ```
    *   `404 Not Found`: Simulação não encontrada.
    *   `500 Internal Server Error`: Erro ao comunicar com o serviço de IA ou erro interno.

### `GET /api/simulations/{simulationId}`

*   **Resumo:** Busca uma simulação por ID
*   **Descrição:** Retorna os detalhes de uma simulação de desastre específica.
*   **Segurança:** `bearerAuth`, `ROLE_USER` ou `ROLE_ADMIN`
*   **Parâmetros de Caminho:**
    *   `simulationId` (long, obrigatório): ID da simulação.
*   **Exemplo de Requisição (Caminho):** `/api/simulations/1`
*   **Respostas:**
    *   `200 OK`: Simulação encontrada.
        *   Conteúdo: `SimulatedDisasterResponseDto`
        *   **Exemplo de Resposta:**
            ```json
            {
              "id": 1,
              "userId": 2,
              "requestTimestamp": "2023-03-15T10:00:00Z",
              "disasterType": "Hurricane",
              "magnitude": 4,
              // ... (outros campos)
              "predictedFatalityCategory": "CATEGORY_3"
            }
            ```
    *   `404 Not Found`: Simulação não encontrada.

### `GET /api/simulations`

*   **Resumo:** Lista simulações de desastre do usuário logado
*   **Descrição:** Retorna uma lista paginada de simulações criadas pelo usuário autenticado, com filtros opcionais.
*   **Segurança:** `bearerAuth`, `ROLE_USER` ou `ROLE_ADMIN`
*   **Parâmetros de Query (Pageable):**
    *   `page` (integer, opcional, padrão: 0)
    *   `size` (integer, opcional, padrão: 10)
    *   `sort` (string, opcional, padrão: "requestTimestamp,desc")
*   **Parâmetros de Query (Filtro - `SimulatedDisasterFilterDto`):** (ex: `disasterType=Hurricane`, `countryIso=USA`)
*   **Exemplo de Requisição (Query):** `/api/simulations?disasterType=Hurricane&page=0&size=2`
*   **Respostas:**
    *   `200 OK`: Lista de simulações.
        *   Conteúdo: `Page<SimulatedDisasterResponseDto>`
        *   **Exemplo de Resposta:**
            ```json
            {
              "content": [
                {
                  "id": 1,
                  "userId": 2,
                  "disasterType": "Hurricane",
                  "predictedFatalityCategory": "CATEGORY_3",
                  // ...
                }
                // ... (outras simulações do usuário)
              ],
              "pageable": { /* ... */ },
              "totalPages": 1,
              "totalElements": 1,
              // ... (outros campos da paginação)
            }
            ```

### `GET /api/simulations/admin/all`

*   **Resumo:** Lista todas as simulações de desastre (Admin)
*   **Descrição:** Retorna uma lista paginada de todas as simulações no sistema, com filtros opcionais. Requer papel ADMIN.
    *   **Segurança:** Requer autenticação JWT e papel `ROLE_USER`.
*   **Parâmetros de Query (Pageable):**
    *   `page` (integer, opcional, padrão: 0)
    *   `size` (integer, opcional, padrão: 10)
    *   `sort` (string, opcional, padrão: "requestTimestamp,desc")
*   **Parâmetros de Query (Filtro - `SimulatedDisasterFilterDto`):** (ex: `userId=2`, `iaProcessingStatus=COMPLETED`)
*   **Exemplo de Requisição (Query):** `/api/simulations/admin/all?page=0&size=10`
*   **Respostas:**
    *   `200 OK`: Lista de todas as simulações.
        *   Conteúdo: `Page<SimulatedDisasterResponseDto>`
        *   **Exemplo de Resposta:**
            ```json
            {
              "content": [
                {
                  "id": 1,
                  "userId": 2,
                  "disasterType": "Hurricane",
                  // ...
                },
                {
                  "id": 2,
                  "userId": 5,
                  "disasterType": "Earthquake",
                  // ...
                }
                // ... (todas as simulações no sistema)
              ],
              "pageable": { /* ... */ },
              // ... (outros campos da paginação)
            }
            ```

---

# API Documentation

This document provides an overview of the API endpoints for the GlobalSight API.

**Note:** The tests for this API passed successfully after adding the `python.ia.service.url` property to the `application.properties` file. This property should be configured to point to the correct URL of the Python AI service.

## Authentication Endpoints

**Base Path:** `/auth`

**Controller:** `AuthController.java`

**Tags:** Autenticação

### `POST /auth/register`

*   **Summary:** Registra um novo usuário
*   **Description:** Cria um novo usuário no sistema com o papel padrão ROLE_USER.
*   **Request Body:** `UserRegistrationDto`
    *   `username` (string, required): Nome de usuário.
    *   `password` (string, required): Senha.
    *   `email` (string, required, email format): Endereço de e-mail.
    *   `completeName` (string, optional): Nome completo do usuário.
*   **Request Example:**
    ```json
    {
      "username": "newuser",
      "password": "password123",
      "email": "newuser@example.com",
      "completeName": "New User"
    }
    ```
*   **Responses:**
    *   `201 Created`: Usuário registrado com sucesso.
        *   Content: `UserDetailsDto`
            *   `id` (long)
            *   `username` (string)
            *   `email` (string)
            *   `completeName` (string)
            *   `roles` (array of strings)
        *   **Response Example:**
            ```json
            {
              "id": 1,
              "username": "newuser",
              "email": "newuser@example.com",
              "completeName": "New User",
              "roles": ["ROLE_USER"]
            }
            ```
    *   `400 Bad Request`: Dados de entrada inválidos ou nome de usuário já existe.

### `POST /auth/login`

*   **Summary:** Autentica um usuário
*   **Description:** Autentica um usuário e retorna um token JWT se as credenciais forem válidas.
*   **Request Body:** `AuthRequestDto`
    *   `username` (string, required): Nome de usuário.
    *   `password` (string, required): Senha.
*   **Request Example:**
    ```json
    {
      "username": "existinguser",
      "password": "password123"
    }
    ```
*   **Responses:**
    *   `200 OK`: Login bem-sucedido, token JWT retornado.
        *   Content: `AuthResponseDto`
            *   `token` (string): Token JWT.
            *   `userId` (long)
            *   `username` (string)
            *   `email` (string)
            *   `roles` (array of strings)
        *   **Response Example:**
            ```json
            {
              "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGlzdGluZ3VzZXIiLCJpYXQiOjE2NzgzNzYxMzUsImV4cCI6MTY3ODM3OTczNX0.exampleToken",
              "userId": 2,
              "username": "existinguser",
              "email": "existinguser@example.com",
              "roles": ["ROLE_USER"]
            }
            ```
    *   `400 Bad Request`: Requisição de login inválida.
    *   `401 Unauthorized`: Credenciais inválidas.

## Disaster Event History Endpoints

**Base Path:** `/api/history`

**Controller:** `DisasterEventHistoryController.java`

**Tags:** Histórico de Desastres

**Security:** Requires JWT Authentication for endpoints marked with `bearerAuth`.

### `POST /api/history`

*   **Summary:** Cria um novo evento histórico de desastre
*   **Description:** Adiciona um novo registro ao histórico de desastres. Requer papel ADMIN.
*   **Security:** `bearerAuth`, `ROLE_ADMIN`
*   **Request Body:** `DisasterEventHistoryDto`
*   **Request Example:**
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
*   **Responses:**
    *   `201 Created`: Evento histórico criado com sucesso.
        *   Content: `DisasterEventHistoryDto`
        *   **Response Example:** (Similar to request, with potentially generated fields like an ID if not `disNo`)
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

*   **Summary:** Busca um evento histórico por DisNo (ID)
*   **Description:** Retorna os detalhes de um evento histórico específico.
*   **Path Parameters:**
    *   `disNo` (string, required): DisNo (ID EMDAT) do evento histórico.
*   **Request Example (Path):** `/api/history/2023-0001-PHL`
*   **Responses:**
    *   `200 OK`: Evento histórico encontrado.
        *   Content: `DisasterEventHistoryDto`
        *   **Response Example:**
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

*   **Summary:** Lista eventos históricos de desastre
*   **Description:** Retorna uma lista paginada de eventos históricos, com filtros opcionais.
*   **Query Parameters (Pageable):**
    *   `page` (integer, optional, default: 0): Número da página.
    *   `size` (integer, optional, default: 10): Tamanho da página.
    *   `sort` (string, optional, default: "yearEvent,desc"): Campo para ordenação e direção (asc/desc).
*   **Query Parameters (Filter - `DisasterEventHistoryFilterDto`):** (e.g., `year=2023`, `country=PHL`, `disasterType=Earthquake`)
*   **Request Example (Query):** `/api/history?yearEvent=2023&country=Philippines%20(the)&page=0&size=5`
*   **Responses:**
    *   `200 OK`: Lista de eventos históricos.
        *   Content: `Page<DisasterEventHistoryDto>`
        *   **Response Example:**
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

*   **Summary:** Atualiza um evento histórico de desastre
*   **Description:** Atualiza os dados de um evento histórico existente. Requer papel ADMIN.
*   **Security:** `bearerAuth`, `ROLE_ADMIN`
*   **Path Parameters:**
    *   `disNo` (string, required): DisNo (ID EMDAT) do evento a ser atualizado.
*   **Request Body:** `DisasterEventHistoryDto`
*   **Request Example (Path):** `/api/history/2023-0001-PHL`
*   **Request Example (Body):**
    ```json
    {
      "disNo": "2023-0001-PHL",
      "yearEvent": 2023,
      "seq": 1,
      "disasterGroup": "Natural",
      "disasterSubgroup": "Geophysical",
      "disasterType": "Earthquake",
      "eventName": "Luzon Earthquake (Updated)",
      // ... (outros campos atualizados)
      "totalDeaths": 30
    }
    ```
*   **Responses:**
    *   `200 OK`: Evento histórico atualizado com sucesso.
        *   Content: `DisasterEventHistoryDto`
        *   **Response Example:**
            ```json
            {
              "disNo": "2023-0001-PHL",
              "yearEvent": 2023,
              "eventName": "Luzon Earthquake (Updated)",
              // ...
              "totalDeaths": 30
            }
            ```
    *   `400 Bad Request`: Dados de entrada inválidos.
    *   `404 Not Found`: Evento histórico não encontrado.
    *   `401 Unauthorized`: Não autenticado.
    *   `403 Forbidden`: Não autorizado (requer ADMIN).

### `DELETE /api/history/{disNo}`

*   **Summary:** Deleta um evento histórico de desastre
*   **Description:** Remove um evento histórico do sistema. Requer papel ADMIN.
*   **Security:** `bearerAuth`, `ROLE_ADMIN`
*   **Path Parameters:**
    *   `disNo` (string, required): DisNo (ID EMDAT) do evento a ser deletado.
*   **Request Example (Path):** `/api/history/2023-0001-PHL`
*   **Responses:**
    *   `204 No Content`: Evento histórico deletado com sucesso. (No body content)
    *   `404 Not Found`: Evento histórico não encontrado.
    *   `401 Unauthorized`: Não autenticado.
    *   `403 Forbidden`: Não autorizado (requer ADMIN).

## Drone Simulation Endpoints

**Base Path:** `/api/drone`

**Controller:** `DroneController.java`

**Tags:** Simulação de Drone

**Security:** Requires JWT Authentication for all endpoints (`bearerAuth`).

### `POST /api/drone/dispatch/{simulationId}`

*   **Summary:** Simula o despacho de drones para uma simulação
*   **Description:** Com base na predição de risco de uma simulação, gera e retorna uma resposta simulada de despacho de drone.
*   **Security:** `bearerAuth`, `ROLE_USER` or `ROLE_ADMIN`
*   **Path Parameters:**
    *   `simulationId` (long, required): ID da simulação de desastre para a qual o drone será despachado.
*   **Request Example (Path):** `/api/drone/dispatch/123`
*   **Responses:**
    *   `200 OK`: Simulação de despacho de drone bem-sucedida.
        *   Content: `SimulatedDroneResponseDto`
        *   **Response Example:**
            ```json
            {
              "simulationId": 123,
              "disasterType": "Flood",
              "predictedFatalityCategory": "High",
              "dronesDispatched": 5,
              "estimatedCoverageArea": "10 sq km",
              "missionNotes": "Priority to rescue operations in sector 4."
            }
            ```
    *   `404 Not Found`: Simulação não encontrada.
    *   `400 Bad Request`: Não é possível despachar drone (ex: predição da IA não disponível).

## Simulated Disaster Endpoints

**Base Path:** `/api/simulations`

**Controller:** `SimulatedDisasterController.java`

**Tags:** Simulações de Desastre

**Security:** Requires JWT Authentication for all endpoints (`bearerAuth`).

### `POST /api/simulations`

*   **Summary:** Cria uma nova simulação de desastre (etapa inicial)
*   **Description:** Registra os dados de entrada de um desastre. A predição da IA deve ser acionada separadamente.
*   **Security:** `bearerAuth`, `ROLE_USER` or `ROLE_ADMIN`
*   **Request Body:** `SimulatedDisasterInputDto`
*   **Request Example:**
    ```json
    {
      "disasterType": "Hurricane",
      "magnitude": 4,
      "latitude": "25.7617 N",
      "longitude": "80.1918 W",
      "affectedPopulation": 100000,
      "infrastructureDamage": "Severe",
      "countryIso": "USA"
    }
    ```
*   **Responses:**
    *   `201 Created`: Simulação inicial criada com sucesso.
        *   Content: `SimulatedDisasterResponseDto`
        *   **Response Example:**
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

*   **Summary:** Processa uma simulação com a IA
*   **Description:** Envia os dados da simulação para a API Python para obter a predição da categoria de fatalidade.
*   **Security:** `bearerAuth`, `ROLE_USER` or `ROLE_ADMIN`
*   **Path Parameters:**
    *   `simulationId` (long, required): ID da simulação a ser processada.
*   **Request Example (Path):** `/api/simulations/1/predict`
*   **Responses:**
    *   `200 OK`: Processamento com IA concluído.
        *   Content: `SimulatedDisasterResponseDto`
        *   **Response Example:**
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

*   **Summary:** Busca uma simulação por ID
*   **Description:** Retorna os detalhes de uma simulação de desastre específica.
*   **Security:** `bearerAuth`, `ROLE_USER` or `ROLE_ADMIN`
*   **Path Parameters:**
    *   `simulationId` (long, required): ID da simulação.
*   **Request Example (Path):** `/api/simulations/1`
*   **Responses:**
    *   `200 OK`: Simulação encontrada.
        *   Content: `SimulatedDisasterResponseDto`
        *   **Response Example:**
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

*   **Summary:** Lista simulações de desastre do usuário logado
*   **Description:** Retorna uma lista paginada de simulações criadas pelo usuário autenticado, com filtros opcionais.
*   **Security:** `bearerAuth`, `ROLE_USER` or `ROLE_ADMIN`
*   **Query Parameters (Pageable):**
    *   `page` (integer, optional, default: 0)
    *   `size` (integer, optional, default: 10)
    *   `sort` (string, optional, default: "requestTimestamp,desc")
*   **Query Parameters (Filter - `SimulatedDisasterFilterDto`):** (e.g., `disasterType=Hurricane`, `countryIso=USA`)
*   **Request Example (Query):** `/api/simulations?disasterType=Hurricane&page=0&size=2`
*   **Responses:**
    *   `200 OK`: Lista de simulações.
        *   Content: `Page<SimulatedDisasterResponseDto>`
        *   **Response Example:**
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

*   **Summary:** Lista todas as simulações de desastre (Admin)
*   **Description:** Retorna uma lista paginada de todas as simulações no sistema, com filtros opcionais. Requer papel ADMIN.
*   **Security:** `bearerAuth`, `ROLE_ADMIN`
*   **Query Parameters (Pageable):**
    *   `page` (integer, optional, default: 0)
    *   `size` (integer, optional, default: 10)
    *   `sort` (string, optional, default: "requestTimestamp,desc")
*   **Query Parameters (Filter - `SimulatedDisasterFilterDto`):** (e.g., `userId=2`, `iaProcessingStatus=COMPLETED`)
*   **Request Example (Query):** `/api/simulations/admin/all?page=0&size=10`
*   **Responses:**
    *   `200 OK`: Lista de todas as simulações.
        *   Content: `Page<SimulatedDisasterResponseDto>`
        *   **Response Example:**
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

*Note: DTO field details (e.g., for `DisasterEventHistoryDto`, `SimulatedDroneResponseDto`, `SimulatedDisasterInputDto`, `SimulatedDisasterResponseDto`, `SimulatedDisasterFilterDto`) are not fully expanded here and would be available in the respective DTO class definitions or the live Swagger UI.*

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
*   **Responses:**
    *   `201 Created`: Usuário registrado com sucesso.
        *   Content: `UserDetailsDto`
            *   `id` (long)
            *   `username` (string)
            *   `email` (string)
            *   `completeName` (string)
            *   `roles` (array of strings)
    *   `400 Bad Request`: Dados de entrada inválidos ou nome de usuário já existe.

### `POST /auth/login`

*   **Summary:** Autentica um usuário
*   **Description:** Autentica um usuário e retorna um token JWT se as credenciais forem válidas.
*   **Request Body:** `AuthRequestDto`
    *   `username` (string, required): Nome de usuário.
    *   `password` (string, required): Senha.
*   **Responses:**
    *   `200 OK`: Login bem-sucedido, token JWT retornado.
        *   Content: `AuthResponseDto`
            *   `token` (string): Token JWT.
            *   `userId` (long)
            *   `username` (string)
            *   `email` (string)
            *   `roles` (array of strings)
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
*   **Request Body:** `DisasterEventHistoryDto` (details of fields depend on DTO definition)
*   **Responses:**
    *   `201 Created`: Evento histórico criado com sucesso.
        *   Content: `DisasterEventHistoryDto`
    *   `400 Bad Request`: Dados de entrada inválidos ou evento já existe.
    *   `401 Unauthorized`: Não autenticado.
    *   `403 Forbidden`: Não autorizado (requer ADMIN).

### `GET /api/history/{disNo}`

*   **Summary:** Busca um evento histórico por DisNo (ID)
*   **Description:** Retorna os detalhes de um evento histórico específico.
*   **Path Parameters:**
    *   `disNo` (string, required): DisNo (ID EMDAT) do evento histórico.
*   **Responses:**
    *   `200 OK`: Evento histórico encontrado.
        *   Content: `DisasterEventHistoryDto`
    *   `404 Not Found`: Evento histórico não encontrado.

### `GET /api/history`

*   **Summary:** Lista eventos históricos de desastre
*   **Description:** Retorna uma lista paginada de eventos históricos, com filtros opcionais.
*   **Query Parameters (Pageable):**
    *   `page` (integer, optional, default: 0): Número da página.
    *   `size` (integer, optional, default: 10): Tamanho da página.
    *   `sort` (string, optional, default: "yearEvent,desc"): Campo para ordenação e direção (asc/desc).
*   **Query Parameters (Filter - `DisasterEventHistoryFilterDto`):** (Specific filter fields depend on DTO definition, e.g., `year`, `country`, `disasterType`)
*   **Responses:**
    *   `200 OK`: Lista de eventos históricos.
        *   Content: `Page<DisasterEventHistoryDto>`

### `PUT /api/history/{disNo}`

*   **Summary:** Atualiza um evento histórico de desastre
*   **Description:** Atualiza os dados de um evento histórico existente. Requer papel ADMIN.
*   **Security:** `bearerAuth`, `ROLE_ADMIN`
*   **Path Parameters:**
    *   `disNo` (string, required): DisNo (ID EMDAT) do evento a ser atualizado.
*   **Request Body:** `DisasterEventHistoryDto`
*   **Responses:**
    *   `200 OK`: Evento histórico atualizado com sucesso.
        *   Content: `DisasterEventHistoryDto`
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
*   **Responses:**
    *   `204 No Content`: Evento histórico deletado com sucesso.
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
*   **Responses:**
    *   `200 OK`: Simulação de despacho de drone bem-sucedida.
        *   Content: `SimulatedDroneResponseDto` (details depend on DTO)
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
*   **Request Body:** `SimulatedDisasterInputDto` (details depend on DTO)
*   **Responses:**
    *   `201 Created`: Simulação inicial criada com sucesso.
        *   Content: `SimulatedDisasterResponseDto` (details depend on DTO)
    *   `400 Bad Request`: Dados de entrada inválidos.
    *   `401 Unauthorized`: Não autenticado.
    *   `403 Forbidden`: Não autorizado.

### `POST /api/simulations/{simulationId}/predict`

*   **Summary:** Processa uma simulação com a IA
*   **Description:** Envia os dados da simulação para a API Python para obter a predição da categoria de fatalidade.
*   **Security:** `bearerAuth`, `ROLE_USER` or `ROLE_ADMIN`
*   **Path Parameters:**
    *   `simulationId` (long, required): ID da simulação a ser processada.
*   **Responses:**
    *   `200 OK`: Processamento com IA concluído.
        *   Content: `SimulatedDisasterResponseDto`
    *   `404 Not Found`: Simulação não encontrada.
    *   `500 Internal Server Error`: Erro ao comunicar com o serviço de IA ou erro interno.

### `GET /api/simulations/{simulationId}`

*   **Summary:** Busca uma simulação por ID
*   **Description:** Retorna os detalhes de uma simulação de desastre específica.
*   **Security:** `bearerAuth`, `ROLE_USER` or `ROLE_ADMIN`
*   **Path Parameters:**
    *   `simulationId` (long, required): ID da simulação.
*   **Responses:**
    *   `200 OK`: Simulação encontrada.
        *   Content: `SimulatedDisasterResponseDto`
    *   `404 Not Found`: Simulação não encontrada.

### `GET /api/simulations`

*   **Summary:** Lista simulações de desastre do usuário logado
*   **Description:** Retorna uma lista paginada de simulações criadas pelo usuário autenticado, com filtros opcionais.
*   **Security:** `bearerAuth`, `ROLE_USER` or `ROLE_ADMIN`
*   **Query Parameters (Pageable):**
    *   `page` (integer, optional, default: 0)
    *   `size` (integer, optional, default: 10)
    *   `sort` (string, optional, default: "requestTimestamp,desc")
*   **Query Parameters (Filter - `SimulatedDisasterFilterDto`):** (Specific filter fields depend on DTO definition)
*   **Responses:**
    *   `200 OK`: Lista de simulações.
        *   Content: `Page<SimulatedDisasterResponseDto>`

### `GET /api/simulations/admin/all`

*   **Summary:** Lista todas as simulações de desastre (Admin)
*   **Description:** Retorna uma lista paginada de todas as simulações no sistema, com filtros opcionais. Requer papel ADMIN.
*   **Security:** `bearerAuth`, `ROLE_ADMIN`
*   **Query Parameters (Pageable):**
    *   `page` (integer, optional, default: 0)
    *   `size` (integer, optional, default: 10)
    *   `sort` (string, optional, default: "requestTimestamp,desc")
*   **Query Parameters (Filter - `SimulatedDisasterFilterDto`):** (Specific filter fields depend on DTO definition)
*   **Responses:**
    *   `200 OK`: Lista de todas as simulações.
        *   Content: `Page<SimulatedDisasterResponseDto>`

---

*Note: DTO field details (e.g., for `DisasterEventHistoryDto`, `SimulatedDroneResponseDto`, `SimulatedDisasterInputDto`, `SimulatedDisasterResponseDto`, `SimulatedDisasterFilterDto`) are not fully expanded here and would be available in the respective DTO class definitions or the live Swagger UI.*

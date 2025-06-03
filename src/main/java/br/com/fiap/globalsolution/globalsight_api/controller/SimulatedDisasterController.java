package br.com.fiap.globalsolution.globalsight_api.controller;

import br.com.fiap.globalsolution.globalsight_api.dto.SimulatedDisasterFilterDto;
import br.com.fiap.globalsolution.globalsight_api.dto.SimulatedDisasterInputDto;
import br.com.fiap.globalsolution.globalsight_api.dto.SimulatedDisasterResponseDto;
import br.com.fiap.globalsolution.globalsight_api.service.SimulatedDisasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/simulations")
@Tag(name = "Simulações de Desastre", description = "Endpoints para criar e gerenciar simulações de desastre")
@SecurityRequirement(name = "bearerAuth") // Requer autenticação JWT para todos os endpoints neste controller
public class SimulatedDisasterController {

    private final SimulatedDisasterService simulatedDisasterService;

    @Autowired
    public SimulatedDisasterController(SimulatedDisasterService simulatedDisasterService) {
        this.simulatedDisasterService = simulatedDisasterService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Cria uma nova simulação de desastre (etapa inicial)",
            description = "Registra os dados de entrada de um desastre. A predição da IA deve ser acionada separadamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Simulação inicial criada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimulatedDisasterResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Não autorizado")
    })
    public ResponseEntity<SimulatedDisasterResponseDto> createInitialSimulation(
            @Valid @RequestBody SimulatedDisasterInputDto inputDto,
            Authentication authentication) {
        String username = authentication.getName();
        SimulatedDisasterResponseDto createdSimulation = simulatedDisasterService.createInitialSimulation(inputDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSimulation);
    }

    @PostMapping("/{simulationId}/predict")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Ou apenas o dono da simulação
    @Operation(summary = "Processa uma simulação com a IA",
            description = "Envia os dados da simulação para a API Python para obter a predição da categoria de fatalidade.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Processamento com IA concluído (verifique o status e a predição no corpo da resposta)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimulatedDisasterResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Simulação não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro ao comunicar com o serviço de IA ou erro interno")
    })
    public ResponseEntity<SimulatedDisasterResponseDto> processWithIA(
            @Parameter(description = "ID da simulação a ser processada") @PathVariable Long simulationId) {
        // Adicionar verificação de permissão (usuário dono ou admin) no service se necessário
        SimulatedDisasterResponseDto processedSimulation = simulatedDisasterService.processSimulationWithIA(simulationId);
        return ResponseEntity.ok(processedSimulation);
    }

    @GetMapping("/{simulationId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Busca uma simulação por ID",
            description = "Retorna os detalhes de uma simulação de desastre específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Simulação encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimulatedDisasterResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Simulação não encontrada")
    })
    public ResponseEntity<SimulatedDisasterResponseDto> getSimulationById(
            @Parameter(description = "ID da simulação") @PathVariable Long simulationId) {
        // Adicionar verificação de permissão no service
        SimulatedDisasterResponseDto simulation = simulatedDisasterService.getSimulationById(simulationId);
        return ResponseEntity.ok(simulation);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Lista simulações de desastre do usuário logado",
            description = "Retorna uma lista paginada de simulações criadas pelo usuário autenticado, com filtros opcionais.")
    public ResponseEntity<Page<SimulatedDisasterResponseDto>> getMySimulations(
            @ParameterObject @PageableDefault(size = 10, sort = "requestTimestamp,desc") Pageable pageable,
            @ParameterObject SimulatedDisasterFilterDto filterDto, // DTO para os parâmetros de filtro
            Authentication authentication) {
        String username = authentication.getName();
        Page<SimulatedDisasterResponseDto> simulations = simulatedDisasterService.getAllSimulations(pageable, filterDto, username);
        return ResponseEntity.ok(simulations);
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Lista todas as simulações de desastre (Admin)",
            description = "Retorna uma lista paginada de todas as simulações no sistema, com filtros opcionais. Requer papel ADMIN.")
    public ResponseEntity<Page<SimulatedDisasterResponseDto>> getAllSimulationsForAdmin(
            @ParameterObject @PageableDefault(size = 10, sort = "requestTimestamp,desc") Pageable pageable,
            @ParameterObject SimulatedDisasterFilterDto filterDto) {
        Page<SimulatedDisasterResponseDto> simulations = simulatedDisasterService.getAllSimulationsAdmin(pageable, filterDto);
        return ResponseEntity.ok(simulations);
    }
}
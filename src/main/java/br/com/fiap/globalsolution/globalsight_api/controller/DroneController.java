package br.com.fiap.globalsolution.globalsight_api.controller;

import br.com.fiap.globalsolution.globalsight_api.dto.SimulatedDroneResponseDto;
import br.com.fiap.globalsolution.globalsight_api.service.DroneSimulationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drone")
@Tag(name = "Simulação de Drone", description = "Endpoints para simular o despacho de drones em resposta a desastres")
@SecurityRequirement(name = "bearerAuth")
public class DroneController {

    private final DroneSimulationService droneSimulationService;

    @Autowired
    public DroneController(DroneSimulationService droneSimulationService) {
        this.droneSimulationService = droneSimulationService;
    }

    @PostMapping("/dispatch/{simulationId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Ou apenas o dono da simulação
    @Operation(summary = "Simula o despacho de drones para uma simulação",
            description = "Com base na predição de risco de uma simulação, gera e retorna uma resposta simulada de despacho de drone.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Simulação de despacho de drone bem-sucedida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimulatedDroneResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Simulação não encontrada"),
            @ApiResponse(responseCode = "400", description = "Não é possível despachar drone (ex: predição da IA não disponível)")
    })
    public ResponseEntity<SimulatedDroneResponseDto> dispatchDrone(
            @Parameter(description = "ID da simulação de desastre para a qual o drone será despachado") @PathVariable Long simulationId) {
        // Adicionar verificação de permissão no service se necessário
        SimulatedDroneResponseDto droneResponse = droneSimulationService.simulateDroneDispatch(simulationId);
        return ResponseEntity.ok(droneResponse);
    }
}
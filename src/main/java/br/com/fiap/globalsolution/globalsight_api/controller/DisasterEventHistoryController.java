package br.com.fiap.globalsolution.globalsight_api.controller;

import br.com.fiap.globalsolution.globalsight_api.dto.DisasterEventHistoryDto;
import br.com.fiap.globalsolution.globalsight_api.dto.DisasterEventHistoryFilterDto;
import br.com.fiap.globalsolution.globalsight_api.service.DisasterEventHistoryService;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
@Tag(name = "Histórico de Desastres", description = "Endpoints para gerenciar e consultar dados históricos de desastres (EMDAT)")
public class DisasterEventHistoryController {

    private final DisasterEventHistoryService historyService;

    @Autowired
    public DisasterEventHistoryController(DisasterEventHistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')") // Apenas ADMIN pode criar novos registros históricos
    @Operation(summary = "Cria um novo evento histórico de desastre",
            description = "Adiciona um novo registro ao histórico de desastres. Requer papel ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento histórico criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DisasterEventHistoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou evento já existe"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Não autorizado (requer ADMIN)")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DisasterEventHistoryDto> createHistoryEvent(
            @Valid @RequestBody DisasterEventHistoryDto eventDto) {
        DisasterEventHistoryDto createdEvent = historyService.createHistoryEvent(eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @GetMapping("/{disNo}")
    // @PreAuthorize("isAuthenticated()") // Ou pode ser público dependendo dos requisitos
    @Operation(summary = "Busca um evento histórico por DisNo (ID)",
            description = "Retorna os detalhes de um evento histórico específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento histórico encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DisasterEventHistoryDto.class))),
            @ApiResponse(responseCode = "404", description = "Evento histórico não encontrado")
    })
    public ResponseEntity<DisasterEventHistoryDto> getHistoryEventById(
            @Parameter(description = "DisNo (ID EMDAT) do evento histórico") @PathVariable String disNo) {
        DisasterEventHistoryDto event = historyService.getHistoryEventById(disNo);
        return ResponseEntity.ok(event);
    }

    @GetMapping
    // @PreAuthorize("isAuthenticated()") // Ou pode ser público
    @Operation(summary = "Lista eventos históricos de desastre",
            description = "Retorna uma lista paginada de eventos históricos, com filtros opcionais.")
    public ResponseEntity<Page<DisasterEventHistoryDto>> getAllHistoryEvents(
            @ParameterObject @PageableDefault(size = 10, sort = "yearEvent", direction = Sort.Direction.DESC) Pageable pageable,
            @ParameterObject DisasterEventHistoryFilterDto filterDto) {
        Page<DisasterEventHistoryDto> events = historyService.getAllHistoryEvents(pageable, filterDto);
        return ResponseEntity.ok(events);
    }

    @PutMapping("/{disNo}")
    @PreAuthorize("hasRole('USER')") // Apenas ADMIN pode atualizar registros históricos
    @Operation(summary = "Atualiza um evento histórico de desastre",
            description = "Atualiza os dados de um evento histórico existente. Requer papel ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento histórico atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DisasterEventHistoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Evento histórico não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Não autorizado (requer ADMIN)")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DisasterEventHistoryDto> updateHistoryEvent(
            @Parameter(description = "DisNo (ID EMDAT) do evento a ser atualizado") @PathVariable String disNo,
            @Valid @RequestBody DisasterEventHistoryDto eventDto) {
        DisasterEventHistoryDto updatedEvent = historyService.updateHistoryEvent(disNo, eventDto);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{disNo}")
    @PreAuthorize("hasRole('USER')") // Apenas ADMIN pode deletar registros históricos
    @Operation(summary = "Deleta um evento histórico de desastre",
            description = "Remove um evento histórico do sistema. Requer papel ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evento histórico deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Evento histórico não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Não autorizado (requer ADMIN)")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteHistoryEvent(
            @Parameter(description = "DisNo (ID EMDAT) do evento a ser deletado") @PathVariable String disNo) {
        historyService.deleteHistoryEvent(disNo);
        return ResponseEntity.noContent().build();
    }
}
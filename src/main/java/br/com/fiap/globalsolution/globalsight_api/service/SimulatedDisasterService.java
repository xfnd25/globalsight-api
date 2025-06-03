package br.com.fiap.globalsolution.globalsight_api.service;

import br.com.fiap.globalsolution.globalsight_api.dto.SimulatedDisasterFilterDto;
import br.com.fiap.globalsolution.globalsight_api.dto.SimulatedDisasterInputDto;
import br.com.fiap.globalsolution.globalsight_api.dto.SimulatedDisasterResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SimulatedDisasterService {
    SimulatedDisasterResponseDto createInitialSimulation(SimulatedDisasterInputDto inputDto, String authenticatedUsername);
    SimulatedDisasterResponseDto processSimulationWithIA(Long simulationId);
    SimulatedDisasterResponseDto getSimulationById(Long id);
    Page<SimulatedDisasterResponseDto> getAllSimulations(Pageable pageable, SimulatedDisasterFilterDto filterDto, String authenticatedUsername);
    // Opcional: permitir que um admin veja todas as simulações, independentemente do usuário.
    Page<SimulatedDisasterResponseDto> getAllSimulationsAdmin(Pageable pageable, SimulatedDisasterFilterDto filterDto);
}
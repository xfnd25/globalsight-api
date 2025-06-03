package br.com.fiap.globalsolution.globalsight_api.service;

import br.com.fiap.globalsolution.globalsight_api.dto.SimulatedDroneResponseDto;

public interface DroneSimulationService {
    SimulatedDroneResponseDto simulateDroneDispatch(Long simulationId);
}
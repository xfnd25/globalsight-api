package br.com.fiap.globalsolution.globalsight_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulatedDroneResponseDto {
    private Long simulationId; // ID da SimulatedDisasterResponse original
    private String disasterType; // Tipo de desastre da simulação original
    private String predictedRiskCategory; // A predictedFatalitiesCategory
    private int dronesDeployed;
    private String droneTasksAssigned; // Ex: "Roteamento de internet, Mapeamento da área"
    private double targetLatitude;
    private double targetLongitude;
    private String estimatedCoverageAreaKm2; // Ex: "5 km²"
    private String simulatedCo2Level; // Ex: "Normal", "Elevated", "Critical"
    private String simulatedConnectivityStatus; // Ex: "Wi-Fi Mesh Network Active: Strong Signal"
    private String mapLink; // Ex: "http://maps.example.com/disaster_area?sim_id=123&lat=X&lon=Y"
    private String message; // Mensagem geral, ex: "Drone dispatch simulation initiated."
}
package br.com.fiap.globalsolution.globalsight_api.dto;

import br.com.fiap.globalsolution.globalsight_api.entity.SimulationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulatedDisasterResponseDto {
    private Long id;

    // Input fields
    private Integer inputYear;
    private Integer inputStartMonth;
    private Integer inputStartDay;
    private Integer inputEndYear;
    private Integer inputEndMonth;
    private Integer inputEndDay;
    private String inputDisasterGroup;
    private String inputDisasterSubgroup;
    private String inputDisasterType;
    private String inputContinent;
    private String inputRegion;
    private String inputDisMagScale;
    private Double inputDisMagValue;
    private Double inputLatitude;
    private Double inputLongitude;
    private Double inputCpi;

    // IA Prediction
    private String predictedFatalitiesCategory;
    private LocalDateTime iaPredictionTimestamp;

    // Drone Simulation
    private Integer simulatedDronesDeployed;
    private String simulatedDroneTasks;

    // Metadata
    private LocalDateTime requestTimestamp;
    private SimulationStatus status;
    private String errorMessage;
    private UserDetailsDto user; // Detalhes do usuário que criou a simulação
}
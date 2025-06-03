package br.com.fiap.globalsolution.globalsight_api.dto;

import br.com.fiap.globalsolution.globalsight_api.entity.SimulationStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SimulatedDisasterFilterDto {
    private String disasterType;
    private String continent;
    private String region;
    private SimulationStatus status;
    private Integer year; // Filtro por ano do desastre (inputYear)

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDateFrom; // Data de início da requisição da simulação

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDateTo; // Data de fim da requisição da simulação


}
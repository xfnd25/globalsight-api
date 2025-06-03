package br.com.fiap.globalsolution.globalsight_api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SimulatedDisasterInputDto {

    @NotNull(message = "Input Year cannot be null")
    @Min(value = 1900, message = "Year must be after 1900")
    @Max(value = 2200, message = "Year must be realistic (e.g., up to 2200)")
    private Integer inputYear;

    @Min(value = 1, message = "Start month must be between 1 and 12")
    @Max(value = 12, message = "Start month must be between 1 and 12")
    private Integer inputStartMonth; // Pode ser nulo se DisasterDurationDays for calculado de outra forma ou imputado

    @Min(value = 1, message = "Start day must be between 1 and 31")
    @Max(value = 31, message = "Start day must be between 1 and 31")
    private Integer inputStartDay; // Pode ser nulo

    @Min(value = 1900, message = "End year must be after 1900")
    @Max(value = 2200, message = "End year must be realistic")
    private Integer inputEndYear; // Pode ser nulo

    @Min(value = 1, message = "End month must be between 1 and 12")
    @Max(value = 12, message = "End month must be between 1 and 12")
    private Integer inputEndMonth; // Pode ser nulo

    @Min(value = 1, message = "End day must be between 1 and 31")
    @Max(value = 31, message = "End day must be between 1 and 31")
    private Integer inputEndDay; // Pode ser nulo

    @Size(max = 100, message = "Disaster group maximum length is 100 characters")
    private String inputDisasterGroup;

    @Size(max = 100, message = "Disaster subgroup maximum length is 100 characters")
    private String inputDisasterSubgroup;

    @NotBlank(message = "Disaster type cannot be blank") // Tipo de desastre é crucial para o modelo
    @Size(max = 100, message = "Disaster type maximum length is 100 characters")
    private String inputDisasterType;

    @Size(max = 100, message = "Continent maximum length is 100 characters")
    private String inputContinent; // Importante para o modelo

    @Size(max = 100, message = "Region maximum length is 100 characters")
    private String inputRegion; // Importante para o modelo

    @Size(max = 50, message = "Disaster magnitude scale maximum length is 50 characters")
    private String inputDisMagScale; // Pode ser nulo, modelo trata 'Unknown_Scale'

    private Double inputDisMagValue; // Pode ser nulo, modelo faz imputação

    @NotNull(message = "Latitude cannot be null")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90.0 and 90.0")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90.0 and 90.0")
    private Double inputLatitude;

    @NotNull(message = "Longitude cannot be null")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180.0 and 180.0")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180.0 and 180.0")
    private Double inputLongitude;

    private Double inputCpi; // Pode ser nulo, modelo faz imputação
}
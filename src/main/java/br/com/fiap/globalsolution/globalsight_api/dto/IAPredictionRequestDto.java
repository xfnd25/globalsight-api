package br.com.fiap.globalsolution.globalsight_api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO para enviar os dados de entrada de uma simulação de desastre
 * para o serviço de predição da IA (API Python).
 * Este DTO contém os campos brutos que o script Python usará para
 * realizar o pré-processamento e a engenharia de features.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IAPredictionRequestDto {

    // Os nomes dos campos aqui devem corresponder às chaves JSON
    // que a sua API Python espera receber.
    // Se a API Python for usar diretamente os nomes "inputYear", "inputStartMonth", etc.,
    // então este DTO está correto.
    // Se a API Python esperar nomes como "Year", "Start Month", etc.,
    // você precisará fazer um mapeamento na API Java antes de enviar,
    // ou a API Python deverá mapear os nomes ao receber.
    // Para consistência com SimulatedDisasterInputDto, manteremos os prefixos "input".
    // A API Python fará o mapeamento interno para os nomes de coluna do dataset original.

    @NotNull(message = "Input Year cannot be null")
    @Min(value = 1900, message = "Year must be after 1900")
    @Max(value = 2200, message = "Year must be realistic (e.g., up to 2200)")
    private Integer inputYear;

    @Min(value = 1, message = "Start month must be between 1 and 12")
    @Max(value = 12, message = "Start month must be between 1 and 12")
    private Integer inputStartMonth;

    @Min(value = 1, message = "Start day must be between 1 and 31")
    @Max(value = 31, message = "Start day must be between 1 and 31")
    private Integer inputStartDay;

    @Min(value = 1900, message = "End year must be after 1900")
    @Max(value = 2200, message = "End year must be realistic")
    private Integer inputEndYear;

    @Min(value = 1, message = "End month must be between 1 and 12")
    @Max(value = 12, message = "End month must be between 1 and 12")
    private Integer inputEndMonth;

    @Min(value = 1, message = "End day must be between 1 and 31")
    @Max(value = 31, message = "End day must be between 1 and 31")
    private Integer inputEndDay;

    @Size(max = 100, message = "Disaster group maximum length is 100 characters")
    private String inputDisasterGroup;

    @Size(max = 100, message = "Disaster subgroup maximum length is 100 characters")
    private String inputDisasterSubgroup;

    @NotBlank(message = "Disaster type cannot be blank")
    @Size(max = 100, message = "Disaster type maximum length is 100 characters")
    private String inputDisasterType;

    @Size(max = 100, message = "Continent maximum length is 100 characters")
    private String inputContinent;

    @Size(max = 100, message = "Region maximum length is 100 characters")
    private String inputRegion;

    @Size(max = 50, message = "Disaster magnitude scale maximum length is 50 characters")
    private String inputDisMagScale;

    private Double inputDisMagValue;

    @NotNull(message = "Latitude cannot be null")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90.0 and 90.0")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90.0 and 90.0")
    private Double inputLatitude;

    @NotNull(message = "Longitude cannot be null")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180.0 and 180.0")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180.0 and 180.0")
    private Double inputLongitude;

    private Double inputCpi;
}
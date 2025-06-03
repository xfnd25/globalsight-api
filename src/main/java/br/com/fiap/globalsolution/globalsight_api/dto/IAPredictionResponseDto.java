package br.com.fiap.globalsolution.globalsight_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IAPredictionResponseDto {
    private String predictedFatalitiesCategory;
    // Opcional: adicionar outros campos se a API Python retornar,
    // como probabilidades de classe, versão do modelo, etc.
}
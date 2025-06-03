package br.com.fiap.globalsolution.globalsight_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private String tokenType = "Bearer";
    private UserDetailsDto userDetails;
}
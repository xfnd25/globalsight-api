package br.com.fiap.globalsolution.globalsight_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto {
    private Long id;
    private String username;
    private boolean enabled;
    private List<String> roles; // Lista de nomes dos papéis (ex: "ROLE_USER")
}
package br.com.fiap.globalsolution.globalsight_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    // Para produção, considere regras de complexidade de senha mais robustas.
    private String password;

    @NotBlank(message = "Email cannot be blank")
    @Size(max = 100, message = "Email must be up to 100 characters")
    // Adicionar @Email para validação de formato de email
    private String email;

    @NotBlank(message = "Complete name cannot be blank")
    @Size(max = 100, message = "Complete name must be up to 100 characters")
    private String completeName;

    // Opcional: adicionar campo para roles se o usuário puder escolher ao se registrar,
    // ou atribuir um role padrão no service.
}
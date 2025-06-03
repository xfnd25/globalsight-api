package br.com.fiap.globalsolution.globalsight_api.controller;

import br.com.fiap.globalsolution.globalsight_api.dto.AuthRequestDto;
import br.com.fiap.globalsolution.globalsight_api.dto.AuthResponseDto;
import br.com.fiap.globalsolution.globalsight_api.dto.UserDetailsDto;
import br.com.fiap.globalsolution.globalsight_api.dto.UserRegistrationDto;
import br.com.fiap.globalsolution.globalsight_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para registro e login de usuários")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registra um novo usuário", description = "Cria um novo usuário no sistema com o papel padrão ROLE_USER.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDetailsDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou nome de usuário já existe")
    })
    public ResponseEntity<UserDetailsDto> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        // A lógica para tratar EntityExistsException (usuário já existe) pode ser feita
        // em um @ControllerAdvice global para retornar um HTTP 400 ou 409 mais específico.
        UserDetailsDto userDetails = authService.registerUser(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDetails);
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica um usuário", description = "Autentica um usuário e retorna um token JWT se as credenciais forem válidas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido, token JWT retornado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Requisição de login inválida"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody AuthRequestDto authRequestDto) {
        AuthResponseDto authResponse = authService.loginUser(authRequestDto);
        return ResponseEntity.ok(authResponse);
    }
}
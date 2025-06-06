package br.com.fiap.globalsolution.globalsight_api.exception;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// ... other existing imports ...
import br.com.fiap.globalsolution.globalsight_api.exception.UserAlreadyExistsException; // Add this import

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // DTO para a resposta de erro padronizada
    public record ApiErrorResponse(
            LocalDateTime timestamp,
            int status,
            String error,
            String message,
            String path, // Você pode adicionar o path da requisição se desejar
            List<String> details // Para erros de validação
    ) {
        public ApiErrorResponse(HttpStatus httpStatus, String message, String path, List<String> details) {
            this(LocalDateTime.now(), httpStatus.value(), httpStatus.getReasonPhrase(), message, path, details);
        }
        public ApiErrorResponse(HttpStatus httpStatus, String message, String path) {
            this(LocalDateTime.now(), httpStatus.value(), httpStatus.getReasonPhrase(), message, path, null);
        }
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException ex, jakarta.servlet.http.HttpServletRequest request) {
        logger.warn("Acesso negado: {} para o path: {}", ex.getMessage(), request.getRequestURI());
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.FORBIDDEN,
                "Acesso negado. Você não tem permissão para acessar este recurso.",
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class) // Para falhas de autenticação (token inválido, ausente) que resultam em 401
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(AuthenticationException ex, jakarta.servlet.http.HttpServletRequest request) {
        logger.warn("Falha na autenticação: {} para o path: {}", ex.getMessage(), request.getRequestURI());
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Falha na autenticação. Verifique suas credenciais ou token.",
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex, jakarta.servlet.http.HttpServletRequest request) {
        logger.warn("Recurso não encontrado: {} para o path: {}", ex.getMessage(), request.getRequestURI());
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(), // Mensagem da exceção (ex: "Evento histórico com DisNo X não encontrado.")
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex, jakarta.servlet.http.HttpServletRequest request) {
        logger.error("Erro de integridade de dados: {} para o path: {}", ex.getMessage(), request.getRequestURI(), ex);
        String message = "Erro de integridade dos dados. Pode ser um valor duplicado ou uma restrição violada.";
        // Tenta extrair uma mensagem mais específica se for uma constraint violation conhecida
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException cve) {
            message = "Violação de restrição do banco de dados: " + cve.getConstraintName() + " (" + cve.getSQLException().getMessage() + ")";
        }
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.CONFLICT, // Ou BAD_REQUEST dependendo da natureza
                message,
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, jakarta.servlet.http.HttpServletRequest request) {
        logger.warn("Argumento ilegal: {} para o path: {}", ex.getMessage(), request.getRequestURI());
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalStateException(IllegalStateException ex, jakarta.servlet.http.HttpServletRequest request) {
        logger.warn("Operação ilegal ou estado inválido: {} para o path: {}", ex.getMessage(), request.getRequestURI());
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST, // Or CONFLICT (409) depending on typical use of IllegalStateException
                ex.getMessage(), // Use the specific message from the exception
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, jakarta.servlet.http.HttpServletRequest request) {
        logger.warn("Erro de validação: {} para o path: {}", ex.getMessage(), request.getRequestURI());
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Erro de validação nos dados de entrada.",
                request.getRequestURI(),
                details
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex, jakarta.servlet.http.HttpServletRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(), // Message from the exception itself
                request.getRequestURI(),
                null // Or provide specific details if needed
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // Um handler genérico para outras exceções não tratadas especificamente
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex, jakarta.servlet.http.HttpServletRequest request) {
        logger.error("Erro inesperado: {} para o path: {}", ex.getMessage(), request.getRequestURI(), ex);
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro inesperado no servidor.",
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

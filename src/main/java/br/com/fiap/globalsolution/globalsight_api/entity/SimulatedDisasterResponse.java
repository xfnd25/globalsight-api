package br.com.fiap.globalsolution.globalsight_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "simulated_disaster_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulatedDisasterResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Características Base do Desastre (Input do Usuário/App) ---
    // Campos que serão enviados para a API Python.
    // Removi @NotNull da maioria, assumindo que a API Python/modelo fará imputação
    // ou que a validação no DTO será mais específica.

    private Integer inputYear;
    private Integer inputStartMonth;
    private Integer inputStartDay;
    private Integer inputEndYear;
    private Integer inputEndMonth;
    private Integer inputEndDay;

    @Column(length = 100)
    private String inputDisasterGroup;

    @Column(length = 100)
    private String inputDisasterSubgroup;

    @Column(length = 100)
    private String inputDisasterType; // Provavelmente um dos campos mais importantes

    @Column(length = 100)
    private String inputContinent;

    @Column(length = 100)
    private String inputRegion;

    @Column(length = 50)
    private String inputDisMagScale;

    private Double inputDisMagValue;

    // Coordenadas são essenciais para o drone e podem ser para o modelo
    @NotNull // Mantendo NotNull aqui pois são cruciais
    private Double inputLatitude;

    @NotNull // Mantendo NotNull aqui pois são cruciais
    private Double inputLongitude;

    private Double inputCpi;

    // --- Resultado da Predição da IA (retornado pelo serviço Python) ---
    @Column(length = 100)
    private String predictedFatalitiesCategory;
    private LocalDateTime iaPredictionTimestamp; // Quando a predição foi recebida

    // --- Detalhes da Simulação da Resposta do Drone (definidos pela API Java após a predição) ---
    private Integer simulatedDronesDeployed;

    @Lob // Para textos potencialmente longos
    @Column(columnDefinition = "TEXT")
    private String simulatedDroneTasks;

    // --- Metadados da Requisição e Status ---
    @Column(nullable = false)
    private LocalDateTime requestTimestamp; // Quando a simulação foi criada na API Java

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private SimulationStatus status;

    @Column(columnDefinition = "TEXT")
    private String errorMessage; // Para armazenar mensagens de erro (ex: da IA)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Uma simulação deve pertencer a um usuário
    private User user;
}
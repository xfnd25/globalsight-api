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
    // Estas são as informações que o serviço de IA Python usará para
    // recriar todas as features necessárias para o modelo.

    @NotNull
    private Integer inputYear;

    @NotNull
    private Integer inputStartMonth;

    // Para calcular DisasterDurationDays, precisamos das datas de início e fim completas
    // ou da duração já calculada. Vamos pedir os componentes da data.
    @NotNull
    private Integer inputStartDay;

    @NotNull
    private Integer inputEndYear;

    @NotNull
    private Integer inputEndMonth;

    @NotNull
    private Integer inputEndDay;

    // Features Categóricas Base
    @Column(length = 100)
    private String inputDisasterGroup;

    @Column(length = 100)
    private String inputDisasterSubgroup;

    @Column(length = 100)
    private String inputDisasterType;

    @Column(length = 100)
    private String inputContinent;

    @Column(length = 100)
    private String inputRegion;

    @Column(length = 50)
    private String inputDisMagScale; // Usuário informa a escala original

    // Features Numéricas Base
    private Double inputDisMagValue; // Usuário informa o valor da magnitude (pode ser nulo)

    @NotNull
    private Double inputLatitude; // Coordenada X para o drone e feature para IA

    @NotNull
    private Double inputLongitude; // Coordenada Y para o drone e feature para IA

    private Double inputCpi;

    // A API Java NÃO precisará enviar as features DMV_*, DisMagValue_IsMissing,
    // DisasterDurationDays (calculada), nem as features de interação, nem as OHE.
    // O serviço Python de IA fará essa engenharia a partir dos inputs base acima.

    // --- Resultado da Predição da IA (retornado pelo serviço Python) ---
    @Column(length = 100)
    private String predictedFatalitiesCategory; // Ex: "3_Alta (101-1000)"

    // --- Detalhes da Simulação da Resposta do Drone (definidos pela API Java após a predição) ---
    private Integer simulatedDronesDeployed;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String simulatedDroneTasks; // Ex: "Roteamento de internet, Mapeamento da área, Medição de CO2"

    @Column(nullable = false)
    private LocalDateTime requestTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
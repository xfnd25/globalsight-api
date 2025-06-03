package br.com.fiap.globalsolution.globalsight_api.service;

import br.com.fiap.globalsolution.globalsight_api.dto.SimulatedDroneResponseDto;
import br.com.fiap.globalsolution.globalsight_api.entity.SimulatedDisasterResponse;
import br.com.fiap.globalsolution.globalsight_api.entity.SimulationStatus;
import br.com.fiap.globalsolution.globalsight_api.repository.SimulatedDisasterResponseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DroneSimulationServiceImpl implements DroneSimulationService {

    private static final Logger logger = LoggerFactory.getLogger(DroneSimulationServiceImpl.class);

    private final SimulatedDisasterResponseRepository simulationRepository;

    @Autowired
    public DroneSimulationServiceImpl(SimulatedDisasterResponseRepository simulationRepository) {
        this.simulationRepository = simulationRepository;
    }

    @Override
    @Transactional
    public SimulatedDroneResponseDto simulateDroneDispatch(Long simulationId) {
        SimulatedDisasterResponse simulation = simulationRepository.findById(simulationId)
                .orElseThrow(() -> new EntityNotFoundException("Simulação com ID " + simulationId + " não encontrada para despacho de drone."));

        if (simulation.getPredictedFatalitiesCategory() == null ||
                simulation.getStatus() != SimulationStatus.IA_PREDICTION_SUCCESS) {
            logger.warn("Não é possível simular despacho de drone para simulação ID: {}. Predição da IA não disponível ou status incorreto ({}).",
                    simulationId, simulation.getStatus());
            throw new IllegalStateException("Predição da IA não está disponível ou a simulação não está no status correto para despacho de drone.");
        }

        int dronesDeployed = 0;
        String droneTasks = "";
        String co2Level = "Normal";
        String connectivity = "Wi-Fi Mesh Network Pending Activation";
        String coverage = "1 km²";

        // Lógica para determinar drones e tarefas baseado na categoria de fatalidade
        // Estes labels devem ser exatamente os mesmos que a IA retorna.
        String riskCategory = simulation.getPredictedFatalitiesCategory();

        if (riskCategory.startsWith("4_Muito Alta")) { // "4_Muito Alta (>1000)"
            dronesDeployed = 5;
            droneTasks = "Roteamento de internet de emergência, Mapeamento 3D da área, Detecção de CO2 elevado, Suporte para busca e resgate, Entrega de kits básicos.";
            co2Level = "Potentially Elevated/Critical";
            connectivity = "Wi-Fi Mesh Network Active - High Priority";
            coverage = "10 km²";
        } else if (riskCategory.startsWith("3_Alta")) { // "3_Alta (101-1000)"
            dronesDeployed = 3;
            droneTasks = "Roteamento de internet, Mapeamento 2D da área, Detecção de CO2.";
            co2Level = "Potentially Elevated";
            connectivity = "Wi-Fi Mesh Network Active - Medium Priority";
            coverage = "5 km²";
        } else if (riskCategory.startsWith("2_Media")) { // "2_Media (11-100)"
            dronesDeployed = 2;
            droneTasks = "Mapeamento da área, Monitoramento visual.";
            connectivity = "Wi-Fi Mesh Network Active - Low Priority";
            coverage = "2 km²";
        } else if (riskCategory.startsWith("1_Baixa")) { // "1_Baixa (1-10)"
            dronesDeployed = 1;
            droneTasks = "Monitoramento visual da área.";
            coverage = "1 km²";
        } else { // "0_Nenhuma" ou categoria desconhecida
            dronesDeployed = 0;
            droneTasks = "Nenhum drone despachado, risco considerado mínimo.";
            co2Level = "Normal";
            connectivity = "Standard Cellular Network Available";
        }

        simulation.setSimulatedDronesDeployed(dronesDeployed);
        simulation.setSimulatedDroneTasks(droneTasks);
        simulation.setStatus(SimulationStatus.DRONE_SIMULATION_READY); // ou COMPLETED se este for o último passo
        simulationRepository.save(simulation);
        logger.info("Simulação de despacho de drone concluída para simulação ID: {}. Drones: {}, Tarefas: {}", simulationId, dronesDeployed, droneTasks);

        return new SimulatedDroneResponseDto(
                simulation.getId(),
                simulation.getInputDisasterType(),
                riskCategory,
                dronesDeployed,
                droneTasks,
                simulation.getInputLatitude(),
                simulation.getInputLongitude(),
                coverage,
                co2Level,
                connectivity,
                "http://maps.example.com/disaster?sim_id=" + simulationId + "&lat=" + simulation.getInputLatitude() + "&lon=" + simulation.getInputLongitude(), // Link de mapa simulado
                "Simulação de despacho de drone para " + riskCategory + " concluída."
        );
    }
}
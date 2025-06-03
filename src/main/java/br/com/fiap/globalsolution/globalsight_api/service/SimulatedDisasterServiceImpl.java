package br.com.fiap.globalsolution.globalsight_api.service;

import br.com.fiap.globalsolution.globalsight_api.dto.IAPredictionResponseDto;
import br.com.fiap.globalsolution.globalsight_api.dto.SimulatedDisasterFilterDto;
import br.com.fiap.globalsolution.globalsight_api.dto.SimulatedDisasterInputDto;
import br.com.fiap.globalsolution.globalsight_api.dto.SimulatedDisasterResponseDto;
import br.com.fiap.globalsolution.globalsight_api.dto.UserDetailsDto;
import br.com.fiap.globalsolution.globalsight_api.entity.Role;
import br.com.fiap.globalsolution.globalsight_api.entity.SimulatedDisasterResponse;
import br.com.fiap.globalsolution.globalsight_api.entity.SimulationStatus;
import br.com.fiap.globalsolution.globalsight_api.entity.User;
import br.com.fiap.globalsolution.globalsight_api.repository.SimulatedDisasterResponseRepository;
import br.com.fiap.globalsolution.globalsight_api.repository.UserRepository;
import br.com.fiap.globalsolution.globalsight_api.service.filters.SimulatedDisasterSpecification; // Criaremos esta classe
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate; // Ou WebClient para chamadas não bloqueantes

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class SimulatedDisasterServiceImpl implements SimulatedDisasterService {

    private static final Logger logger = LoggerFactory.getLogger(SimulatedDisasterServiceImpl.class);

    private final SimulatedDisasterResponseRepository simulationRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate; // Para chamadas HTTP à API Python

    @Value("${python.ia.service.url}") // Injetar a URL da API Python do application.properties
    private String pythonApiServiceUrl;

    @Autowired
    public SimulatedDisasterServiceImpl(SimulatedDisasterResponseRepository simulationRepository,
                                        UserRepository userRepository,
                                        RestTemplate restTemplate) {
        this.simulationRepository = simulationRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public SimulatedDisasterResponseDto createInitialSimulation(SimulatedDisasterInputDto inputDto, String authenticatedUsername) {
        User currentUser = userRepository.findByUsername(authenticatedUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário " + authenticatedUsername + " não encontrado."));

        SimulatedDisasterResponse simulation = new SimulatedDisasterResponse();
        BeanUtils.copyProperties(inputDto, simulation); // Copia campos com nomes correspondentes

        simulation.setUser(currentUser);
        simulation.setRequestTimestamp(LocalDateTime.now());
        simulation.setStatus(SimulationStatus.PENDING_USER_INPUT); // Ou PENDING_IA se for processar imediatamente

        SimulatedDisasterResponse savedSimulation = simulationRepository.save(simulation);
        logger.info("Simulação inicial criada com ID: {}", savedSimulation.getId());
        return convertToDto(savedSimulation);
    }

    @Override
    @Transactional
    public SimulatedDisasterResponseDto processSimulationWithIA(Long simulationId) {
        SimulatedDisasterResponse simulation = simulationRepository.findById(simulationId)
                .orElseThrow(() -> new EntityNotFoundException("Simulação com ID " + simulationId + " não encontrada."));

        if (simulation.getStatus() == SimulationStatus.IA_PREDICTION_SUCCESS || simulation.getStatus() == SimulationStatus.DRONE_SIMULATION_READY || simulation.getStatus() == SimulationStatus.COMPLETED) {
            logger.warn("Simulação {} já foi processada pela IA ou completada. Status atual: {}", simulationId, simulation.getStatus());
            // Poderia lançar uma exceção ou apenas retornar o estado atual
            // throw new IllegalStateException("Simulação já processada pela IA.");
            return convertToDto(simulation);
        }

        simulation.setStatus(SimulationStatus.PROCESSING_IA);
        simulationRepository.save(simulation); // Salva o status de processamento

        // Prepara a requisição para a API Python
        // O SimulatedDisasterInputDto pode ser usado diretamente como corpo da requisição
        // se a API Python esperar os campos com prefixo "input".
        // Ou, crie um IAPredictionRequestDto específico se necessário.
        SimulatedDisasterInputDto iaRequestDto = new SimulatedDisasterInputDto();
        BeanUtils.copyProperties(simulation, iaRequestDto); // Copia os campos "input*" da entidade para o DTO

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SimulatedDisasterInputDto> requestEntity = new HttpEntity<>(iaRequestDto, headers);

        try {
            logger.info("Enviando requisição para API Python para simulação ID: {}. URL: {}", simulationId, pythonApiServiceUrl);
            ResponseEntity<IAPredictionResponseDto> responseEntity = restTemplate.postForEntity(
                    pythonApiServiceUrl,
                    requestEntity,
                    IAPredictionResponseDto.class
            );

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                IAPredictionResponseDto predictionResponse = responseEntity.getBody();
                simulation.setPredictedFatalitiesCategory(predictionResponse.getPredictedFatalitiesCategory());
                simulation.setIaPredictionTimestamp(LocalDateTime.now());
                simulation.setStatus(SimulationStatus.IA_PREDICTION_SUCCESS);
                simulation.setErrorMessage(null); // Limpa erros anteriores
                logger.info("Predição da IA recebida com sucesso para simulação ID: {}. Categoria: {}", simulationId, predictionResponse.getPredictedFatalitiesCategory());
            } else {
                simulation.setStatus(SimulationStatus.IA_PREDICTION_FAILED);
                simulation.setErrorMessage("API Python retornou status não esperado: " + responseEntity.getStatusCode());
                logger.error("API Python retornou status não esperado {} para simulação ID: {}", responseEntity.getStatusCode(), simulationId);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            simulation.setStatus(SimulationStatus.IA_PREDICTION_FAILED);
            simulation.setErrorMessage("Erro na API Python: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            logger.error("Erro ao chamar API Python para simulação ID: {}. Status: {}, Response: {}", simulationId, e.getStatusCode(), e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            simulation.setStatus(SimulationStatus.IA_PREDICTION_FAILED);
            simulation.setErrorMessage("Erro de comunicação com a API Python: " + e.getMessage());
            logger.error("Erro de comunicação ao chamar API Python para simulação ID: {}: ", simulationId, e);
        }

        SimulatedDisasterResponse updatedSimulation = simulationRepository.save(simulation);
        return convertToDto(updatedSimulation);
    }


    @Override
    public SimulatedDisasterResponseDto getSimulationById(Long id) {
        SimulatedDisasterResponse simulation = simulationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Simulação com ID " + id + " não encontrada."));
        return convertToDto(simulation);
    }

    @Override
    public Page<SimulatedDisasterResponseDto> getAllSimulations(Pageable pageable, SimulatedDisasterFilterDto filterDto, String authenticatedUsername) {
        User currentUser = userRepository.findByUsername(authenticatedUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário " + authenticatedUsername + " não encontrado."));

        Specification<SimulatedDisasterResponse> spec = Specification
                .where(SimulatedDisasterSpecification.hasUser(currentUser))
                .and(SimulatedDisasterSpecification.createSpecification(filterDto));

        return simulationRepository.findAll(spec, pageable).map(this::convertToDto);
    }

    @Override
    public Page<SimulatedDisasterResponseDto> getAllSimulationsAdmin(Pageable pageable, SimulatedDisasterFilterDto filterDto) {
        Specification<SimulatedDisasterResponse> spec = SimulatedDisasterSpecification.createSpecification(filterDto);
        return simulationRepository.findAll(spec, pageable).map(this::convertToDto);
    }

    private SimulatedDisasterResponseDto convertToDto(SimulatedDisasterResponse simulation) {
        SimulatedDisasterResponseDto dto = new SimulatedDisasterResponseDto();
        BeanUtils.copyProperties(simulation, dto); // Copia campos correspondentes

        // Mapeia o usuário para UserDetailsDto
        if (simulation.getUser() != null) {
            User user = simulation.getUser();
            UserDetailsDto userDto = new UserDetailsDto(
                    user.getId(),
                    user.getUsername(),
                    user.isEnabled(),
                    user.getRoles().stream().map(Role::getName).collect(Collectors.toList())
            );
            dto.setUser(userDto);
        }
        return dto;
    }
}
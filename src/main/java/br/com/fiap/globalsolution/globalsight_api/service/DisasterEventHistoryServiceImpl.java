package br.com.fiap.globalsolution.globalsight_api.service;

import br.com.fiap.globalsolution.globalsight_api.dto.DisasterEventHistoryDto;
import br.com.fiap.globalsolution.globalsight_api.dto.DisasterEventHistoryFilterDto;
import br.com.fiap.globalsolution.globalsight_api.entity.DisasterEventHistory; // Import CORRETO
import br.com.fiap.globalsolution.globalsight_api.repository.DisasterEventHistoryRepository;
import br.com.fiap.globalsolution.globalsight_api.service.filters.DisasterEventHistorySpecification;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification; // Import CORRETO
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DisasterEventHistoryServiceImpl implements DisasterEventHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(DisasterEventHistoryServiceImpl.class);
    private final DisasterEventHistoryRepository historyRepository;

    @Autowired
    public DisasterEventHistoryServiceImpl(DisasterEventHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DisasterEventHistoryDto> getAllHistoryEvents(Pageable pageable, DisasterEventHistoryFilterDto filterDto) {
        // O tipo de 'spec' é explicitamente Specification<DisasterEventHistory>
        Specification<DisasterEventHistory> spec = DisasterEventHistorySpecification.createSpecification(filterDto);
        // Agora o historyRepository.findAll(spec, pageable) deve ser resolvido corretamente
        return historyRepository.findAll(spec, pageable).map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public DisasterEventHistoryDto getHistoryEventById(String disNo) {
        DisasterEventHistory event = historyRepository.findById(disNo)
                .orElseThrow(() -> {
                    logger.warn("Tentativa de buscar evento histórico com DisNo inexistente: {}", disNo);
                    return new EntityNotFoundException("Evento histórico com DisNo '" + disNo + "' não encontrado.");
                });
        return convertToDto(event);
    }

    @Override
    @Transactional
    public DisasterEventHistoryDto createHistoryEvent(DisasterEventHistoryDto eventDto) {
        if (eventDto.getDisNo() == null || eventDto.getDisNo().trim().isEmpty()) {
            throw new IllegalArgumentException("DisNo (ID do evento) não pode ser nulo ou vazio para criação.");
        }
        if (historyRepository.existsById(eventDto.getDisNo())) {
            logger.warn("Tentativa de criar evento histórico com DisNo que já existe: {}", eventDto.getDisNo());
            throw new DataIntegrityViolationException("Evento histórico com DisNo '" + eventDto.getDisNo() + "' já existe.");
        }
        DisasterEventHistory event = new DisasterEventHistory();
        BeanUtils.copyProperties(eventDto, event);

        DisasterEventHistory savedEvent = historyRepository.save(event);
        logger.info("Evento histórico criado com DisNo: {}", savedEvent.getDisNo());
        return convertToDto(savedEvent);
    }

    @Override
    @Transactional
    public DisasterEventHistoryDto updateHistoryEvent(String disNo, DisasterEventHistoryDto eventDto) {
        DisasterEventHistory existingEvent = historyRepository.findById(disNo)
                .orElseThrow(() -> {
                    logger.warn("Tentativa de atualizar evento histórico com DisNo inexistente: {}", disNo);
                    return new EntityNotFoundException("Evento histórico com DisNo '" + disNo + "' não encontrado para atualização.");
                });

        BeanUtils.copyProperties(eventDto, existingEvent, "disNo");

        DisasterEventHistory updatedEvent = historyRepository.save(existingEvent);
        logger.info("Evento histórico atualizado com DisNo: {}", updatedEvent.getDisNo());
        return convertToDto(updatedEvent);
    }

    @Override
    @Transactional
    public void deleteHistoryEvent(String disNo) {
        if (!historyRepository.existsById(disNo)) {
            logger.warn("Tentativa de excluir evento histórico com DisNo inexistente: {}", disNo);
            throw new EntityNotFoundException("Evento histórico com DisNo '" + disNo + "' não encontrado para exclusão.");
        }
        historyRepository.deleteById(disNo);
        logger.info("Evento histórico excluído com DisNo: {}", disNo);
    }

    private DisasterEventHistoryDto convertToDto(DisasterEventHistory event) {
        DisasterEventHistoryDto dto = new DisasterEventHistoryDto();
        BeanUtils.copyProperties(event, dto);
        return dto;
    }
}
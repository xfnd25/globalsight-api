package br.com.fiap.globalsolution.globalsight_api.service;

import br.com.fiap.globalsolution.globalsight_api.dto.DisasterEventHistoryDto;
import br.com.fiap.globalsolution.globalsight_api.dto.DisasterEventHistoryFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DisasterEventHistoryService {

    // Read (Métodos que você já tinha)
    Page<DisasterEventHistoryDto> getAllHistoryEvents(Pageable pageable, DisasterEventHistoryFilterDto filterDto);
    DisasterEventHistoryDto getHistoryEventById(String disNo);

    // Create
    DisasterEventHistoryDto createHistoryEvent(DisasterEventHistoryDto eventDto);

    // Update
    DisasterEventHistoryDto updateHistoryEvent(String disNo, DisasterEventHistoryDto eventDto);

    // Delete
    void deleteHistoryEvent(String disNo);
}
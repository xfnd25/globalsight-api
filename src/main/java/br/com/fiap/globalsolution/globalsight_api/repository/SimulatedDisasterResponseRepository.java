package br.com.fiap.globalsolution.globalsight_api.repository;

import br.com.fiap.globalsolution.globalsight_api.entity.SimulatedDisasterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulatedDisasterResponseRepository extends JpaRepository<SimulatedDisasterResponse, Long>, JpaSpecificationExecutor<SimulatedDisasterResponse> {

    /**
     * Encontra todas as respostas de simulação de desastre para um usuário específico, com paginação.
     * @param userId O ID do usuário.
     * @param pageable Objeto contendo informações de paginação (número da página, tamanho, ordenação).
     * @return Uma página de SimulatedDisasterResponse.
     */
    Page<SimulatedDisasterResponse> findByUserId(Long userId, Pageable pageable);

    // Você pode adicionar outros métodos de consulta derivados aqui, se necessário.
    // Ex: List<SimulatedDisasterResponse> findByStatus(SimulationStatus status);
    // Ex: Page<SimulatedDisasterResponse> findByInputDisasterTypeAndStatus(String disasterType, SimulationStatus status, Pageable pageable);
    // No entanto, para filtros mais complexos, JpaSpecificationExecutor é mais flexível.
}
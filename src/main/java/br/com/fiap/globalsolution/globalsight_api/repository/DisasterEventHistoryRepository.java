package br.com.fiap.globalsolution.globalsight_api.repository;

import br.com.fiap.globalsolution.globalsight_api.entity.DisasterEventHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
// Não são necessários imports para Page, Pageable, Specification aqui,
// pois os métodos são herdados e os tipos são resolvidos pelo Spring Data JPA.

@Repository
public interface DisasterEventHistoryRepository extends
        JpaRepository<DisasterEventHistory, String>,
        JpaSpecificationExecutor<DisasterEventHistory> {

    // O método findAll(Specification<DisasterEventHistory> spec, Pageable pageable)
    // é herdado automaticamente de JpaSpecificationExecutor.
    // Você não precisa declará-lo aqui.
}
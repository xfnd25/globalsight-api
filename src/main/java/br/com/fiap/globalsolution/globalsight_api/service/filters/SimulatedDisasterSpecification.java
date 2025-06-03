package br.com.fiap.globalsolution.globalsight_api.service.filters;

import br.com.fiap.globalsolution.globalsight_api.dto.SimulatedDisasterFilterDto;
import br.com.fiap.globalsolution.globalsight_api.entity.SimulatedDisasterResponse;
import br.com.fiap.globalsolution.globalsight_api.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils; // Para verificar strings vazias/nulas

import java.util.ArrayList;
import java.util.List;

public class SimulatedDisasterSpecification {

    public static Specification<SimulatedDisasterResponse> createSpecification(SimulatedDisasterFilterDto filterDto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto == null) {
                return criteriaBuilder.conjunction(); // Retorna uma condição "true" se não houver filtros
            }

            if (StringUtils.hasText(filterDto.getDisasterType())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("inputDisasterType")),
                        "%" + filterDto.getDisasterType().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(filterDto.getContinent())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("inputContinent")),
                        "%" + filterDto.getContinent().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(filterDto.getRegion())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("inputRegion")),
                        "%" + filterDto.getRegion().toLowerCase() + "%"));
            }

            if (filterDto.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filterDto.getStatus()));
            }

            if (filterDto.getYear() != null) {
                predicates.add(criteriaBuilder.equal(root.get("inputYear"), filterDto.getYear()));
            }

            if (filterDto.getStartDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("requestTimestamp"),
                        filterDto.getStartDateFrom().atStartOfDay())); // Converte LocalDate para LocalDateTime
            }

            if (filterDto.getStartDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("requestTimestamp"),
                        filterDto.getStartDateTo().atTime(23, 59, 59))); // Fim do dia
            }

            // Adicione mais predicados de filtro conforme os campos em SimulatedDisasterFilterDto

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<SimulatedDisasterResponse> hasUser(User user) {
        return (root, query, criteriaBuilder) -> {
            if (user == null) {
                // Se o usuário for nulo, pode-se optar por não retornar nada ou lançar exceção,
                // dependendo da regra de negócio para endpoints não autenticados ou cenários de admin.
                // Para buscar apenas por usuário específico, um usuário nulo resultaria em nenhum filtro de usuário.
                // Neste caso, assumimos que se o usuário é fornecido, o filtro é aplicado.
                return criteriaBuilder.conjunction(); // Não adiciona restrição se user for nulo
            }
            return criteriaBuilder.equal(root.get("user"), user);
        };
    }
}
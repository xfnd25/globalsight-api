package br.com.fiap.globalsolution.globalsight_api.service.filters;

import br.com.fiap.globalsolution.globalsight_api.dto.DisasterEventHistoryFilterDto;
import br.com.fiap.globalsolution.globalsight_api.entity.DisasterEventHistory; // Import CORRETO
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification; // Import CORRETO
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DisasterEventHistorySpecification {

    public static Specification<DisasterEventHistory> createSpecification(DisasterEventHistoryFilterDto filterDto) {
        // O tipo de retorno é Specification<DisasterEventHistory>
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto == null) {
                return criteriaBuilder.conjunction();
            }

            if (filterDto.getYearEvent() != null) {
                predicates.add(criteriaBuilder.equal(root.get("yearEvent"), filterDto.getYearEvent()));
            }

            if (StringUtils.hasText(filterDto.getDisasterType())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("disasterType")),
                        "%" + filterDto.getDisasterType().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(filterDto.getDisasterGroup())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("disasterGroup")),
                        "%" + filterDto.getDisasterGroup().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(filterDto.getDisasterSubgroup())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("disasterSubgroup")),
                        "%" + filterDto.getDisasterSubgroup().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(filterDto.getContinent())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("continent")),
                        "%" + filterDto.getContinent().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(filterDto.getRegion())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("region")),
                        "%" + filterDto.getRegion().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(filterDto.getCountry())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("country")),
                        "%" + filterDto.getCountry().toLowerCase() + "%"));
            }

            if (filterDto.getMinTotalDeaths() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalDeaths"), filterDto.getMinTotalDeaths()));
            }

            if (filterDto.getMaxTotalDeaths() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("totalDeaths"), filterDto.getMaxTotalDeaths()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
package br.com.fiap.globalsolution.globalsight_api.dto;

import lombok.Data;

@Data
public class DisasterEventHistoryFilterDto {
    private Integer yearEvent;
    private String disasterType;
    private String disasterGroup;
    private String disasterSubgroup;
    private String continent;
    private String region;
    private String country;
    private Integer minTotalDeaths;
    private Integer maxTotalDeaths;
}
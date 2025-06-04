package br.com.fiap.globalsolution.globalsight_api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisasterEventHistoryDto {
    private String disNo;
    private Integer yearEvent;
    private String disasterGroup;
    private String disasterSubgroup;
    private String disasterType;
    private String country;
    private String iso;
    private String region;
    private String continent;
    private Integer startYear;
    private Integer startMonth;
    private Integer startDay;
    private Integer endYear;
    private Integer endMonth;
    private Integer endDay;
    private Double disMagValue;
    private String disMagScale;
    private String latitude;
    private String longitude;
    private Double cpi;
    private Integer totalDeaths;
    private Integer noInjured;
    private Integer noAffected;
    private Integer noHomeless;
    private Integer totalAffectedCombined;
    private Double reconstructionCosts000Usd;
    private Double insuredDamages000Usd;
    private Double totalDamages000Usd;
    private String glide;
    private String eventName;
    private String origin;
    private String associatedDis;
    private String associatedDis2;
    private Boolean ofdaResponse;
    private String appeal;
    private String declaration;
    private Double aidContribution;
    private String localTime;
    private String riverBasin;
    private Double coordinateX;
    private Double coordinateZ;
}
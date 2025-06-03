package br.com.fiap.globalsolution.globalsight_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "disaster_event_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisasterEventHistory {

    @Id
    @Column(name = "dis_no", unique = true, nullable = false, length = 50)
    private String disNo;

    @Column(name = "year_event")
    private Integer yearEvent;

    @Column(name = "disaster_group", length = 100)
    private String disasterGroup;

    @Column(name = "disaster_subgroup", length = 100)
    private String disasterSubgroup;

    @Column(name = "disaster_type", length = 100)
    private String disasterType;

    @Column(length = 100)
    private String country;

    @Column(length = 10)
    private String iso;

    @Column(length = 100)
    private String region;

    @Column(length = 100)
    private String continent;

    @Column(name = "start_year")
    private Integer startYear;
    @Column(name = "start_month")
    private Integer startMonth;
    @Column(name = "start_day")
    private Integer startDay;
    @Column(name = "end_year")
    private Integer endYear;
    @Column(name = "end_month")
    private Integer endMonth;
    @Column(name = "end_day")
    private Integer endDay;

    @Column(name = "dis_mag_value")
    private Double disMagValue;
    @Column(name = "dis_mag_scale", length = 50)
    private String disMagScale;

    @Column(length = 50)
    private String latitude;
    @Column(length = 50)
    private String longitude;

    private Double cpi;

    @Column(name = "total_deaths")
    private Integer totalDeaths;

    @Column(name = "no_injured")
    private Integer noInjured;

    @Column(name = "no_affected")
    private Integer noAffected;

    @Column(name = "no_homeless")
    private Integer noHomeless;

    @Column(name = "total_affected_combined") // Anteriormente 'total_affected'
    private Integer totalAffectedCombined;

    @Column(name = "reconstruction_costs_000_usd")
    private Double reconstructionCosts000Usd;

    @Column(name = "insured_damages_000_usd")
    private Double insuredDamages000Usd;

    @Column(name = "total_damages_000_usd")
    private Double totalDamages000Usd;

    @Column(name = "glide", length = 50)
    private String glide;

    @Column(name = "event_name", columnDefinition = "TEXT")
    private String eventName;

    @Column(name = "origin", columnDefinition = "TEXT")
    private String origin;

    @Column(name = "associated_dis", columnDefinition = "TEXT")
    private String associatedDis;

    @Column(name = "associated_dis2", columnDefinition = "TEXT")
    private String associatedDis2;

    @Column(name = "ofda_response")
    private Boolean ofdaResponse;

    @Column(name = "appeal", length = 100) // Aumentei o tamanho caso seja um texto e não apenas sim/não
    private String appeal;

    @Column(name = "declaration", length = 100) // Aumentei o tamanho
    private String declaration;

    @Column(name = "aid_contribution")
    private Double aidContribution;

    @Column(name = "local_time", length = 20)
    private String localTime;

    @Column(name = "river_basin", columnDefinition = "TEXT")
    private String riverBasin;

    @Column(name = "coordinate_x")
    private Double coordinateX;

    @Column(name = "coordinate_z")
    private Double coordinateZ;
}
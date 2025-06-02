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

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "iso", length = 10)
    private String iso;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "continent", length = 100)
    private String continent;

    // Campos para datas de início/fim para cálculo da duração se necessário
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

    // Campos de magnitude
    @Column(name = "dis_mag_value")
    private Double disMagValue;
    @Column(name = "dis_mag_scale", length = 50)
    private String disMagScale;

    // Campos de localização
    @Column(length = 50)
    private String latitude;
    @Column(length = 50)
    private String longitude;

    @Column(name = "cpi")
    private Double cpi;

    // Campos de impacto
    @Column(name = "total_deaths")
    private Integer totalDeaths;
    @Column(name = "total_affected")
    private Integer totalAffected;
    @Column(name = "total_damages_000_usd")
    private Double totalDamages000Usd;

    // Adicione mais colunas do EMDAT que sejam úteis para consulta
}
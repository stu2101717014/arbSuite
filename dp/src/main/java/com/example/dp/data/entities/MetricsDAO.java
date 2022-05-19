package com.example.dp.data.entities;

import javax.persistence.*;

@Entity
@Table(name = "metricsDAO")
public class MetricsDAO {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dataReshapeTimeComplexity", nullable = false)
    private Long dataReshapeTimeComplexity;

    @Column(name = "nameSimilaritiesRemapTimeComplexity", nullable = false)
    private Long nameSimilaritiesRemapTimeComplexity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDataReshapeTimeComplexity() {
        return dataReshapeTimeComplexity;
    }

    public void setDataReshapeTimeComplexity(Long dataReshapeTimeComplexity) {
        this.dataReshapeTimeComplexity = dataReshapeTimeComplexity;
    }

    public Long getNameSimilaritiesRemapTimeComplexity() {
        return nameSimilaritiesRemapTimeComplexity;
    }

    public void setNameSimilaritiesRemapTimeComplexity(Long nameSimilaritiesRemapTimeComplexity) {
        this.nameSimilaritiesRemapTimeComplexity = nameSimilaritiesRemapTimeComplexity;
    }
}

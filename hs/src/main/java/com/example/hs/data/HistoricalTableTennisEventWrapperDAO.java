package com.example.hs.data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "historicalTableTennisEventWrapperDAO")
public class HistoricalTableTennisEventWrapperDAO {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dateCreated", nullable = true)
    private Date time;

    @Lob
    private String historicalRecordEventWrapperAsJson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getHistoricalRecordEventWrapperAsJson() {
        return historicalRecordEventWrapperAsJson;
    }

    public void setHistoricalRecordEventWrapperAsJson(String historicalRecordEventWrapperAsJson) {
        this.historicalRecordEventWrapperAsJson = historicalRecordEventWrapperAsJson;
    }
}

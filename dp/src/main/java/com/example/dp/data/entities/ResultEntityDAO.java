package com.example.dp.data.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "ResultEntityDAO")
public class ResultEntityDAO {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="platformName", nullable = false)
    private String platformName;

    @Column(name="exception", nullable = true)
    private Exception exception;

    @Column(name="dateCreated", nullable = true)
    private Date time;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "result_tableTennisEvent",
            joinColumns = @JoinColumn(name = "resultId"),
            inverseJoinColumns = @JoinColumn(name = "tableTennisEventId"))
    private Set<TableTennisEventEntityDAO> tableTennisEventEntitySet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Set<TableTennisEventEntityDAO> getTableTennisEventEntitySet() {
        return tableTennisEventEntitySet;
    }

    public void setTableTennisEventEntitySet(Set<TableTennisEventEntityDAO> payload) {
        this.tableTennisEventEntitySet = payload;
    }
}

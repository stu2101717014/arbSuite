package com.example.bwinexperimental.data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "tableTennisEvent")
public class TableTennisEventEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstPlayerName", length = 255, nullable = true)
    private String firstPlayerName;

    @Column(name = "secondPlayerName", length = 255, nullable = true)
    private String secondPlayerName;

    @Column(name = "firstPlayerWinningOdd", nullable = true)
    private Double firstPlayerWinningOdd;

    @Column(name = "secondPlayerWinningOdd", nullable = true)
    private Double secondPlayerWinningOdd;

    @ManyToMany(mappedBy = "tableTennisEventEntitySet", fetch = FetchType.EAGER)
    private transient Set<ResultEntity> resultEntity;

    @Column(name="eventDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;

    public Set<ResultEntity> getResultEntity() {
        return resultEntity;
    }

    public void setResultEntity(Set<ResultEntity> resultEntity) {this.resultEntity = resultEntity ;}

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstPlayerName() {
        return firstPlayerName;
    }

    public void setFirstPlayerName(String firstPlayerName) {
        this.firstPlayerName = firstPlayerName;
    }

    public String getSecondPlayerName() {
        return secondPlayerName;
    }

    public void setSecondPlayerName(String secondPlayerName) {
        this.secondPlayerName = secondPlayerName;
    }

    public Double getFirstPlayerWinningOdd() {
        return firstPlayerWinningOdd;
    }

    public void setFirstPlayerWinningOdd(Double firstPlayerWinningOdd) {
        this.firstPlayerWinningOdd = firstPlayerWinningOdd;
    }

    public Double getSecondPlayerWinningOdd() {
        return secondPlayerWinningOdd;
    }

    public void setSecondPlayerWinningOdd(Double secondPlayerWinningOdd) {
        this.secondPlayerWinningOdd = secondPlayerWinningOdd;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }
}

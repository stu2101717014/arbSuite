package com.example.bwinexperimental.data;


import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "result")
public class ResultEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="exception", nullable = true)
    private Exception exception;

    @Column(name="dateCreated", nullable = true)
    private Date time;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "result_tableTennisEvent",
            joinColumns = @JoinColumn(name = "resultId"),
            inverseJoinColumns = @JoinColumn(name = "tableTennisEventId"))
    private Set<TableTennisEventEntity> tableTennisEventEntitySet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<TableTennisEventEntity> getTableTennisEventEntitySet() {
        return tableTennisEventEntitySet;
    }

    public void setTableTennisEventEntitySet(Set<TableTennisEventEntity> payload) {
        this.tableTennisEventEntitySet = payload;
    }
}

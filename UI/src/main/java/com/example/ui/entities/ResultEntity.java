package com.example.ui.entities;

import java.util.Date;
import java.util.Set;

public class ResultEntity {

    private Long id;

    private Exception exception;

    private Date time;

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

package com.example.ui.entities.helpers;

import java.util.Date;
import java.util.Set;

public class ResultEntity {

    private Exception exception;

    private Date time;

    private Set<TableTennisEventEntity> tableTennisEventEntitySet;

    private String platformName;

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

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
}

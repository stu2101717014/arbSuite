package dtos;

import java.util.Date;

public class HistoricalTableTennisEventWrapperDTO {

    private Date time;

    private String historicalRecordEventWrapperAsJson;

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

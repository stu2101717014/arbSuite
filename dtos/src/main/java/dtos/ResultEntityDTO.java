package dtos;

import java.util.Date;
import java.util.Set;

public class ResultEntityDTO {

    private String platformName;

    private Exception exception;

    private Date time;

    private Set<TableTennisEventEntityDTO> tableTennisEventEntitySet;

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

    public Set<TableTennisEventEntityDTO> getTableTennisEventEntitySet() {
        return tableTennisEventEntitySet;
    }

    public void setTableTennisEventEntitySet(Set<TableTennisEventEntityDTO> payload) {
        this.tableTennisEventEntitySet = payload;
    }
}

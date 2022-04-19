package dtos;

import java.util.Date;

public class PostProcessTableTennisWrapperDTO {

    private Date time;

    private String resultAsJson;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getResultAsJson() {
        return resultAsJson;
    }

    public void setResultAsJson(String resultAsJson) {
        this.resultAsJson = resultAsJson;
    }
}

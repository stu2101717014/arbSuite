package dtos;

import java.util.Date;
import java.util.Set;

public class TableTennisEventEntityDTO {

    private String firstPlayerName;

    private String secondPlayerName;

    private Double firstPlayerWinningOdd;

    private Double secondPlayerWinningOdd;

    private transient Set<ResultEntityDTO> resultEntityDTO;

    private Date eventDate;

    private Date startExtraction;

    private Date finishedExtraction;

    public Set<ResultEntityDTO> getResultEntity() {
        return resultEntityDTO;
    }

    public void setResultEntity(Set<ResultEntityDTO> resultEntityDTO) {this.resultEntityDTO = resultEntityDTO;}

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

    public Date getStartExtraction() {
        return startExtraction;
    }

    public void setStartExtraction(Date startExtraction) {
        this.startExtraction = startExtraction;
    }

    public Date getFinishedExtraction() {
        return finishedExtraction;
    }

    public void setFinishedExtraction(Date finishedExtraction) {
        this.finishedExtraction = finishedExtraction;
    }

    @Override
    public String toString() {
        return getFirstPlayerWinningOdd() + " : " + getSecondPlayerWinningOdd();
    }
}

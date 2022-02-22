package com.example.ui.entities.helpers;

import java.util.Date;

public class TableTennisEventEntity {
    private String firstPlayerName;

    private String secondPlayerName;

    private Double firstPlayerWinningOdd;

    private Double secondPlayerWinningOdd;

    private java.util.Date eventDate;

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

    @Override
    public String toString() {
        return getFirstPlayerName() + " : " + getSecondPlayerName() + "\n"
                + getFirstPlayerWinningOdd() + " : " + getSecondPlayerWinningOdd();
    }
}

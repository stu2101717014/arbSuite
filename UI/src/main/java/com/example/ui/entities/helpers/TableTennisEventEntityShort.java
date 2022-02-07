package com.example.ui.entities.helpers;

import com.helger.commons.compare.IComparable;

import java.util.Date;
import java.util.Objects;

public class TableTennisEventEntityShort {
    private String firstPlayer;

    private String secondPlayer;

    private Date eventDate;

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(String firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public String getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(String secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstPlayer, secondPlayer, eventDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        TableTennisEventEntityShort obj1 = (TableTennisEventEntityShort) obj;
        if (obj1.getFirstPlayer().equals(this.getFirstPlayer())
        && obj1.getSecondPlayer().equals(this.getSecondPlayer())
        && obj1.getEventDate().equals(this.getEventDate())){
            return true;
        }
        return false;
    }
}

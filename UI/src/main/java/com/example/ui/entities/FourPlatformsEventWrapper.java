package com.example.ui.entities;

import com.helger.commons.compare.IComparable;

import java.util.Date;

public class FourPlatformsEventWrapper implements IComparable<FourPlatformsEventWrapper> {

    private Long id;

    private Date eventDate;

    private String firstPlayer;

    private String secondPlayer;

    private String bwinEvent;

    private String bets22Event;

    private String betWinnerEvent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

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

    public String getBwinEvent() {
        return bwinEvent;
    }

    public void setBwinEvent(String bwinEvent) {
        this.bwinEvent = bwinEvent;
    }

    public String getBets22Event() {
        return bets22Event;
    }

    public void setBets22Event(String bets22Event) {
        this.bets22Event = bets22Event;
    }

    public String getBetWinnerEvent() {
        return betWinnerEvent;
    }

    public void setBetWinnerEvent(String betWinnerEvent) {
        this.betWinnerEvent = betWinnerEvent;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null ){ return false;}
        FourPlatformsEventWrapper obj1 = (FourPlatformsEventWrapper) obj;
        if ((obj1.getEventDate().compareTo(this.getEventDate()) == 0)
                && (obj1.getFirstPlayer().compareTo(this.getFirstPlayer()) == 0)
                && (obj1.getSecondPlayer().compareTo(this.getSecondPlayer()) == 0)) {
            return true;
        }
        return false;

    }

    @Override
    public int compareTo(FourPlatformsEventWrapper o) {
        if (o.equals(this)){
            return 0;
        }
        return (o.getFirstPlayer() + o.getSecondPlayer()).compareTo(this.getFirstPlayer() + this.getSecondPlayer());
    }

}

package com.example.ui.entities.jpa;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "namesSimilarities")
public class NamesSimilarities {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="lastTimeUsed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastTimeUsed;

    @Column(name = "universalPlayerName", length = 255, nullable = true)
    private String universalPlayerName;

    @Column(name = "platformName", length = 255, nullable = true)
    private String platformName;

    private String platformSpecificPlayerName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLastTimeUsed() {
        return lastTimeUsed;
    }

    public void setLastTimeUsed(Date lastTimeUsed) {
        this.lastTimeUsed = lastTimeUsed;
    }

    public String getUniversalPlayerName() {
        return universalPlayerName;
    }

    public void setUniversalPlayerName(String universalPlayerName) {
        this.universalPlayerName = universalPlayerName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformSpecificPlayerName() {
        return platformSpecificPlayerName;
    }

    public void setPlatformSpecificPlayerName(String platformSpecificPlayerName) {
        this.platformSpecificPlayerName = platformSpecificPlayerName;
    }
}

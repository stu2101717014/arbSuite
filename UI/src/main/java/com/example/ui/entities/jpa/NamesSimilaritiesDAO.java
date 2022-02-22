package com.example.ui.entities.jpa;

import javax.persistence.*;

@Entity
@Table(name = "namesSimilarities")
public class NamesSimilaritiesDAO {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "universalPlayerName", length = 255, nullable = true)
    private String universalPlayerName;

    @Column(name = "platformName", length = 255, nullable = true)
    private String platformName;

    @Column(name = "platformSpecificPlayerName", length = 255, nullable = true)
    private String platformSpecificPlayerName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

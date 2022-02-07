package com.example.ui.entities.jpa;

import javax.persistence.*;

@Entity
@Table(name = "platformDataRequestWrapperEntity")
public class PlatformDataRequestWrapperEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "platformName", length = 255, nullable = true)
    private String platformName;

    @Column(name = "url", length = 255, nullable = true)
    private String url;

    @Column(name = "isAccessible", columnDefinition="tinyint(1) default 1")
    private Boolean isAccessible;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getAccessible() {
        return isAccessible;
    }

    public void setAccessible(Boolean accessible) {
        isAccessible = accessible;
    }
}

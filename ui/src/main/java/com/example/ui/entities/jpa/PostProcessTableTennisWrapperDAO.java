package com.example.ui.entities.jpa;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "postProcessTableTennisWrapperDAO")
public class PostProcessTableTennisWrapperDAO {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="dateCreated", nullable = true)
    private Date time;

    @Lob
    private String resultAsJson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

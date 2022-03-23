package com.example.ui.entities.jpa;

import javax.persistence.*;

@Entity
@Table(name = "settings")
public class SettingsDAO {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name="postProcessDataFixedDelayTime", nullable = false)
    private Long postProcessDataFixedDelayTime = 40000L; // 40 seconds in milliseconds


    @Column(name="cleanupOldDataTime", nullable = false)
    private Long cleanupOldDataTime = 600000L; // 10 minutes in milliseconds


}

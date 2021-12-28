package com.example.bets22.controllers;

import org.example.commons.bets22.services.Bets22Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/22bets")
public class Bets22 {

    @Autowired
    private Bets22Service bets22Service;

    @GetMapping
    public ResponseEntity<String> get22BetsData() {
        String result = bets22Service.createAndExecuteTableTennisUserStory();

        return ResponseEntity.ok().body(result);
    }
}
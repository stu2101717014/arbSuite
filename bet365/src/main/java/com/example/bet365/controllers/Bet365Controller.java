package com.example.bet365.controllers;

import org.example.commons.bet365.services.Bet365Service;
import org.example.commons.entities.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bet365")
public class Bet365Controller {

    @Autowired
    private Bet365Service bet365Service;

    @GetMapping
    public ResponseEntity<String> getBet365Data() {

        String andExecuteTableTennisUserStory = bet365Service.createAndExecuteTableTennisUserStory();

        return ResponseEntity.ok().body(andExecuteTableTennisUserStory);
    }
}

package com.example.betwinner.controllers;

import org.example.commons.betwinner.services.BetwinnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/betwinner")
public class BetWinner {

    @Autowired
    private BetwinnerService betwinnerService;

    @GetMapping
    public ResponseEntity<String> get22BetsData() {

        String andExecuteTableTennisUserStory = betwinnerService.createAndExecuteTableTennisUserStory();

        return ResponseEntity.ok().body(andExecuteTableTennisUserStory);
    }
}
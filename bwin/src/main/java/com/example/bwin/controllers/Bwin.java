package com.example.bwin.controllers;

import org.example.commons.bwin.services.BwinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/bwin")
public class Bwin {

    @Autowired
    private BwinService bwinService;

    @GetMapping
    public ResponseEntity<String> get22BetsData() {
        String result = bwinService.createAndExecuteTableTennisUserStory();

        return ResponseEntity.ok().body(result);
    }
}

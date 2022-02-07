package com.example.williamhill.controllers;

import org.example.commons.williamhill.services.WilliamHillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/williamhill")
public class WilliamHillController {

    @Autowired
    private WilliamHillService williamHillService;

    @GetMapping
    public ResponseEntity<String> get22BetsData() {
        String result = williamHillService.createAndExecuteTableTennisUserStory();

        return ResponseEntity.ok().body(result);
    }
}

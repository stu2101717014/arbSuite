package com.example.mybookie.controllers;

import org.example.commons.mybookie.services.MybookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mybookie")
public class MybookieController {

    private final MybookieService mybookieService;

    @Autowired
    public MybookieController(MybookieService mybookieService) {
        this.mybookieService = mybookieService;
    }

    @GetMapping
    public ResponseEntity<String> getMybookieData() {
        String result = mybookieService.createAndExecuteTableTennisUserStory();

        return ResponseEntity.ok().body(result);
    }
}

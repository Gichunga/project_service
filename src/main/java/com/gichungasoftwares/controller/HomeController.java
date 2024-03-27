package com.gichungasoftwares.controller;

import com.gichungasoftwares.model.Project;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/projects")
    public ResponseEntity<String> getProjectById() {

        return new ResponseEntity<>("Welcome to the project microservice", HttpStatus.OK);
    }
}

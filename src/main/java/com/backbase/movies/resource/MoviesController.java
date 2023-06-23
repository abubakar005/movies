package com.backbase.movies.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class MoviesController {

    @GetMapping
    public String hello() {
        return "Hellooooooo";
    }
}

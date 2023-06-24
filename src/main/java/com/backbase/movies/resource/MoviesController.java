package com.backbase.movies.resource;

import com.backbase.movies.dto.MovieInfoDto;
import com.backbase.movies.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/")
public class MoviesController {

    private final MovieService movieService;

    public MoviesController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public String hello() throws IOException {
        movieService.importDataFromCSV("D:\\Recruiters\\Backbase\\academy_awards.csv");
        return "Hellooooooo";
    }

    @GetMapping("search-by-title/{title}")
    public MovieInfoDto searchByTitle(@PathVariable("title") String title) {
        return movieService.searchMovieByTitle(title);
    }
}

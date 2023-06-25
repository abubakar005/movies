package com.backbase.movies.resource;

import com.backbase.movies.dto.MovieInfoDto;
import com.backbase.movies.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/movie")
public class MoviesController {

    private final MovieService movieService;

    public MoviesController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/hi")
    public String getMesg() {
        return "hi";
    }
    @GetMapping
    public String hello() throws Exception {
        movieService.importDataFromCSV("D:\\Recruiters\\Backbase\\academy_awards.csv");
        return "Hellooooooo";
    }

    @GetMapping("/search-by-title/{title}")
    public MovieInfoDto searchByTitle(@PathVariable("title") String title) throws Exception {
        return movieService.searchMovieByTitle(title);
    }

    @PostMapping("/{title}/rating")
    public ResponseEntity<Void> rateMovie(@PathVariable String title,
                                          @RequestParam double rating) throws Exception {
        movieService.saveRating(title, rating);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

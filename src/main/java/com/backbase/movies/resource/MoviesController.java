package com.backbase.movies.resource;

import com.backbase.movies.service.MovieService;
import jakarta.validation.constraints.NotNull;
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

    @GetMapping("/{movie-title}/best-picture-award")
    public ResponseEntity<String> checkBestPictureAward(@NotNull @PathVariable("movie-title") String movieTitle) {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.hasWonBestPictureAward(movieTitle));
    }

    @PostMapping("/{title}/rating")
    public ResponseEntity<String> rateMovie(@NotNull @PathVariable("title") String title,
                                          @NotNull @RequestParam int rating) {

        if (rating < 1 || rating > 10)
            return ResponseEntity.badRequest().body("Rating value should be between 1 and 10.");

        movieService.saveRating(title, rating);
        return ResponseEntity.ok("Rating saved successfully.");
    }

    @GetMapping("/top-rated")
    public ResponseEntity<String> topRatedMovies() {
        return null;
    }
}

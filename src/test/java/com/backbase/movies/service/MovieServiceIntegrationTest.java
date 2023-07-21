package com.backbase.movies.service;

import com.backbase.movies.AbstractContainerBaseTest;
import com.backbase.movies.dto.TopRatedMovie;
import com.backbase.movies.entity.Movie;
import com.backbase.movies.exception.NotFoundException;
import com.backbase.movies.repository.MovieRepository;
import com.backbase.movies.util.AwardsInfoFileUploadUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieServiceIntegrationTest extends AbstractContainerBaseTest {

    @BeforeAll
    static void setUp () {
        mySQLContainer.start();
        AwardsInfoFileUploadUtil.loadBestPictureAwardsMap();
    }

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    @DisplayName("This should return TRUE as it has won the best picture Award")
    void movieHasWonBestPictureAward() {
        String response = movieService.hasWonBestPictureAward("Hamlet");
        assertEquals("'Hamlet' has won a best picture award", response);
    }

    @Test
    @DisplayName("This should return FALSE as it has not won the best picture Award")
    void movieHasNotWonBestPictureAward() {
        String response = movieService.hasWonBestPictureAward("The Fighter");
        assertEquals("'The Fighter' has not won a best picture award", response);
    }

    @Test
    @DisplayName("This should return Not Found exception as this movie doesn't exists")
    void movieNotFoundForBestPictureAward() {
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> movieService.hasWonBestPictureAward("TestMovie"));
        assertEquals("Movie not found against the title TestMovie", exception.getMsg());
    }

    //@Test
    @DisplayName("This should save the Movie rating")
    void shouldSaveMovieRating() {
        movieService.saveRating("Hamlet", 8);
        Movie movie = movieRepository.findByTitle("Hamlet").get();
       // assertEquals(movie.getRating(), BigDecimal.valueOf(1));
    }

    @Test
    @DisplayName("This should return Top rated movies list")
    void shouldReturnTopRatedMovies() {
        List<TopRatedMovie> topRatedMovies = movieService.topRatedMovies();
        assertEquals(2, topRatedMovies.size());
    }
}
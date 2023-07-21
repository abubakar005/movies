package com.backbase.movies.resource;

import com.backbase.movies.BaseTestConfiguration;
import com.backbase.movies.dto.TopRatedMovie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@AutoConfigureTestDatabase//(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ExtendWith({MySQLTestContainerExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MoviesControllerIntegrationTest extends BaseTestConfiguration {

    @Autowired
    private MoviesController moviesController;

    @AfterAll
    static void tearDown() {
        mySQLContainer.stop();
    }

    @Test
    void shouldReturnBestPictureAwardWon() {
        ResponseEntity<String> response = moviesController.checkBestPictureAward("Hamlet");
        assertEquals("'Hamlet' has won a best picture award", response.getBody());
    }

    @Test
    void shouldReturnBestPictureAwardNotWon() {
        ResponseEntity<String> response = moviesController.checkBestPictureAward("The Fighter");
        assertEquals("'The Fighter' has not won a best picture award", response.getBody());
    }

    @Test
    void rateMovie() {
    }

    @Test
    void shouldReturnTopRatedMoviesList() {
        ResponseEntity<List<TopRatedMovie>> topRatedMovies = moviesController.topRatedMovies();
        assertEquals(2, topRatedMovies.getBody().size());
    }
}
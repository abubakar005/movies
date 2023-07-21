package com.backbase.movies.service;

import com.backbase.movies.dto.MovieInfoDto;
import com.backbase.movies.entity.Movie;
import com.backbase.movies.repository.MovieRatingRepository;
import com.backbase.movies.repository.MovieRepository;
import com.backbase.movies.repository.UserRepository;
import com.backbase.movies.service.impl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.powermock.api.mockito.PowerMockito;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;

@ExtendWith({MockitoExtension.class})
@PrepareForTest(MovieServiceImpl.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieRatingRepository movieRatingRepository;

    @Mock
    private UserRepository userRepository;

    private MovieService movieService;

    @BeforeEach
    public void setup() {
        movieService = new MovieServiceImpl(movieRepository, movieRatingRepository, userRepository);
    }

    @Test
    void hasWonBestPictureAward() {
        when(movieRepository.findByTitle(anyString())).thenReturn(Optional.of(getMockedMovie()));
        String response = movieService.hasWonBestPictureAward("All About Eve");
        assertEquals("'All About Eve' has won a best picture award", response);
    }

    //@Test
    void hasNotWonBestPictureAward() throws Exception {
        ReflectionTestUtils.setField(movieService, "OMDB_API_KEY", "value"); // to provide value to the @Value annotated parameter
        when(movieRepository.findByTitle(anyString())).thenReturn(Optional.empty());

        MovieService spy = PowerMockito.spy(movieService);
        doReturn(getMockedMovieInfoDto()).when(spy, "searchMovieByTitle", ArgumentMatchers.any());
        //when(movieService.someMethod()).thenReturn("mockedValue");
        String response = movieService.hasWonBestPictureAward("All About Eve");
        assertEquals("'All About Eve' has won a best picture award", response);
    }

    @Test
    void saveRating() {
    }

    @Test
    void topRatedMovies() {
    }

    private Movie getMockedMovie() {
        return Movie.builder()
                .id(1l)
                .title("All About Eve")
                .imdId("tt0042192")
                .actors("Bette Davis, Anne Baxter, George Sanders")
                .awards("Won 6 Oscars. 27 wins & 20 nominations total")
                .boxOffice(63463l)
                .genre("Drama")
                .oscarWon(Boolean.TRUE)
                .rating(BigDecimal.valueOf(8.2))
                .released("27 Oct 1950")
                .votes(134216l)
                .build();
    }

    private MovieInfoDto getMockedMovieInfoDto() {
        MovieInfoDto movieInfoDto = new MovieInfoDto("Title",
                "Year",
                "Rated",
                "Released",
                "Runtime",
                "Genre",
                "Director",
                "Writer",
                "Actors",
                "Plot",
                "Language",
                "Country",
                "Awards",
                "Poster",
                new LinkedList<>(),
                "Metascore",
                "imdbRating",
                "imdbVotes",
                "imdbID",
                "Type",
                "DVD",
                "BoxOffice",
                "Production",
                "Website",
                "Response");
        return movieInfoDto;
    }
}
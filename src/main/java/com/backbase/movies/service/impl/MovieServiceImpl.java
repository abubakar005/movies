package com.backbase.movies.service.impl;

import com.backbase.movies.dto.MovieInfoDto;
import com.backbase.movies.entity.Movie;
import com.backbase.movies.entity.MovieRating;
import com.backbase.movies.entity.User;
import com.backbase.movies.exception.GeneralException;
import com.backbase.movies.exception.NotFoundException;
import com.backbase.movies.exception.ServiceUnavailableException;
import com.backbase.movies.exception.UserUnauthorizedException;
import com.backbase.movies.repository.MovieRepository;
import com.backbase.movies.repository.MovieRatingRepository;
import com.backbase.movies.repository.UserRepository;
import com.backbase.movies.service.MovieService;
import com.backbase.movies.util.AwardsInfoFileUploadUtil;
import com.backbase.movies.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

    @Value("${OMDB_API_KEY:NoKey}")
    private String OMDB_API_KEY;
    private final MovieRepository movieRepository;
    private final MovieRatingRepository movieRatingRepository;
    private final UserRepository userRepository;

    public MovieServiceImpl(MovieRepository movieRepository, MovieRatingRepository movieRatingRepository
            , UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.movieRatingRepository = movieRatingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String hasWonBestPictureAward(String movieTitle) {

        boolean oscarWon = false;

        try {
            Optional<Movie> movieObj = movieRepository.findByTitle(movieTitle);

            if (movieObj.isPresent()) {
                oscarWon = movieObj.get().getOscarWon();
            } else {
                MovieInfoDto movieInfo = searchMovieByTitle(movieTitle);
                String awardStatus = AwardsInfoFileUploadUtil.getBestPictureAwardsMap().get(movieTitle);
                oscarWon = awardStatus != null && awardStatus.equals(Constants.YES);

                Movie movie = dtoToEntity(movieInfo, oscarWon);
                movieRepository.save(movie);
            }
        } catch (NotFoundException nfe) {
            throw new NotFoundException(nfe.getCode(), nfe.getMsg());
        } catch (ServiceUnavailableException sue) {
            throw new ServiceUnavailableException(sue.getCode(), sue.getMsg());
        } catch (Exception e) {
            throw new GeneralException(Constants.GENERAL_ERROR, e.getMessage());
        }

        return oscarWon ? String.format(Constants.ERROR_MOVIE_WON_AWARD_MSG, movieTitle)
                : String.format(Constants.ERROR_MOVIE_NOT_WON_AWARD_MSG, movieTitle);
    }

    @Override
    public void saveRating(String title, int rating) {
        Movie movie = movieRepository.findByTitle(title)
                .orElseGet(() -> {
                    try {
                        MovieInfoDto movieInfoDto = searchMovieByTitle(title);
                        String result = AwardsInfoFileUploadUtil.getBestPictureAwardsMap().get(title);
                        boolean awardWon = result != null && result.equals(Constants.YES);

                        return dtoToEntity(movieInfoDto, awardWon);
                    } catch (Exception e) {
                        throw new GeneralException(Constants.GENERAL_ERROR, e.getMessage());
                    }
                });

        long totalVotes = movie.getVotes();
        BigDecimal oldRating = movie.getRating();

        BigDecimal newRating = BigDecimal.valueOf(rating);
        BigDecimal updatedRating = oldRating.multiply(BigDecimal.valueOf(totalVotes)).add(newRating);
        long updatedVotes = totalVotes + 1;
        movie.setRating(updatedRating.divide(BigDecimal.valueOf(updatedVotes), 1, RoundingMode.CEILING));
        movie.setVotes(updatedVotes);

        movieRepository.save(movie);
          /* MovieRating movieRating = MovieRating.builder()
                .rating(rating)
                .movieId(1l)
                .user(getAuthenticatedUser())
                .build();
        movieRatingRepository.save(movieRating);*/
    }

    private MovieInfoDto searchMovieByTitle(String movieTitle) throws Exception {

        if(OMDB_API_KEY.isBlank() || OMDB_API_KEY.equals(Constants.OMDB_API_DEFAULT_NO_KEY))
            throw new NotFoundException(Constants.ERROR_OMDB_API_KEY_NOT_FOUND, Constants.ERROR_OMDB_API_KEY_NOT_FOUND_MSG);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format(Constants.OMDB_API_URL, OMDB_API_KEY, movieTitle.replace(Constants.CHARACTER_SPACE, Constants.CHARACTER_PLUS))))
                .GET()
                .build();

        MovieInfoDto movieInfo;
        HttpResponse<String> response;

        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();

        if (statusCode == 200) {

            if(response.body().contains(Constants.ERROR_MESSAGE))
                throw new NotFoundException(Constants.ERROR_MOVIE_NOT_FOUND, String.format(Constants.ERROR_MOVIE_NOT_FOUND_MSG, movieTitle));

            ObjectMapper objectMapper = new ObjectMapper();
            movieInfo = objectMapper.readValue(response.body(), MovieInfoDto.class);
        } else {
            throw new ServiceUnavailableException(Constants.ERROR_OMDB_API_FAILED, Constants.ERROR_OMDB_API_FAILED_MSG);
        }

        return movieInfo;
    }

    private Movie dtoToEntity(MovieInfoDto movieInfo, boolean oscarWon) {
        return Movie.builder()
                .imdId(movieInfo.imdbID())
                .title(movieInfo.title())
                .actors(movieInfo.actors())
                .genre(movieInfo.genre())
                .released(movieInfo.released())
                .awards(movieInfo.awards())
                .rating(new BigDecimal(movieInfo.imdbRating()))
                .votes(Long.parseLong(movieInfo.imdbVotes().replace(",", "")))
                .boxOffice(movieInfo.boxOffice())
                .oscarWon(oscarWon)
                .build();
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            throw new UserUnauthorizedException(Constants.ERROR_USER_NOT_AUTHENTICATED, Constants.ERROR_USER_NOT_AUTHENTICATED_MSG);

        var userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUserName(userDetails.getUsername()).orElse(null);
    }
}

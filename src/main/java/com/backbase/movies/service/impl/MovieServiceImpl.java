package com.backbase.movies.service.impl;

import com.backbase.movies.dto.MovieInfoDto;
import com.backbase.movies.dto.TopRatedMovie;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
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
    @Transactional
    public void saveRating(String title, int rating) {

        boolean isAlreadyRated = false;
        Movie movie;
        MovieRating movieRating = null;
        User activeUser = getAuthenticatedUser();

        Optional<Movie> movieOptional = movieRepository.findByTitle(title);

        if (movieOptional.isPresent()) {
            movie = movieOptional.get();
            Optional<MovieRating> oldRatingOptional = movieRatingRepository.findByUserAndMovie(activeUser, movie);

            if(oldRatingOptional.isPresent()) {
                movieRating = oldRatingOptional.get();
                isAlreadyRated = true;
            }
        } else {
            try {
                MovieInfoDto movieInfoDto = searchMovieByTitle(title);
                String result = AwardsInfoFileUploadUtil.getBestPictureAwardsMap().get(title);
                boolean awardWon = result != null && result.equals(Constants.YES);

                movie = dtoToEntity(movieInfoDto, awardWon);
            } catch (Exception e) {
                throw new GeneralException(Constants.GENERAL_ERROR, e.getMessage());
            }
        }

        long totalVotes = movie.getVotes();
        BigDecimal oldRating = movie.getRating();
        BigDecimal updatedRating = oldRating.multiply(BigDecimal.valueOf(totalVotes)).add(BigDecimal.valueOf(rating));

        // If already rated the same movie then reducing counter by 1 and subtracting old rating from total rating
        if(isAlreadyRated) {
            totalVotes -= 1;
            updatedRating = updatedRating.subtract(BigDecimal.valueOf(movieRating.getRating()));
            movieRating.setRating(rating);
        } else {
            movieRating = MovieRating.builder()
                    .rating(rating)
                    .movie(movie)
                    .user(getAuthenticatedUser())
                    .build();
        }

        movieRatingRepository.save(movieRating);

        long updatedVotes = totalVotes + 1;
        movie.setRating(updatedRating.divide(BigDecimal.valueOf(updatedVotes), 2, RoundingMode.HALF_UP)
                .setScale(1, RoundingMode.HALF_UP));
        movie.setVotes(updatedVotes);

        movieRepository.save(movie);
    }

    @Override
    public List<TopRatedMovie> topRatedMovies() {
        List<Movie> top10Movies = movieRepository.findTop10ByOrderByRatingDescBoxOfficeDesc();

        return top10Movies.stream()
                .map(this::entityToDto)
                .toList();
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

        if (statusCode == Constants.STATUS_CODE_SUCCESS) {

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
                .boxOffice(convertBoxOfficeValueToLong(movieInfo.boxOffice()))
                .oscarWon(oscarWon)
                .build();
    }

    private TopRatedMovie entityToDto(Movie movie) {
        return new TopRatedMovie(movie.getImdId(), movie.getTitle(), movie.getRating(), longToBoxOffice(movie.getBoxOffice()), movie.getOscarWon());
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            throw new UserUnauthorizedException(Constants.ERROR_USER_NOT_AUTHENTICATED, Constants.ERROR_USER_NOT_AUTHENTICATED_MSG);

        var userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUserName(userDetails.getUsername()).orElse(null);
    }

    private long convertBoxOfficeValueToLong(String boxOffice) {
        return switch (boxOffice.strip()) {
            case Constants.NOT_APPLICABLE -> 0L;
            default -> Long.parseLong(boxOffice.replace("$", "").replace(",", ""));
        };
    }

    private String longToBoxOffice(long boxOffice) {
        if(boxOffice == 0L)
            return Constants.NOT_APPLICABLE;
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return Constants.DOLLAR_SIGN+numberFormat.format(boxOffice);
    }
}

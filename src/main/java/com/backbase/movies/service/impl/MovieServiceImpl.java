package com.backbase.movies.service.impl;

import com.backbase.movies.dto.MovieInfoDto;
import com.backbase.movies.entity.MovieInfo;
import com.backbase.movies.entity.MovieRating;
import com.backbase.movies.entity.User;
import com.backbase.movies.exception.NotFoundException;
import com.backbase.movies.exception.ServiceUnavailableException;
import com.backbase.movies.repository.MovieInfoRepository;
import com.backbase.movies.repository.MovieRatingRepository;
import com.backbase.movies.repository.UserRepository;
import com.backbase.movies.service.MovieService;
import com.backbase.movies.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

    @Value("${OMDB_API_KEY:NoKey}")
    private String OMDB_API_KEY;
    private final MovieInfoRepository movieInfoRepository;
    private final MovieRatingRepository movieRatingRepository;
    private final UserRepository userRepository;

    public MovieServiceImpl(MovieInfoRepository movieInfoRepository, MovieRatingRepository movieRatingRepository
            , UserRepository userRepository) {
        this.movieInfoRepository = movieInfoRepository;
        this.movieRatingRepository = movieRatingRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void importDataFromCSV(String filePath) throws Exception {
        List<MovieInfo> entities = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int count = 0;

            while ((line = reader.readLine()) != null) {

                // Ignoring title line
                if(line.startsWith(Constants.HEADER_COLUMN_YEAR))
                    continue;
                // Parse and process CSV line
                entities.add(parseCSVLine(line));
                count++;

                if (count % Constants.BATCH_SIZE == 0) {
                    movieInfoRepository.saveAll(entities);
                    entities.clear();
                }
            }
        }

        // Save any remaining entities
        if (!entities.isEmpty()) {
            movieInfoRepository.saveAll(entities);
        }
    }

    @Override
    public MovieInfoDto searchMovieByTitle(String title) throws Exception {

        if(OMDB_API_KEY.isBlank() || OMDB_API_KEY.equals(Constants.OMDB_API_DEFAULT_NO_KEY))
            throw new NotFoundException(Constants.ERROR_OMDB_API_KEY_NOT_FOUND, Constants.ERROR_OMDB_API_KEY_NOT_FOUND_MSG);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format(Constants.OMDB_API_URL, OMDB_API_KEY, title)))
                .GET()
                .build();

        MovieInfoDto movieInfo;
        HttpResponse<String> response;

        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();

        if (statusCode == 200) {

            if(response.body().contains(Constants.ERROR_MESSAGE))
                throw new NotFoundException(Constants.ERROR_MOVIE_NOT_FOUND, String.format(Constants.ERROR_MOVIE_NOT_FOUND_MSG, title));

            ObjectMapper objectMapper = new ObjectMapper();
            movieInfo = objectMapper.readValue(response.body(), MovieInfoDto.class);
        } else {
            throw new ServiceUnavailableException(Constants.ERROR_OMDB_API_FAILED, Constants.ERROR_OMDB_API_FAILED_MSG);
        }

        return movieInfo;
    }

    @Override
    public void saveRating(String title, double rating) throws Exception {

        //MovieInfoDto movieInfo = searchMovieByTitle(title);
        MovieRating movieRating = MovieRating.builder()
                .rating(rating)
                .movieId(1l)
                .user(getAuthenticatedUser())
                .build();
        movieRatingRepository.save(movieRating);
    }

    private MovieInfo parseCSVLine(String line) {
        String[] fields = line.split(Constants.COMMA_SEPARATOR);

        return MovieInfo.builder()
                .year(fields[Constants.INDEX_ZERO])
                .category(fields[Constants.INDEX_ONE])
                .nominee(fields[Constants.INDEX_TWO])
                .additionalInfo(fields[Constants.INDEX_THREE])
                .oscarWon(fields[Constants.INDEX_FOUR])
                .build();
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        var userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUserName(userDetails.getUsername()).orElse(null);
    }
}

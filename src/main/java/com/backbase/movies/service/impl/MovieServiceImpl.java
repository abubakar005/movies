package com.backbase.movies.service.impl;

import com.backbase.movies.dto.MovieInfoDto;
import com.backbase.movies.entity.MovieInfo;
import com.backbase.movies.repository.MovieInfoRepository;
import com.backbase.movies.service.MovieService;
import com.backbase.movies.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieInfoRepository movieInfoRepository;

    public MovieServiceImpl(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    @Override
    @Transactional
    public void importDataFromCSV(String filePath) throws IOException {
        List<MovieInfo> entities = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int count = 0;

            while ((line = reader.readLine()) != null) {

                // Ignoring title line
                if(line.startsWith("Year"))
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

    // taking more time ....
    @Transactional
    public void importDataFromCSV1(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<MovieInfo> entities = reader.lines()
                    .parallel() // Enable parallel processing
                    .map(this::parseCSVLine)
                    .collect(Collectors.toList());

            ConcurrentLinkedQueue<MovieInfo> queue = new ConcurrentLinkedQueue<>(entities);
            int batchSize = 1000;

            while (!queue.isEmpty()) {
                List<MovieInfo> batch = queue.stream()
                        .limit(batchSize)
                        .collect(Collectors.toList());

                movieInfoRepository.saveAll(batch);
                queue.removeAll(batch);
            }
        }
    }

    private MovieInfo parseCSVLine(String line) {
        String[] fields = line.split(Constants.COMMA_SEPARATOR);

        return MovieInfo.builder()
                .year(fields[0])
                .category(fields[1])
                .nominee(fields[2])
                .additionalInfo(fields[3])
                .oscarWon(fields[4])
                .build();
    }

    @Override
    public MovieInfoDto searchMovieByTitle(String title) {

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://www.omdbapi.com/?apikey=23470fee&t="+title))
                .GET()
                .build();

        System.out.println("Request:: "+request);

        MovieInfoDto customDTO = null;

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();

            System.out.println("Status Code: " + statusCode);
            System.out.println("Response Body: " + responseBody);

            if (statusCode == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                customDTO = objectMapper.readValue(responseBody, MovieInfoDto.class);

                // Access the customDTO object and perform further operations
                System.out.println("Response DTO: " + customDTO.toString());
            } else {
                System.out.println("Request failed with status code: " + statusCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return customDTO;
    }
}

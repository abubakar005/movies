package com.backbase.movies.service;

import com.backbase.movies.dto.MovieInfoDto;


public interface MovieService {
    void importDataFromCSV(String filePath) throws Exception;
    MovieInfoDto searchMovieByTitle(String title) throws Exception;
    void saveRating(String title, double rating) throws Exception;
}

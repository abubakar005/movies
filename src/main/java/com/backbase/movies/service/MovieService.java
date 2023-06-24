package com.backbase.movies.service;

import com.backbase.movies.dto.MovieInfoDto;

import java.io.IOException;

public interface MovieService {
    void importDataFromCSV(String filePath) throws IOException;
    MovieInfoDto searchMovieByTitle(String title);
}

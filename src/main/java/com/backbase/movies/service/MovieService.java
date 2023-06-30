package com.backbase.movies.service;

import com.backbase.movies.dto.MovieInfoDto;


public interface MovieService {
    String hasWonBestPictureAward(String movieTitle);
    void saveRating(String title, int rating);
}

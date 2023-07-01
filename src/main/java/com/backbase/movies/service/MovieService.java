package com.backbase.movies.service;


import com.backbase.movies.dto.TopRatedMovie;

import java.util.List;

public interface MovieService {
    String hasWonBestPictureAward(String movieTitle);
    void saveRating(String title, int rating);
    List<TopRatedMovie> topRatedMovies();
}

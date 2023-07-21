package com.backbase.movies.repository;

import com.backbase.movies.entity.Movie;
import com.backbase.movies.entity.MovieRating;
import com.backbase.movies.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRatingRepository extends JpaRepository<MovieRating, Long> {
    Optional<MovieRating> findByUserAndMovie(User user, Movie movie);
}

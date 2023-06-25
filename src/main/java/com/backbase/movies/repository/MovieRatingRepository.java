package com.backbase.movies.repository;

import com.backbase.movies.entity.MovieRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRatingRepository extends JpaRepository<MovieRating, Long>, JpaSpecificationExecutor<MovieRating> {
}

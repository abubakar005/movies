package com.backbase.movies.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record MovieInfoDto( @JsonProperty("Title") String title,
                            @JsonProperty("Year") String year,
                            @JsonProperty("Rated") String rated,
                            @JsonProperty("Released") String released,
                            @JsonProperty("Runtime") String runtime,
                            @JsonProperty("Genre") String genre,
                            @JsonProperty("Director") String director,
                            @JsonProperty("Writer") String writer,
                            @JsonProperty("Actors") String actors,
                            @JsonProperty("Plot") String plot,
                            @JsonProperty("Language") String language,
                            @JsonProperty("Country") String country,
                            @JsonProperty("Awards") String awards,
                            @JsonProperty("Poster") String poster,
                            @JsonProperty("Ratings") List<Rating> ratings,
                            @JsonProperty("Metascore") String metaScore,
                            String imdbRating,
                            String imdbVotes,
                            String imdbID,
                            @JsonProperty("Type") String type,
                            @JsonProperty("DVD") String dVD,
                            @JsonProperty("BoxOffice") String boxOffice,
                            @JsonProperty("Production") String production,
                            @JsonProperty("Website") String website,
                            @JsonProperty("Response") String response) {
}
package com.backbase.movies.dto;

import java.math.BigDecimal;

public record TopRatedMovie(String imdId, String title, BigDecimal rating, String boxOffice, Boolean oscarWon) {
}

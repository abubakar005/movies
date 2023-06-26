package com.backbase.movies.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Rating(@JsonProperty("Source") String source,  @JsonProperty("Value") String value) {
}

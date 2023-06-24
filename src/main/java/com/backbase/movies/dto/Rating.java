package com.backbase.movies.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rating {
    @JsonProperty("Source")
    public String source;
    @JsonProperty("Value")
    public String value;
}

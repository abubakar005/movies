package com.backbase.movies.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This DTO is for sending error message in case of any Exception
 * Used Lombok library to have all methods required through annotations and precise code
 * */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo {

    private int code;
    private String message;

    public ErrorInfo(int code) {
        this.code = code;
    }

    public ErrorInfo(String message) {
        this.message = message;
    }
}

package com.backbase.movies.util;

/**
 * This is the Constant class where all the static data available including error messages and Regex
 * */

public final class Constants {

    public static final String AWARDS_INFO_FILE_PATH = System.getProperty("user.dir")+"/src/main/resources/academy_awards.csv";
    public static final String COMMA_SEPARATOR = ",";
    public static final String DOLLAR_SIGN = "$";
    public static final String NOT_APPLICABLE = "N/A";
    public static final int BATCH_SIZE = 1000;
    public static final String OMDB_API_URL = "http://www.omdbapi.com/?apikey=%s&t=%s";
    public static final String OMDB_API_DEFAULT_NO_KEY = "NoKey";
    public static final String ERROR_MESSAGE = "Error";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String TOKEN_BEARER = "Bearer ";
    public static final String YES = "YES";
    public static final String CHARACTER_SPACE = " ";
    public static final String CHARACTER_PLUS = "+";
    public static final String CATEGORY_BEST_PICTURE = "Best Picture";
    public static final String ERROR_MOVIE_WON_AWARD_MSG = "'%s' has won a best picture award";
    public static final String ERROR_MOVIE_NOT_WON_AWARD_MSG = "'%s' has not won a best picture award";
    public static final int STATUS_CODE_SUCCESS = 200;
    public static final int INDEX_ZERO = 0;
    public static final int INDEX_ONE = 1;
    public static final int INDEX_TWO = 2;
    public static final int INDEX_THREE = 3;
    public static final int INDEX_FOUR = 4;
    public static final int INDEX_SEVEN = 7;

    // Error codes and messages
    public static final int ERROR_MOVIE_NOT_FOUND = 101;
    public static final String ERROR_MOVIE_NOT_FOUND_MSG = "Movie not found against the title %s";
    public static final int ERROR_OMDB_API_FAILED = 102;
    public static final String ERROR_OMDB_API_FAILED_MSG = "OMDB Api is not responding or unavailable";
    public static final int ERROR_OMDB_API_KEY_NOT_FOUND = 103;
    public static final String ERROR_OMDB_API_KEY_NOT_FOUND_MSG = "OMDB Api key is not found. Please provide in the config property file";
    public static final int ERROR_DUPLICATE_USER_NAME = 104;
    public static final String ERROR_DUPLICATE_USER_NAME_MSG = "Username is already exist!";
    public static final int ERROR_DUPLICATE_USER_EMAIL = 105;
    public static final String ERROR_DUPLICATE_USER_EMAIL_MSG = "User email is already exist!";
    public static final int ERROR_USER_NOT_AUTHENTICATED = 106;
    public static final String ERROR_USER_NOT_AUTHENTICATED_MSG = "User not authenticated!";
    public static final int GENERAL_ERROR = 107;
    public static final String ERROR_USER_TOKEN_EXPIRED_MSG = "Token has expired!";
    public static final int ERROR_FILE_PARSING_FAILED = 108;
    public static final String ERROR_FILE_PARSING_FAILED_MSG = "File parsing error!";
    public static final String ERROR_INVALID_USER_REQUEST_MSG = "invalid user request !";
}

package com.backbase.movies.util;

/**
 * This is the Constant class where all the static data available including error messages and Regex
 * */

public final class Constants {

    public static final String COMMA_SEPARATOR = ",";
    public static final int BATCH_SIZE = 1000;
    public static final String OMDB_API_URL = "http://www.omdbapi.com/?apikey=%s&t=%s";
    public static final String OMDB_API_DEFAULT_NO_KEY = "NoKey";
    public static final String ERROR_MESSAGE = "Error";
    public static final String HEADER_COLUMN_YEAR = "Year";
    public static final int INDEX_ZERO = 0;
    public static final int INDEX_ONE = 1;
    public static final int INDEX_TWO = 2;
    public static final int INDEX_THREE = 3;
    public static final int INDEX_FOUR = 4;



    public static final String REGEX_ITEM = "(?<index>\\d+),(?<weight>\\d+\\.\\d+),€(?<cost>\\d+)";
    public static final String REGEX_LINE = String.format("(\\d+) : ((\\(%s)\\s*\\)+)", REGEX_ITEM);
    public static final String REGEX_ITEM_2 = "\\((\\d+),([\\d.]+),€(\\d+)\\)";




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



    public static final String ERROR_FILE_PATH_REQUIRED = "File path is required!";
    public static final String ERROR_FILE_NOT_FOUND = "Invalid path. Please provide valid file path";

    public static final String ERROR_COST_NOT_ALLOWED = "Cost of an item can not be greater than 100";
    public static final String ERROR_WEIGHT_NOT_ALLOWED = "Weight can not be greater than 100";
    public static final String ERROR_ITEMS_NOT_ALLOWED = "Items can not be more than 15";
}

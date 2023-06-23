package com.mobiquity.util;

/**
 * This is the Constant class where all the static data available including error messages and Regex
 * */

public final class Constants {

    public static final String REGEX_ITEM = "(?<index>\\d+),(?<weight>\\d+\\.\\d+),€(?<cost>\\d+)";
    public static final String REGEX_LINE = String.format("(\\d+) : ((\\(%s)\\s*\\)+)", REGEX_ITEM);
    public static final String REGEX_ITEM_2 = "\\((\\d+),([\\d.]+),€(\\d+)\\)";
    public static final String REGEX_COLON = ":";
    public static final int MAX_WEIGHT_ALLOWED = 100;
    public static final int MAX_COST_ALLOWED = 100;
    public static final int INDEX_ZERO = 0;
    public static final int INDEX_ONE = 1;
    public static final String DATA_INDEX = "index";
    public static final String DATA_WEIGHT = "weight";
    public static final String DATA_COST = "cost";
    public static final String DELIMITER_COMMA_SPACE = ", ";
    public static final String DASH = "-";
    public static final int MAX_ITEM_SIZE = 15;

    public static final String ERROR_FILE_PATH_REQUIRED = "File path is required!";
    public static final String ERROR_FILE_NOT_FOUND = "Invalid path. Please provide valid file path";
    public static final String ERROR_INVALID_DATA_FORMAT = "Data is invalid : %s";
    public static final String ERROR_COST_NOT_ALLOWED = "Cost of an item can not be greater than 100";
    public static final String ERROR_WEIGHT_NOT_ALLOWED = "Weight can not be greater than 100";
    public static final String ERROR_ITEMS_NOT_ALLOWED = "Items can not be more than 15";
}

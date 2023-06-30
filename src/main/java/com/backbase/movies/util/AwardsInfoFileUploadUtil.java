package com.backbase.movies.util;

import com.backbase.movies.exception.NotFoundException;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public final class AwardsInfoFileUploadUtil {

    private static final Map<String, String> BEST_PICTURE_AWARD_STATUS = new HashMap<>();

    public static void loadBestPictureAwardsMap() {

        try (CSVReader reader = new CSVReader(new FileReader(Constants.AWARDS_INFO_FILE_PATH))) {
            String[] line;

            // Ignoring Header line
            reader.readNext();

            while ((line = reader.readNext()) != null) {
                // Process column values in the line (saving only best movie records)
                if(line[Constants.INDEX_ONE].equals(Constants.CATEGORY_BEST_PICTURE))
                    BEST_PICTURE_AWARD_STATUS.put(line[Constants.INDEX_TWO], line[Constants.INDEX_FOUR]);
            }
        } catch (Exception e) {
            throw new NotFoundException(Constants.ERROR_FILE_PARSING_FAILED, Constants.ERROR_FILE_PARSING_FAILED_MSG);
        }
    }

    public static Map<String, String> getBestPictureAwardsMap() {
        return BEST_PICTURE_AWARD_STATUS;
    }
}

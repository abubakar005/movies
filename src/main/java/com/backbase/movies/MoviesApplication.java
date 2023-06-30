package com.backbase.movies;

import com.backbase.movies.util.AwardsInfoFileUploadUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoviesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesApplication.class, args);
		AwardsInfoFileUploadUtil.loadBestPictureAwardsMap();
	}
}

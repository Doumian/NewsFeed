package com.example.NewsFeed;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The type News feed application.
 */
@Slf4j
@SpringBootApplication
public class NewsFeedApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		SpringApplication.run(NewsFeedApplication.class, args);
	}

}

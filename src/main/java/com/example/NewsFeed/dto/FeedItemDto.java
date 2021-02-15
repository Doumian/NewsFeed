package com.example.NewsFeed.dto;

import lombok.Data;

import java.time.LocalDateTime;


/**
 * The type Feed item dto.
 * @author dlarena
 */
@Data
public class FeedItemDto {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime publicationDate;

    private byte[] image;

}

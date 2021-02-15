package com.example.NewsFeed.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedItemDto {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime publicationDate;

    private byte[] image;

}

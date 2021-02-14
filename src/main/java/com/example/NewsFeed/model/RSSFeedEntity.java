package com.example.NewsFeed.model;

import lombok.Data;

import java.util.List;

@Data
public class RSSFeedEntity {
    private List<FeedItemEntity> feed;
}

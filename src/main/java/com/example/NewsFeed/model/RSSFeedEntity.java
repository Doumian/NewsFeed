package com.example.NewsFeed.model;

import lombok.Data;

import java.util.List;

/**
 * The type Rss feed entity.
 */
@Data
public class RSSFeedEntity {
    private List<FeedItemEntity> feed;
}

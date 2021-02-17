package com.example.NewsFeed.model;

import lombok.Data;

import java.util.List;

/**
 * The type Rss feed entity.
 */
@Data
public class FeedEntity {
    private List<NewEntity> feed;
}

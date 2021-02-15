package com.example.NewsFeed.service;

import com.example.NewsFeed.dto.FeedItemDto;
import java.util.List;

/**
 * The interface Feed service.
 */
public interface FeedService {

    /**
     * Gets all.
     *
     * @return the all
     */
    List<FeedItemDto> getAll();

}

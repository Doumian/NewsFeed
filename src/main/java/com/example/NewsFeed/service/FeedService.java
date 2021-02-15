package com.example.NewsFeed.service;

import com.example.NewsFeed.dto.FeedItemDto;
import java.util.List;

public interface FeedService {

    List<FeedItemDto> getAll();

}

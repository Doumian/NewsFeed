package com.example.NewsFeed.service;

import com.example.NewsFeed.model.dto.NewDto;
import java.util.List;

/**
 * The interface Feed service.
 */
public interface NewService {

    /**
     * Gets all.
     *
     * @return the all
     */
    List<NewDto> getAll();

}

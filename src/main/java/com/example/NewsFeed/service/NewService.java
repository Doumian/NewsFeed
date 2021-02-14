package com.example.NewsFeed.service;

import com.example.NewsFeed.dto.NewDto;
import java.util.List;

public interface NewService {

    List<NewDto> getAll();

}

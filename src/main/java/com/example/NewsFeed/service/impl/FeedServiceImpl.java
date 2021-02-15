package com.example.NewsFeed.service.impl;


import com.example.NewsFeed.dto.FeedItemDto;
import com.example.NewsFeed.repository.FeedRepository;
import com.example.NewsFeed.service.FeedService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedServiceImpl implements FeedService {

    @Autowired
    private FeedRepository feedRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<FeedItemDto> getAll() {
       return feedRepository.findAll().stream()
               .map(source -> modelMapper.map(source, FeedItemDto.class))
               .collect(Collectors.toList());
    }

}

package com.example.NewsFeed.service.impl;


import com.example.NewsFeed.dto.NewDto;
import com.example.NewsFeed.repository.NewRepository;
import com.example.NewsFeed.service.NewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Feed service.
 */
@Service
public class NewServiceImpl implements NewService {

    @Autowired
    private NewRepository newRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<NewDto> getAll() {
       return newRepository.findAll().stream()
               .map(source -> modelMapper.map(source, NewDto.class))
               .collect(Collectors.toList());
    }

}

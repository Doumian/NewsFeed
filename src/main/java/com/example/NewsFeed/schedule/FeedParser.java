package com.example.NewsFeed.schedule;

import com.example.NewsFeed.dto.NewDto;
import com.example.NewsFeed.exception.CustomParsingException;
import com.example.NewsFeed.exception.InputSourceException;
import com.example.NewsFeed.exception.XmlReaderException;

import java.util.List;

public interface FeedParser {

    List<NewDto> parse(String url) throws XmlReaderException, InputSourceException, CustomParsingException;

}

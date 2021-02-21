package com.example.NewsFeed.scheduled.task;

import com.example.NewsFeed.model.dto.NewDto;
import com.example.NewsFeed.exception.CustomParsingException;
import com.example.NewsFeed.exception.InputSourceException;
import com.example.NewsFeed.exception.XmlReaderException;

import java.util.List;

/**
 * The interface Feed parser.
 */
public interface FeedParser {

    /**
     * Parse list.
     *
     * @param url the url
     * @return the list
     * @throws XmlReaderException     If reading the XML fails
     * @throws InputSourceException   If preparing the input source fails
     * @throws CustomParsingException If parsing the XML data fails
     */
    List<NewDto> parse(String url) throws XmlReaderException, InputSourceException, CustomParsingException;

}

package com.example.NewsFeed.schedule;

import com.example.NewsFeed.dto.NewDto;
import com.example.NewsFeed.exception.InputSourceException;
import com.example.NewsFeed.exception.CustomParsingException;
import com.example.NewsFeed.exception.XMLReaderException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface CustomParser {

    List<NewDto> parse(String url) throws ParserConfigurationException, SAXException, IOException, XMLReaderException, InputSourceException, CustomParsingException;

    Integer storeInDb(List<NewDto> newDtos);

}

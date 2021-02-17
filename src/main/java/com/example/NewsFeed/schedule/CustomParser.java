package com.example.NewsFeed.schedule;

import com.example.NewsFeed.model.FeedItemEntity;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface CustomParser {

    public List<FeedItemEntity> parseXMLFromURL(String url) throws ParserConfigurationException, SAXException, IOException;

}

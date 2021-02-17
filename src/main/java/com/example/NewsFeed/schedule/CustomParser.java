package com.example.NewsFeed.schedule;

import com.example.NewsFeed.model.NewEntity;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface CustomParser {

    List<NewEntity> parse(String url) throws ParserConfigurationException, SAXException, IOException;

    Integer storeInDb(List<NewEntity> feedItemEntities);

}

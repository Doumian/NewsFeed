package com.example.NewsFeed.schedule.impl;

import com.example.NewsFeed.handler.CustomFeedHandler;
import com.example.NewsFeed.model.NewEntity;
import com.example.NewsFeed.repository.NewRepository;
import com.example.NewsFeed.schedule.CustomParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.transaction.Transactional;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Component
public class CustomParserImpl implements CustomParser {

    @Autowired
    private NewRepository newRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<NewEntity> parse(String feedUrl) throws ParserConfigurationException, SAXException, IOException {

        CustomFeedHandler customFeedHandler = new CustomFeedHandler();
        XMLReader reader = getXmlReader();
        InputSource inputSource = getInputSource(feedUrl);

        reader.setContentHandler(customFeedHandler);
        reader.parse(inputSource);

        return customFeedHandler.getRssFeedEntity().getFeed();
    }

    private XMLReader getXmlReader() throws ParserConfigurationException, SAXException {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        return reader;
    }


    private InputSource getInputSource(String feedUrl) throws IOException {
        URL url = new URL(feedUrl);
        InputSource inputSource = new InputSource(url.openStream());
        return inputSource;
    }

    @Override
    @Transactional
    public Integer storeInDb(List<NewEntity> newList) {
        List<NewEntity> validatedList = checkDuplicatesInList(newList);
        return newRepository.saveAll(validatedList).size();
    }

    private List<NewEntity> checkDuplicatesInList(List<NewEntity> newList) {
        newList.forEach(newEntity -> {
            Optional<NewEntity> existingNew = newRepository.findById(newEntity.getId());
            if(existingNew.isPresent() && existingNew.get().equals(newEntity)) newList.remove(newEntity);
        });
        return newList;
    }
}

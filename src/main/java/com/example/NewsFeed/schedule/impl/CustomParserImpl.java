package com.example.NewsFeed.schedule.impl;

import com.example.NewsFeed.dto.NewDto;
import com.example.NewsFeed.exception.InputSourceException;
import com.example.NewsFeed.exception.CustomParsingException;
import com.example.NewsFeed.exception.XMLReaderException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CustomParserImpl implements CustomParser {

    @Autowired
    private NewRepository newRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<NewDto> parse(String feedUrl) throws XMLReaderException, InputSourceException, CustomParsingException {

        CustomFeedHandler customFeedHandler = new CustomFeedHandler();

        InputSource inputSource = setInputSource(feedUrl);
        XMLReader reader = setXmlReader();

        reader.setContentHandler(customFeedHandler);

        try {
            reader.parse(inputSource);
        } catch(SAXException | IOException ex){
            throw new CustomParsingException("Something went wrong while parsing the XML data: " + ex);

        }
        return customFeedHandler.getRssFeedEntity();
    }



    private XMLReader setXmlReader() throws XMLReaderException {
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            return reader;
        }catch(SAXException | ParserConfigurationException ex){
            throw new XMLReaderException("Something went wrong setting up the XML Reader: " + ex);
        }
    }


    private InputSource setInputSource(String feedUrl) throws InputSourceException {
        try {
            URL url = new URL(feedUrl);
            InputSource inputSource = new InputSource(url.openStream());
            return inputSource;
        } catch(IOException ex) {
            throw new InputSourceException("Something went wrong setting up the Input Source, please check that the URL is well formed: " + ex);
        }
    }

    @Override
    @Transactional
    public Integer storeInDb(List<NewDto> newList) {
        List<NewEntity> newEntityList = parseListToEntity(newList);
        List<NewEntity> validatedList = validateList(newEntityList);
        return newRepository.saveAll(validatedList).size();
    }

    private List<NewEntity> validateList(List<NewEntity> newList) {
        List<NewEntity> validatedList = new ArrayList<>();
        newList.forEach(newEntity -> {
            Optional<NewEntity> existingNew = newRepository.findById(newEntity.getId());
            if(!existingNew.isPresent() || (existingNew.isPresent() && !existingNew.get().equals(newEntity))) validatedList.add(newEntity);
        });
        return validatedList;
    }

    private List<NewEntity> parseListToEntity(List<NewDto> newDtoList){
        return newDtoList.stream()
                .map(source -> modelMapper.map(source, NewEntity.class))
                .collect(Collectors.toList());
    }
}

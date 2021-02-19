package com.example.NewsFeed.schedule.impl;

import com.example.NewsFeed.dto.NewDto;
import com.example.NewsFeed.exception.CustomParsingException;
import com.example.NewsFeed.exception.InputSourceException;
import com.example.NewsFeed.exception.XmlReaderException;
import com.example.NewsFeed.handler.CustomFeedHandler;
import com.example.NewsFeed.schedule.FeedParser;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Component
public class FeedSaxParserImpl implements FeedParser {

    private static final String XML_PARSING_ERROR = "Something went wrong while parsing the XML data: ";
    private static final String SET_INPUT_SOURCE_ERROR = "Something went wrong setting up the Input Source, please check that the URL is well formed: ";
    private static final String SET_XML_ERROR = "Something went wrong setting up the XML Reader: ";


    @Override
    public List<NewDto> parse(String feedUrl) throws XmlReaderException, InputSourceException, CustomParsingException {

        CustomFeedHandler customFeedHandler = new CustomFeedHandler();

        InputSource inputSource = setInputSource(feedUrl);
        XMLReader reader = setXmlReader();

        reader.setContentHandler(customFeedHandler);

        try {
            reader.parse(inputSource);
        } catch(SAXException | IOException ex){
            throw new CustomParsingException(XML_PARSING_ERROR+ ex);
        }
        return customFeedHandler.getRssFeedEntity();
    }


    private XMLReader setXmlReader() throws XmlReaderException {
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            return parser.getXMLReader();
        }catch(SAXException | ParserConfigurationException ex){
            throw new XmlReaderException(SET_XML_ERROR + ex);
        }
    }


    private InputSource setInputSource(String feedUrl) throws InputSourceException {
        try {
            URL url = new URL(feedUrl);
            return new InputSource(url.openStream());
        } catch(IOException ex) {
            throw new InputSourceException(SET_INPUT_SOURCE_ERROR + ex);
        }
    }


}

package com.example.NewsFeed.scheduled.task.impl;

import com.example.NewsFeed.exception.CustomParsingException;
import com.example.NewsFeed.exception.InputSourceException;
import com.example.NewsFeed.exception.XmlReaderException;
import com.example.NewsFeed.model.dto.NewDto;
import com.example.NewsFeed.scheduled.task.FeedParser;
import com.example.NewsFeed.scheduled.task.handler.CustomFeedHandler;
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


/**
 * The type Feed sax parser.
 */
@Component
public class FeedSaxParserImpl implements FeedParser {

    private static final String XML_SAX_PARSING_ERROR = "Something went wrong while parsing the XML data: ";
    private static final String SET_INPUT_SOURCE_ERROR = "Something went wrong setting up the Input Source: ";
    private static final String SET_XML_CONFIG_ERROR = "Something went wrong setting up XML parser configuration: ";

    /**
     *  Parse the XML data retrieved from the URL
     * @param feedUrl XML data url
     * @return list to persist
     * @throws XmlReaderException     If reading the XML fails
     * @throws InputSourceException   If preparing the input source fails
     * @throws CustomParsingException If parsing the XML data fails
     */

    @Override
    public List<NewDto> parse(String feedUrl) throws CustomParsingException, InputSourceException, XmlReaderException {

        CustomFeedHandler customFeedHandler = new CustomFeedHandler();

        try {

            InputSource inputSource = setInputSource(feedUrl);
            XMLReader reader = setXmlReader();
            reader.setContentHandler(customFeedHandler);
            reader.parse(inputSource);


        } catch (ParserConfigurationException ex) {
            throw new CustomParsingException(SET_XML_CONFIG_ERROR + ex);
        } catch(SAXException ex){
            throw new XmlReaderException(XML_SAX_PARSING_ERROR + ex);
        } catch(IOException ex){
            throw new InputSourceException(SET_INPUT_SOURCE_ERROR + ex);
        }

        return customFeedHandler.getRssFeedEntity();
    }

    /**
     *
     * Sets up the XML reader
     *
     * @return the XMLReader
     * @throws SAXException
     * @throws ParserConfigurationException
     */

    private XMLReader setXmlReader() throws SAXException, ParserConfigurationException {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        return parser.getXMLReader();
    }

    /**
     *
     * Sets up the input source
     *
     * @param feedUrl URL to treat
     * @return The input source
     * @throws IOException If setting up the input source fails
     */

    private InputSource setInputSource(String feedUrl) throws IOException {
        URL url = new URL(feedUrl);
        return new InputSource(url.openStream());
    }


}

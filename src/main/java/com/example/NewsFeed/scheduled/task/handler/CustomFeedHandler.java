package com.example.NewsFeed.scheduled.task.handler;

import com.example.NewsFeed.exception.CustomParsingException;
import com.example.NewsFeed.exception.InputSourceException;
import com.example.NewsFeed.model.dto.NewDto;
import lombok.SneakyThrows;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The type Custom feed handler.
 */
public class CustomFeedHandler extends DefaultHandler {

    private StringBuilder chars = new StringBuilder();

    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String PUB_DATE = "pubDate";
    private static final String IMAGE = "enclosure";
    private static final String GUID = "guid";
    private static final String URL = "url";

    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ssZ";
    private static final String PATTERN = "\\d+(?!.*\\d)";

    private static final String DATE_PARSING_ERROR = "Something went wrong while parsing the Publication Date, it may be malformed or non parseable: ";
    private static final String GUID_PARSING_ERROR = "Something went wrong while extracting the GUID, non matching results: ";
    private static final String SET_INPUT_SOURCE_ERROR = "Something went wrong setting up the Input Source: ";


    private List<NewDto> feed;
    private String imageUrl;
    private Boolean limitReached = false;

    /**
     *
     * Method that chains the data between tags
     *
     * @param ch Char to chain
     * @param start Index start
     * @param length The length of it
     */

    @Override
    public void characters(char[] ch, int start, int length) {
        chars.append(ch, start, length);
    }

    /**
     * Initialize the feed
     */

    @Override
    public void startDocument() {
        feed = new ArrayList<>();
    }

    /**
     *
     * Method that evaluates the starting tags from the XML, and what to do in each case
     *
     * @param uri The Namespace URI mapped to the prefix
     * @param lName The qualified name (with prefix), or the empty string if qualified names are not available
     * @param qName Tag name
     * @param attr Posible attributes it may have
     */

    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) {
            chars.setLength(0);
            if(qName.equals(ITEM)){
                if(Boolean.FALSE.equals(limitReached)) {
                    feed.add(new NewDto());
                    if (feed.size() == 10) limitReached = true;
                }
            } else if(qName.equals(IMAGE)){
                imageUrl = attr.getValue(URL);
            }

    }

    /**
     *
     * Method that evaluates the ending tags from the XML, and what to do in each case
     *
     * @param uri The location of the content to be parsed.
     * @param localName Element in a document has a name as it is defined in the namespace
     * @param qName Tag name
     */

    @SneakyThrows
    @Override
    public void endElement(String uri, String localName, String qName) {
        if(!feed.isEmpty()) {
            switch (qName) {
                case TITLE:
                    latestArticle().setTitle(chars.toString());
                    break;
                case DESCRIPTION:
                    latestArticle().setDescription(chars.toString());
                    break;
                case PUB_DATE:
                    latestArticle().setPublicationDate(getLocalDateTime());
                    break;
                case IMAGE:
                    latestArticle().setImage(urlToByteArray(imageUrl));
                    break;
                case GUID:
                    latestArticle().setGuid(extractGuidFromUrl());
                    break;
            }
        }
    }

    /**
     *
     * Reads and parse from the current date data to a LocalDateTime
     *
     * @return The parsed date into LocalDateTime
     * @throws CustomParsingException If parsing the date fails
     */

    private LocalDateTime getLocalDateTime() throws CustomParsingException {
        try {
            SimpleDateFormat curFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
            Date publicationDate = curFormatter.parse(chars.toString());
            LocalDateTime date = LocalDateTime.ofInstant(publicationDate.toInstant(),
                    ZoneId.systemDefault());
            return date;
        } catch(ParseException ex){
            throw new CustomParsingException(DATE_PARSING_ERROR + ex);
        }
    }

    /**
     *
     * Retrieves the last new item from the feed
     *
     * @return the last managed new item
     */

    private NewDto latestArticle() {
        List<NewDto> articleList = feed;
        int latestArticleIndex = articleList.size() - 1;
        return articleList.get(latestArticleIndex);
    }

    /**
     * Gets rss feed entity.
     *
     * @return The list of new Items from the feed
     */
    public List<NewDto> getRssFeedEntity() {
        return feed;
    }


    /**
     *
     * Transform an image from a URL to a byte[] in order to be able to persist it in the db
     *
     * @param imageUrl The image origin
     * @return An image fully transformed to a byte[]
     * @throws IOException If something goes bad when reading and opening the Inputs
     * @throws InputSourceException If preparing the input source fails
     */

    private byte[] urlToByteArray(String imageUrl) throws IOException, InputSourceException {
        BufferedInputStream bis = null;
        try {
            URL url = new URL(imageUrl);
            bis = new BufferedInputStream(url.openConnection().getInputStream());
            return bis.readAllBytes();
        } catch (IOException ex){
            throw new InputSourceException(SET_INPUT_SOURCE_ERROR + ex);
        } finally {
            if(bis != null) bis.close();
        }
    }

    /**
     *
     * Extracts an Integer GUID to from the new URL in order to unique identify them
     *
     * @return Integer GUID
     * @throws CustomParsingException If the extraction fails
     */

    private Integer extractGuidFromUrl() throws CustomParsingException {
        try {
            Pattern pattern = Pattern.compile(PATTERN);
            Matcher matcher = pattern.matcher(chars.toString());
            matcher.find();
            return Integer.valueOf(matcher.group(0));
        } catch(IllegalStateException ex){
            throw new CustomParsingException(GUID_PARSING_ERROR + ex);
        }
    }

}

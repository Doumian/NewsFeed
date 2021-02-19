package com.example.NewsFeed.handler;

import com.example.NewsFeed.dto.NewDto;
import com.example.NewsFeed.exception.CustomParsingException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.math.NumberUtils;
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
 * Custom Handler for parsing the XML data
 * @author dlarena
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
    private static final String IMAGE_PARSING_ERROR = "Something went wrong while parsing the Image URL, it may be malformed or non parseable: ";
    private static final String GUID_PARSING_ERROR = "Something went wrong while extracting the GUID, non matching results: ";

    private List<NewDto> feed;
    private String imageUrl;
    private Boolean limitReached = false;

    /**
     * Receive notification of character data inside an element.
     *
     * @param ch - The characters.
     * @param start - The start position in the character array.
     * @param length - The number of characters to use from the character array.
     */

    @Override
    public void characters(char[] ch, int start, int length) {
        chars.append(ch, start, length);
    }

    /**
     * Starts the document, initializing the object RSSFeedEntity
     */

    @Override
    public void startDocument() {
        feed = new ArrayList<>();
    }

    /**
     *
     * Method that checks the starting element, and depending on the opener tag, creates new items in the list or sets the URL of the image
     *
     * @param uri - The Namespace URI, or the empty string if the element has no Namespace URI or if Namespace processing is not being performed.
     * @param lName - The local name (without prefix), or the empty string if Namespace processing is not being performed.
     * @param qName - The qualified name (with prefix), or the empty string if qualified names are not available.
     * @param attr - The attributes attached to the element. If there are no attributes, it shall be an empty Attributes object.
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
     * Method that checks the closing element, and depending on the closing tag, sets the data of them into the properties of the last article evaluated
     *
     * @param uri - The Namespace URI, or the empty string if the element has no Namespace URI or if Namespace processing is not being performed.
     * @param localName - The local name (without prefix), or the empty string if Namespace processing is not being performed.
     * @param qName - The qualified name (with prefix), or the empty string if qualified names are not available.
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


    private LocalDateTime getLocalDateTime() throws CustomParsingException {
        try {
            SimpleDateFormat curFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
            Date publicationDate = curFormatter.parse(chars.toString());
            return LocalDateTime.ofInstant(publicationDate.toInstant(),
                    ZoneId.systemDefault());
        } catch(ParseException ex){
            throw new CustomParsingException(DATE_PARSING_ERROR + ex);
        }
    }

    private NewDto latestArticle() {
        List<NewDto> articleList = feed;
        int latestArticleIndex = articleList.size() - 1;
        return articleList.get(latestArticleIndex);
    }

    public List<NewDto> getRssFeedEntity() {
        return feed;
    }


    private byte[] urlToByteArray(String imageUrl) throws CustomParsingException, IOException {
        BufferedInputStream bis = null;
        try {
            URL url = new URL(imageUrl);
            bis = new BufferedInputStream(url.openConnection().getInputStream());
            return bis.readAllBytes();
        } catch (IOException ex){
            throw new CustomParsingException(IMAGE_PARSING_ERROR + ex);
        } finally {
            if(bis != null) bis.close();
        }
    }


    private Integer extractGuidFromUrl() throws CustomParsingException {

        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(chars.toString());
        matcher.find();
        if (NumberUtils.isCreatable(matcher.group(0))) return Integer.valueOf(matcher.group(0));
        else throw new CustomParsingException(GUID_PARSING_ERROR + matcher.group(0));
    }

}

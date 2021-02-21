package com.example.NewsFeed.scheduled.task.handler;

import com.example.NewsFeed.exception.CustomParsingException;
import com.example.NewsFeed.exception.InputSourceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.ext.Attributes2Impl;
import org.xml.sax.helpers.AttributesImpl;

import java.io.IOException;
import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomFeedHandlerTest {

    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String PUB_DATE = "pubDate";
    private static final String IMAGE = "enclosure";
    private static final String GUID = "guid";

    private CustomFeedHandler customFeedHandlerUnderTest;

    @BeforeEach
    void setUp() {
        customFeedHandlerUnderTest = new CustomFeedHandler();
        // Setup
        customFeedHandlerUnderTest.startDocument();
        customFeedHandlerUnderTest.startElement("uri", "localName", ITEM, null);
    }


    @Test
    void testEndElement_title() {

        // Run the test
        String title = "testTitle";
        customFeedHandlerUnderTest.characters(title.toCharArray(), 0, title.toCharArray().length);
        customFeedHandlerUnderTest.endElement("uri", "localName", TITLE);

        // Verify the results

        assertThat(customFeedHandlerUnderTest.getRssFeedEntity().get(0).getTitle()).isEqualTo(title);
    }

    @Test
    void testEndElement_guid() {

        // Run the test
        String guidLink = "https://nos.nl/l/2369554";
        Integer guid = 2369554;
        customFeedHandlerUnderTest.characters(guidLink.toCharArray(), 0, guidLink.toCharArray().length);
        customFeedHandlerUnderTest.endElement("uri", "localName", GUID);

        // Verify the results

        assertThat(customFeedHandlerUnderTest.getRssFeedEntity().get(0).getGuid()).isEqualTo(guid);
    }

    @Test
    void testEndElement_guid_ThrowsCustomParsingException() {

        // Run the test
        String guid = "testGuid";
        customFeedHandlerUnderTest.characters(guid.toCharArray(), 0, guid.toCharArray().length);

        // Verify the results

        assertThatThrownBy(() -> customFeedHandlerUnderTest.endElement("uri", "localName", GUID)).isInstanceOf(CustomParsingException.class);

    }

    @Test
    void testEndElement_description() {

        // Run the test
        String description = "testDescription";
        customFeedHandlerUnderTest.characters(description.toCharArray(), 0, description.toCharArray().length);
        customFeedHandlerUnderTest.endElement("uri", "localName", DESCRIPTION);

        // Verify the results

        assertThat(customFeedHandlerUnderTest.getRssFeedEntity().get(0).getDescription()).isEqualTo(description);
    }

    @Test
    void testEndElement_date() throws ParseException {

        // Run the test
        String pubDate = "Sat, 20 Feb 2021 19:40:56 +0100";
        customFeedHandlerUnderTest.characters(pubDate.toCharArray(), 0, pubDate.toCharArray().length);
        customFeedHandlerUnderTest.endElement("uri", "localName", PUB_DATE);

        // Verify the results

        assertThat(customFeedHandlerUnderTest.getRssFeedEntity().get(0).getPublicationDate()).isNotNull();
    }

    @Test
    void testEndElement_date_ThrowsCustomParsingException() {

        // Run the test
        String pubDate = "testDate";
        customFeedHandlerUnderTest.characters(pubDate.toCharArray(), 0, pubDate.toCharArray().length);

        // Verify the results

        assertThatThrownBy(() -> customFeedHandlerUnderTest.endElement("uri", "localName", PUB_DATE)).isInstanceOf(CustomParsingException.class);

    }

    @Test
    void testEndElement_image() throws IOException {

        // Run the test
        String image = "https://cdn.nos.nl/image/2021/02/20/717091/1008x567.jpg";
        AttributesImpl attr = new Attributes2Impl();
        attr.addAttribute("uri","localName","url","",image);


        customFeedHandlerUnderTest.startElement("uri", "localName", IMAGE , attr);
        customFeedHandlerUnderTest.characters(image.toCharArray(), 0, image.toCharArray().length);
        customFeedHandlerUnderTest.endElement("uri", "localName", IMAGE);

        // Verify the results

        assertThat(customFeedHandlerUnderTest.getRssFeedEntity().get(0).getImage()).isNotNull();
    }

    @Test
    void testEndElement_image_ThrowsInputSourceException() {

        // Run the test
        String image = "testImage";
        customFeedHandlerUnderTest.characters(image.toCharArray(), 0, image.toCharArray().length);

        // Verify the results

        assertThatThrownBy(() -> customFeedHandlerUnderTest.endElement("uri", "localName", IMAGE)).isInstanceOf(InputSourceException.class);

    }


}

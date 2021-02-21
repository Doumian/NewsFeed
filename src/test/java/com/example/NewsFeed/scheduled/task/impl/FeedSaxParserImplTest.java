package com.example.NewsFeed.scheduled.task.impl;

import com.example.NewsFeed.exception.InputSourceException;
import com.example.NewsFeed.exception.XmlReaderException;
import com.example.NewsFeed.model.dto.NewDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FeedSaxParserImplTest {

    private static final String RSS_FEED_URL = "http://feeds.nos.nl/nosjournaal?format=xml.";
    private static final String BAD_RSS_FEED_URL = "http://www.w3schools.com/xml/plant_catalog.xml";
    private FeedSaxParserImpl feedSaxParserImplUnderTest;

    @BeforeEach
    void setUp() {
        feedSaxParserImplUnderTest = new FeedSaxParserImpl();
    }

    @Test
    void testParse() throws Exception {

        // Run the test
        final List<NewDto> result = feedSaxParserImplUnderTest.parse(RSS_FEED_URL);

        // Verify the results
        assertThat(result).isNotEmpty();
    }

    @Test
    void testParse_ThrowsInputSourceException() {
        // Run the test
        assertThatThrownBy(() -> feedSaxParserImplUnderTest.parse("feedUrl")).isInstanceOf(InputSourceException.class);
    }

    @Test
    void testParse_ThrowsXmlReaderException() {
        // Run the test
        assertThatThrownBy(() -> feedSaxParserImplUnderTest.parse(BAD_RSS_FEED_URL)).isInstanceOf(XmlReaderException.class);
    }
}

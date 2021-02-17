package com.example.NewsFeed.schedule;

import com.example.NewsFeed.model.NewEntity;
import com.example.NewsFeed.schedule.impl.CustomParserImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;


/**
 * The Schedule class that runs the Scheduled method to retrieve the RSS Feed every 5 min
 * @author dlarena
 */
@Component
public class ScheduledFeedCall{

	private static final String RSS_FEED_URL = "http://feeds.nos.nl/nosjournaal?format=xml.";
	private static final String CRON = "*/5 * * * * *";

	/**
	 * Method that retrieves the news feed, and reads them using a custom handler
	 *
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException                 the sax exception
	 * @throws IOException                  the io exception
	 */
	@Scheduled(cron=CRON)
	public void retrieveNewsFeed() throws IOException, SAXException, ParserConfigurationException {

		CustomParser customParser = new CustomParserImpl();
		List<NewEntity> parse = customParser.parse(RSS_FEED_URL);
		customParser.storeInDb(parse);

	}


}
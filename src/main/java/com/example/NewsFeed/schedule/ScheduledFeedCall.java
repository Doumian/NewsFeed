package com.example.NewsFeed.schedule;

import com.example.NewsFeed.handler.FeedHandler;
import com.example.NewsFeed.model.FeedItemEntity;
import com.example.NewsFeed.model.RSSFeedEntity;
import com.example.NewsFeed.repository.FeedRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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


/**
 * The Schedule class that runs the Scheduled method to retrieve the RSS Feed every 5 min
 * @author dlarena
 */
@Component
public class ScheduledFeedCall {

	@Autowired
	private FeedRepository feedRepository;

	private ModelMapper modelMapper = new ModelMapper();

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
	public void retrieveNewsFeed() throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser parser = parserFactory.newSAXParser();
		XMLReader reader = parser.getXMLReader();
		FeedHandler feedHandler = new FeedHandler();
		URL url = new URL(RSS_FEED_URL);
		InputSource inputSource = new InputSource(url.openStream());

		reader.setContentHandler(feedHandler);
		reader.parse(inputSource);

		storeInDb(feedHandler);

	}

	/**
	 * Method that stores in DB the articles that have been retrieved previously
	 *
	 * @param feedHandler the feed handler
	 */
	@Transactional
	void storeInDb(FeedHandler feedHandler){
		RSSFeedEntity rssFeedEntity = feedHandler.getRssFeedEntity();
		List<FeedItemEntity> feed = rssFeedEntity.getFeed();
		feed.forEach(feedItemEntity -> {
			Optional<FeedItemEntity> feedItem = feedRepository.findById(feedItemEntity.getId());
			if(feedItem.isPresent() && !feedItem.get().equals(feedItemEntity)) {
				feedItem.get().setDescription(feedItemEntity.getDescription());
				feedItem.get().setTitle(feedItemEntity.getTitle());
				feedItem.get().setPublicationDate(feedItemEntity.getPublicationDate());
				feedItem.get().setImage(feedItemEntity.getImage());
			}
				feedRepository.save(feedItemEntity);
		});

	}
}
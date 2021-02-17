package com.example.NewsFeed.schedule;

import com.example.NewsFeed.handler.FeedHandler;
import com.example.NewsFeed.model.FeedItemEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * The Schedule class that runs the Scheduled method to retrieve the RSS Feed every 5 min
 * @author dlarena
 */
@Component
public class ScheduledFeedCall implements CustomParser{

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
	public void retrieveNewsFeed() throws IOException, SAXException, ParserConfigurationException {

		List<FeedItemEntity> feedItemEntities = parseXMLFromURL(RSS_FEED_URL);

		storeInDb(feedItemEntities);
	}

	/**
	 * Method that stores in DB the articles that have been retrieved previously
	 *
	 * @param feed List of items to store in DB
	 */
	@Transactional
	void storeInDb(List<FeedItemEntity> feed){
		feed.forEach(feedItemEntity -> {
			Optional<FeedItemEntity> existingFeedItem = feedRepository.findById(feedItemEntity.getId());
			if(existingFeedItem.isPresent() && !existingFeedItem.get().equals(feedItemEntity)) {
				existingFeedItem.get().setDescription(feedItemEntity.getDescription());
				existingFeedItem.get().setTitle(feedItemEntity.getTitle());
				existingFeedItem.get().setPublicationDate(feedItemEntity.getPublicationDate());
				existingFeedItem.get().setImage(feedItemEntity.getImage());
			}
				feedRepository.save(feedItemEntity);
		});

	}

	@Override
	public List<FeedItemEntity> parseXMLFromURL(String url) throws ParserConfigurationException, SAXException, IOException {
		List<FeedItemEntity> feedItemEntities = new ArrayList<>();

		FeedHandler feedHandler = new FeedHandler();
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser parser = parserFactory.newSAXParser();
		XMLReader reader = parser.getXMLReader();
		URL newsUrl = new URL(url);
		InputSource inputSource = new InputSource(newsUrl.openStream());
		reader.setContentHandler(feedHandler);
		reader.parse(inputSource);

		if(feedHandler.getRssFeedEntity() != null && feedHandler.getRssFeedEntity().getFeed() != null) {
			feedItemEntities = feedHandler.getRssFeedEntity().getFeed();
		}

		return feedItemEntities;
	}
}
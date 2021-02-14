package com.example.NewsFeed.schedule;

import com.example.NewsFeed.handler.FeedHandler;
import com.example.NewsFeed.model.FeedItemEntity;
import com.example.NewsFeed.model.RSSFeedEntity;
import com.example.NewsFeed.repository.NewRepository;
import lombok.Lombok;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


@Component
public class ScheduledTasks {

	@Autowired
	private NewRepository newRepository;
	private ModelMapper modelMapper = new ModelMapper();

	private static final String URL = "http://feeds.nos.nl/nosjournaal?format=xml.";

	@Scheduled(fixedRate = 50000)
	public void reportCurrentTime() throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser parser = parserFactory.newSAXParser();
		XMLReader reader = parser.getXMLReader();
		FeedHandler feedHandler = new FeedHandler();
		reader.setContentHandler(feedHandler);
		reader.parse(new InputSource(new URL(URL).openStream()));
		storeInDb(feedHandler);

	}

	@Transactional
	void storeInDb(FeedHandler feedHandler){
		RSSFeedEntity rssFeedEntity = feedHandler.getRssFeedEntity();
		List<FeedItemEntity> feed = rssFeedEntity.getFeed();
		feed.forEach(feedItemEntity -> {
			newRepository.save(feedItemEntity);
		});
	}
}
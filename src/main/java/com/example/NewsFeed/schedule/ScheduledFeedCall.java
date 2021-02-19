package com.example.NewsFeed.schedule;

import com.example.NewsFeed.dto.NewDto;
import com.example.NewsFeed.exception.CustomParsingException;
import com.example.NewsFeed.exception.InputSourceException;
import com.example.NewsFeed.exception.XmlReaderException;
import com.example.NewsFeed.model.NewEntity;
import com.example.NewsFeed.repository.NewRepository;
import com.example.NewsFeed.schedule.impl.FeedSaxParserImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.transaction.Transactional;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * The Schedule class that runs the Scheduled method to retrieve the RSS Feed every 5 min
 * @author dlarena
 */
@Component
public class ScheduledFeedCall{

	@Autowired
	NewRepository newRepository;

	private ModelMapper modelMapper = new ModelMapper();

	private static final String RSS_FEED_URL = "http://feeds.nos.nl/nosjournaal?format=xml.";
	private static final String CRON = "0 0/5 * * * ?";

	/**
	 * Method that retrieves the news feed, and reads them using a custom handler
	 *
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException                 the sax exception
	 * @throws IOException                  the io exception
	 */

	@Transactional
	@Scheduled(cron=CRON)
	public void retrieveNewsFeed() throws InputSourceException, CustomParsingException, XmlReaderException {

		FeedParser feedParser = new FeedSaxParserImpl();
		List<NewDto> newDtoList = feedParser.parse(RSS_FEED_URL);
		storeInDb(newDtoList);

	}

	public Integer storeInDb(List<NewDto> newList) {
		List<NewEntity> newEntityList = parseDtoListToEntityList(newList);
		List<NewEntity> validatedList = validateList(newEntityList);
		return newRepository.saveAll(validatedList).size();
	}

	private List<NewEntity> validateList(List<NewEntity> newList) {
		List<NewEntity> validatedList = new ArrayList<>();

		newList.forEach(newEntity -> {
			Optional<NewEntity> existingNew = newRepository.findByGuid(newEntity.getGuid());
			if(!existingNew.isPresent()) {
				validatedList.add(newEntity);
			} else if(existingNew.isPresent() && !existingNew.get().equals(newEntity)){
				newEntity.setId(existingNew.get().getId());
				validatedList.add(newEntity);
			}
		});
		return validatedList;
	}

	private List<NewEntity> parseDtoListToEntityList(List<NewDto> newDtoList){
		return newDtoList.stream()
				.map(source -> modelMapper.map(source, NewEntity.class))
				.collect(Collectors.toList());
	}


}
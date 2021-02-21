package com.example.NewsFeed.scheduled.task;

import com.example.NewsFeed.exception.CustomParsingException;
import com.example.NewsFeed.exception.InputSourceException;
import com.example.NewsFeed.exception.XmlReaderException;
import com.example.NewsFeed.model.dto.NewDto;
import com.example.NewsFeed.model.entity.NewEntity;
import com.example.NewsFeed.repository.NewRepository;
import com.example.NewsFeed.scheduled.task.impl.FeedSaxParserImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * The Schedule class that runs the Scheduled method to retrieve the RSS Feed every 5 min
 *
 * @author dlarena
 */
@Component
public class ScheduledFeedCall{

	/**
	 * The New repository.
	 */
	@Autowired
	NewRepository newRepository;

	private ModelMapper modelMapper = new ModelMapper();

	private static final String RSS_FEED_URL = "http://feeds.nos.nl/nosjournaal?format=xml.";
	private static final String CRON = "0 0/5 * * * ?";

	/**
	 * Scheduled task that executes every 05 min, that retrieves and parse XML data from the RSS Feed Link, and store them into the database.
	 *
	 * @return the amount of items that have been persisted
	 * @throws XmlReaderException     If reading the XML fails
	 * @throws InputSourceException   If preparing the input source fails
	 * @throws CustomParsingException If parsing the XML data fails
	 */
	@Transactional
	@Scheduled(cron=CRON)
	public Integer retrieveNewsFeed() throws InputSourceException, CustomParsingException, XmlReaderException {

		FeedParser feedParser = new FeedSaxParserImpl();
		List<NewDto> newDtoList = feedParser.parse(RSS_FEED_URL);
		return storeInDb(newDtoList);

	}


	/**
	 * Validates and stores in db the dats
	 *
	 * @param newList the new list to persist
	 * @return the amount of records persisted
	 */
	public Integer storeInDb(List<NewDto> newList) {
		List<NewEntity> newEntityList = parseDtoListToEntityList(newList);
		List<NewEntity> validatedList = validateList(newEntityList);
		newRepository.saveAll(validatedList);
		return validatedList.size();
	}

	/**
	 * Validates the List checking if the new item already exists in the db, and if so, if there is anything to update. Non existing items are also inserted.
	 *
	 * @param newList the list to validate
	 * @return the new validated list
	 */

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

	/**
	 * Map an existent DTO list to an Entity one
	 *
	 * @param newDtoList List of DTOs
	 * @return DTO list mapped to Entity List
	 */

	private List<NewEntity> parseDtoListToEntityList(List<NewDto> newDtoList){
		return newDtoList.stream()
				.map(source -> modelMapper.map(source, NewEntity.class))
				.collect(Collectors.toList());
	}


}
package com.example.NewsFeed.scheduled.task;

import com.example.NewsFeed.model.dto.NewDto;
import com.example.NewsFeed.model.entity.NewEntity;
import com.example.NewsFeed.repository.NewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScheduledFeedCallTest {

    private ScheduledFeedCall scheduledFeedCallUnderTest;

    @BeforeEach
    void setUp() {
        scheduledFeedCallUnderTest = new ScheduledFeedCall();
        scheduledFeedCallUnderTest.newRepository = mock(NewRepository.class);
    }

    @Test
    void testRetrieveNewsFeed() throws Exception {

        // Run the test
        final Integer result = scheduledFeedCallUnderTest.retrieveNewsFeed();

        // Verify the results
        assertThat(result).isPositive();
    }



    @Test
    void testRetrieveNewsFeed_NewRepositoryFindByGuidReturnsAbsent() throws Exception {
        // Setup
        when(scheduledFeedCallUnderTest.newRepository.findByGuid(0)).thenReturn(Optional.empty());

        // Configure NewRepository.saveAll(...).
        final NewEntity newEntity = new NewEntity();
        newEntity.setId(0L);
        newEntity.setGuid(0);
        newEntity.setTitle("title");
        newEntity.setDescription("description");
        newEntity.setPublicationDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        newEntity.setImage("content".getBytes());
        final List<NewEntity> newEntities = List.of(newEntity);
        when(scheduledFeedCallUnderTest.newRepository.saveAll(List.of(new NewEntity()))).thenReturn(newEntities);

        // Run the test
        final Integer result = scheduledFeedCallUnderTest.retrieveNewsFeed();

        // Verify the results
        assertThat(result).isPositive();
    }



    @Test
    void testStoreInDb() {
        // Setup
        final NewDto newDto = new NewDto();
        newDto.setId(0L);
        newDto.setGuid(0);
        newDto.setTitle("title");
        newDto.setDescription("description");
        newDto.setPublicationDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        newDto.setImage("content".getBytes());
        final List<NewDto> newList = List.of(newDto);

        // Configure NewRepository.findByGuid(...).
        final NewEntity newEntity1 = new NewEntity();
        newEntity1.setId(0L);
        newEntity1.setGuid(0);
        newEntity1.setTitle("title");
        newEntity1.setDescription("description");
        newEntity1.setPublicationDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        newEntity1.setImage("content".getBytes());
        final Optional<NewEntity> newEntity = Optional.of(newEntity1);
        //when(scheduledFeedCallUnderTest.newRepository.findByGuid(0)).thenReturn(newEntity);

        // Configure NewRepository.saveAll(...).
        final NewEntity newEntity2 = new NewEntity();
        newEntity2.setId(0L);
        newEntity2.setGuid(0);
        newEntity2.setTitle("title");
        newEntity2.setDescription("description");
        newEntity2.setPublicationDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        newEntity2.setImage("content".getBytes());
        final List<NewEntity> newEntities = List.of(newEntity2);
        when(scheduledFeedCallUnderTest.newRepository.saveAll(List.of(new NewEntity()))).thenReturn(newEntities);

        // Run the test
        final Integer result = scheduledFeedCallUnderTest.storeInDb(newList);

        // Verify the results
        assertThat(result).isPositive();
    }

    @Test
    void testStoreInDb_NewRepositoryFindByGuidReturnsAbsent() {
        // Setup
        final NewDto newDto = new NewDto();
        newDto.setId(0L);
        newDto.setGuid(0);
        newDto.setTitle("title");
        newDto.setDescription("description");
        newDto.setPublicationDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        newDto.setImage("content".getBytes());
        final List<NewDto> newList = List.of(newDto);
        when(scheduledFeedCallUnderTest.newRepository.findByGuid(0)).thenReturn(Optional.empty());

        // Configure NewRepository.saveAll(...).
        final NewEntity newEntity = new NewEntity();
        newEntity.setId(0L);
        newEntity.setGuid(43324);
        newEntity.setTitle("title");
        newEntity.setDescription("description");
        newEntity.setPublicationDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        newEntity.setImage("content".getBytes());
        final List<NewEntity> newEntities = List.of(newEntity);
        when(scheduledFeedCallUnderTest.newRepository.saveAll(List.of(new NewEntity()))).thenReturn(newEntities);

        // Run the test
        final Integer result = scheduledFeedCallUnderTest.storeInDb(newList);

        // Verify the results
        assertThat(result).isEqualTo(1);
    }

    @Test
    void testStoreInDb_NewRepositorySaveAllReturnsNoItems() {
        // Setup
        final NewDto newDto = new NewDto();
        newDto.setId(0L);
        newDto.setGuid(0);
        newDto.setTitle("title");
        newDto.setDescription("description");
        newDto.setPublicationDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        newDto.setImage("content".getBytes());
        final List<NewDto> newList = List.of(newDto);

        // Configure NewRepository.findByGuid(...).
        final NewEntity newEntity1 = new NewEntity();
        newEntity1.setId(0L);
        newEntity1.setGuid(0);
        newEntity1.setTitle("title");
        newEntity1.setDescription("description");
        newEntity1.setPublicationDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        newEntity1.setImage("content".getBytes());
        final Optional<NewEntity> newEntity = Optional.of(newEntity1);
        when(scheduledFeedCallUnderTest.newRepository.findByGuid(0)).thenReturn(newEntity);

        when(scheduledFeedCallUnderTest.newRepository.saveAll(List.of(new NewEntity()))).thenReturn(Collections.emptyList());

        // Run the test
        final Integer result = scheduledFeedCallUnderTest.storeInDb(newList);

        // Verify the results
        assertThat(result).isZero();
    }
}

package com.example.NewsFeed.repository;

import com.example.NewsFeed.model.FeedItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Feed repository.
 */
@Repository
public interface FeedRepository extends JpaRepository<FeedItemEntity, Long> {

}
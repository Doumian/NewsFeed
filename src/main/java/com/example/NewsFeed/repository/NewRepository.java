package com.example.NewsFeed.repository;

import com.example.NewsFeed.model.FeedItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewRepository extends JpaRepository<FeedItemEntity, Long> {

}
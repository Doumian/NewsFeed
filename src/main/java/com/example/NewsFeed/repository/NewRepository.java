package com.example.NewsFeed.repository;

import com.example.NewsFeed.model.NewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Feed repository.
 */
@Repository
public interface NewRepository extends JpaRepository<NewEntity, Long> {

    Optional<NewEntity> findByGuid(Integer guid);

}
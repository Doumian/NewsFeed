package com.example.NewsFeed.repository;

import com.example.NewsFeed.model.entity.NewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Feed repository.
 */
@Repository
public interface NewRepository extends JpaRepository<NewEntity, Long> {

    /**
     * Find new by guid.
     *
     * @param guid the guid
     * @return the optional
     */
    Optional<NewEntity> findByGuid(Integer guid);

}
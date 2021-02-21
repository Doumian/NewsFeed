package com.example.NewsFeed.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.example.NewsFeed.model.entity.NewEntity;
import com.example.NewsFeed.repository.NewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * The type New queries.
 */
@Component
public class NewQueries implements GraphQLQueryResolver {

    @Autowired
    private NewRepository newRepository;

    /**
     * Select new by id new entity.
     *
     * @param ID the id
     * @return the new entity
     */
    public NewEntity selectNewById(Long ID) {
        return this.newRepository.findById(ID).get();
    }

    /**
     * Select new by guid new entity.
     *
     * @param guid the guid
     * @return the new entity
     */
    public NewEntity selectNewByGuid(Integer guid) {
        Optional<NewEntity> newEntity = this.newRepository.findByGuid(guid);
        if(newEntity.isPresent()) return newEntity.get();
        return null;
    }

    /**
     * Select all news list.
     *
     * @return the list
     */
    public List<NewEntity> selectAllNews() {
        return this.newRepository.findAll();
    }
}
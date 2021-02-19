package com.example.NewsFeed.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.example.NewsFeed.model.NewEntity;
import com.example.NewsFeed.repository.NewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Query implements GraphQLQueryResolver {

    @Autowired
    private NewRepository newRepository;

    public NewEntity selectNew(Long ID) {
        return this.newRepository.findById(ID).get();
    }
}
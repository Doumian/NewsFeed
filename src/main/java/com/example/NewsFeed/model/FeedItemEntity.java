package com.example.NewsFeed.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * The type Feed item entity.
 */
@Entity
@Data
@Table(name="FeedItem")
public class FeedItemEntity {

    @Id
    private Integer id;

    private String title;
    
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private LocalDateTime publicationDate;

    @Lob
    private byte[] image;

}





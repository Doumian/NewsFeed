package com.example.NewsFeed.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="FeedItem")
public class FeedItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private LocalDateTime publicationDate;

    @Lob
    private byte[] image;

}





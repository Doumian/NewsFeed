package com.example.NewsFeed.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * The type Feed item entity.
 */
@Entity
@Data
@Table(name="NewItem")
public class NewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer guid;

    private String title;
    
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private LocalDateTime publicationDate;

    @Lob
    private byte[] image;

}





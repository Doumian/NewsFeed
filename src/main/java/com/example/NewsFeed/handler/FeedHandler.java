package com.example.NewsFeed.handler;

import com.example.NewsFeed.model.FeedItemEntity;
import com.example.NewsFeed.model.RSSFeedEntity;
import lombok.SneakyThrows;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FeedHandler extends DefaultHandler {

    private StringBuilder chars = new StringBuilder();

    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String PUB_DATE = "pubDate";
    private static final String IMAGE = "enclosure";
    private static final String URL = "url";
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ssZ";

    private RSSFeedEntity rssFeedEntity;
    private String imageUrl;
    private Boolean limitReached = false;

    @Override
    public void characters(char[] ch, int start, int length) {
        chars.append(ch, start, length);
    }

    @Override
    public void startDocument() {
        rssFeedEntity = new RSSFeedEntity();
    }

    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) {
            chars.setLength(0);
            if(qName.equals(ITEM)){
                if(Boolean.FALSE.equals(limitReached)) {
                    if (rssFeedEntity.getFeed() == null) rssFeedEntity.setFeed(new ArrayList<>());
                    rssFeedEntity.getFeed().add(new FeedItemEntity());
                    if (rssFeedEntity.getFeed().size() == 10) limitReached = true;
                }
            } else if(qName.equals(IMAGE)){
                imageUrl = attr.getValue(URL);
            }

    }

    @SneakyThrows
    @Override
    public void endElement(String uri, String localName, String qName) {
        if(rssFeedEntity.getFeed() != null) {
            switch (qName) {
                case TITLE:
                    latestArticle().setTitle(chars.toString());
                    break;
                case DESCRIPTION:
                    latestArticle().setDescription(chars.toString());
                    break;
                case PUB_DATE:
                    SimpleDateFormat curFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
                    Date publicationDate = curFormatter.parse(chars.toString());
                    LocalDateTime ldtPublicationDate = LocalDateTime.ofInstant(publicationDate.toInstant(),
                            ZoneId.systemDefault());
                    latestArticle().setPublicationDate(ldtPublicationDate);
                    break;
                case IMAGE:
                    latestArticle().setImage(urlToByteArray(imageUrl));
                    break;
            }
        }
    }

    private FeedItemEntity latestArticle() {
        List<FeedItemEntity> articleList = rssFeedEntity.getFeed();
        int latestArticleIndex = articleList.size() - 1;
        return articleList.get(latestArticleIndex);
    }

    public RSSFeedEntity getRssFeedEntity() {
        return rssFeedEntity;
    }

    private byte[] urlToByteArray(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        BufferedInputStream bis = new BufferedInputStream(url.openConnection().getInputStream());
        return bis.readAllBytes();
    }

}

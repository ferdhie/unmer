package com.ferdianto.android.unmernews.model;

import java.util.Date;

/**
 * Created by ferdhie on 25-11-2015.
 */
public class News {
    private String title;
    private String shortNews;
    private Date lastUpdate;
    private String link;
    private String newsImage;

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortNews() {
        return shortNews;
    }

    public void setShortNews(String shortNews) {
        this.shortNews = shortNews;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof News)) return false;

        News news = (News) o;

        if (link != null ? !link.equals(news.link) : news.link != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return link != null ? link.hashCode() : 0;
    }
}

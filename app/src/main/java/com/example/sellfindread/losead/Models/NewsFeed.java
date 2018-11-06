package com.example.sellfindread.losead.Models;

public class NewsFeed {

    public String title;
    public String pubDate;
    public String description;
    public Long upvotes;
    public Long downvotes;
    public String link;
    public String itemKey;
    public String postedBy;
    public String userid;

    public NewsFeed(String title, String pubDate, String description, Long upvotes, Long downvotes, String link, String itemKey, String postedBy, String userid) {
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
        this.upvotes=upvotes;
        this.downvotes=downvotes;
        this.link=link;
        this.itemKey=itemKey;
        this.postedBy=postedBy;
        this.userid=userid;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public Long getDownvotes() {
        return downvotes;
    }

    public String getItemKey() {
        return itemKey;
    }

    public String getLink() {
        return link;
    }

    public Long getUpvotes() {
        return upvotes;
    }

    public String getTitle() {
        return title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getUserid() {
        return userid;
    }
}

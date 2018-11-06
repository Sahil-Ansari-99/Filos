package com.example.sellfindread.losead.Models;

public class ReportBug {

    String title;
    String pubDate;
    String userId;

    public ReportBug(String title, String pubDate, String userId) {
        this.title = title;
        this.pubDate=pubDate;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}

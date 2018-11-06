package com.example.sellfindread.losead.Models;

public class LostAndFound {

    public String seller;
    public String title;
    public String contact;
    public String price;
    public String desc;
    public String pubDate;
    public String image;
    public String userid,key;

    public LostAndFound(String contact,String desc, String image, String key,  String price ,String seller, String pubDate,String title,  String userid) {
        this.seller = seller;
        this.title = title;
        this.contact = contact;
        this.price = price;
        this.desc = desc;
        this.pubDate = pubDate;
        this.image = image;
        this.userid = userid;
        this.key = key;
    }

    public LostAndFound() {
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

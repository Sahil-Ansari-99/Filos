package com.example.sellfindread.losead.Models;

public class BuyAndSellCommentModel {
    public String comment;
    public String userName;

    public BuyAndSellCommentModel() {
    }

    public BuyAndSellCommentModel(String comment, String userName) {

        this.comment = comment;
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}

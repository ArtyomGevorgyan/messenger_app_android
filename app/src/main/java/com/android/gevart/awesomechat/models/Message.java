package com.android.gevart.awesomechat.models;

public class Message {

    private String text;
    private String senderUserName;
    private String senderUserId;
    private String recipientUserId;
    private String imageUrl;

    public Message() {
    }

    public Message(String text, String senderUserName, String senderUserId, String recipientUserId, String imageUrl) {
        this.text = text;
        this.senderUserName = senderUserName;
        this.senderUserId = senderUserId;
        this.recipientUserId = recipientUserId;
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSenderUserName() {
        return senderUserName;
    }

    public void setSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getRecipientUserId() {
        return recipientUserId;
    }

    public void setRecipientUserId(String recipientUserId) {
        this.recipientUserId = recipientUserId;
    }
}

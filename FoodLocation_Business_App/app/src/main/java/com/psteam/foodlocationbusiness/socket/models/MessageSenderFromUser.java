package com.psteam.foodlocationbusiness.socket.models;

public class MessageSenderFromUser {
    public MessageSenderFromUser(String sender, String receiver, String title, BodySenderFromUser body) {
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.body = body;
    }

    private String sender;
    private String receiver;
    private String title;
    private BodySenderFromUser body;
}

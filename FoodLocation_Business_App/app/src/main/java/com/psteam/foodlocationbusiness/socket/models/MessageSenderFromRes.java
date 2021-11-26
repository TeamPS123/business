package com.psteam.foodlocationbusiness.socket.models;

public class MessageSenderFromRes {
    public MessageSenderFromRes(String sender, String receiver, String title, BodySenderFromRes body) {
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.body = body;
    }

    private String sender;
    private String receiver;
    private String title;
    private BodySenderFromRes body;
}

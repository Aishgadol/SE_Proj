package client;

import entities.Message;

public class BackgroundImageEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public BackgroundImageEvent(Message message) {
        this.message = message;
    }
}

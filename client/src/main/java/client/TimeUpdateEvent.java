package client;

import entities.Message;

public class TimeUpdateEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public TimeUpdateEvent(Message message) {
        this.message = message;
    }
}

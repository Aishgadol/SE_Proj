package client;

import entities.Message;

public class ErrorEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public ErrorEvent(Message message) {
        this.message = message;
    }
}

package client;

import entities.Message;

public class CustomerAddedEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public CustomerAddedEvent(Message message) {
        this.message = message;
    }
}

package client;

import entities.Message;

public class MovieAlreadyExistsEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public MovieAlreadyExistsEvent(Message message) {
        this.message = message;
    }
}

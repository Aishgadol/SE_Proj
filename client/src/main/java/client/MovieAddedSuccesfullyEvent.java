package client;

import entities.Message;

public class MovieAddedSuccesfullyEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public MovieAddedSuccesfullyEvent(Message message) {
        this.message = message;
    }
}

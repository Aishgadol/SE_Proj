package client;

import entities.Message;

public class MovieRemovedSuccesfullyEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public MovieRemovedSuccesfullyEvent(Message message) {
        this.message = message;
    }
}

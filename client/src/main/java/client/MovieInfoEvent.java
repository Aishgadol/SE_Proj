package client;

import entities.Message;

public class MovieInfoEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public MovieInfoEvent(Message message) {
        this.message = message;
    }
}

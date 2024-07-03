package client;

import entities.Message;

public class MovieInfoListEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public MovieInfoListEvent(Message message) {
        this.message = message;
    }
}

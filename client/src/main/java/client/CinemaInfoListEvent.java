package client;

import entities.Message;

public class CinemaInfoListEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public CinemaInfoListEvent(Message message) {
        this.message = message;
    }
}

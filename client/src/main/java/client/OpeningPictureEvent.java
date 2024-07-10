package client;

import entities.Message;

public class OpeningPictureEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public OpeningPictureEvent(Message message) {
        this.message = message;
    }
}

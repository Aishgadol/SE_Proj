package client;

import entities.Message;

public class OpeningGifEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public OpeningGifEvent(Message message) {
        this.message = message;
    }
}

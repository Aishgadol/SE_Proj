package client;

import entities.Message;

public class ImageCatchEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public ImageCatchEvent(Message message) {
        this.message = message;
    }
}

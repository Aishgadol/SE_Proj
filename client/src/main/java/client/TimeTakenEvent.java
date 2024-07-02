package client;

import entities.Message;

public class TimeTakenEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public TimeTakenEvent(Message message) {
        this.message = message;
    }
}

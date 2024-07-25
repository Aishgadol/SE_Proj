package client;

import entities.Message;

public class TicketInfoListEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public TicketInfoListEvent(Message message) {
        this.message = message;
    }
}

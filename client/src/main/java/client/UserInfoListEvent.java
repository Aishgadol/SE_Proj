package client;

import entities.Message;

public class UserInfoListEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public UserInfoListEvent(Message message) {
        this.message = message;
    }
}

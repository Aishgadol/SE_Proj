package client;

import entities.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class TesterController {
    private int msgId;

    @FXML
    private TextArea board;

    @FXML
    private Button sendBtn;

    @FXML
    private TextField textField;

    @FXML
    void sendToDB(ActionEvent event) {
        Message message = new Message(msgId++, textField.getText());
        textField.clear();
    }

    @FXML
    void initialize(){
        EventBus.getDefault().register(this);
		textField.clear();
        msgId=0;
    }
}

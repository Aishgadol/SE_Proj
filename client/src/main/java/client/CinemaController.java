package client;

import entities.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;


public class CinemaController {
    private int msgId;

    @FXML
    private VBox cinema;


    @Subscribe
    public void doNothing(Message msg){
        System.out.println("Message got was: "+msg.getMessage());
    }
    /*
    @FXML
    void sendToDb(ActionEvent event) {
        try {
            Message message = new Message(msgId++, textField.getText());
            textField.clear();
            SimpleClient.getClient().sendToServer(message);

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Subscribe
	public void setDataFromServerTF(Message msg) {
        board.setText(msg.getMessage());
	}

*/
    @FXML
    void initialize(){
        EventBus.getDefault().register(this);
        msgId=0;
        try {
			Message message = new Message(msgId, "add client");
			SimpleClient.getClient().sendToServer(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

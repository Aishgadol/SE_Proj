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
public class OpeningController{
    private int msgId;


    @FXML
    private VBox cinema;

    @FXML
    private Button updateButton;

    @Subscribe
    public void eventBusFiller(Message message){
        return;
    }
    @FXML
    void initialize(){

    }
}

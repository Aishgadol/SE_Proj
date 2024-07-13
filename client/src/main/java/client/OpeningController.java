package client;

import entities.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import java.io.ByteArrayInputStream;
import java.io.IOException;
public class OpeningController{
    private int msgId;


    @FXML
    private VBox cinema;

    @FXML
    private ImageView myImageView;
    @FXML
    private AnchorPane myAnchor;
    @FXML
    private Label welcomeLabel;

    @Subscribe
    public void catchOpeningGifAndDisplay(OpeningGifEvent event){
        byte[] data=event.getMessage().getImageData();
        if(data!=null){
            try{
                ByteArrayInputStream bis=new ByteArrayInputStream(data);
                Image image=new Image(bis);
                myImageView.setImage(image);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        EventBus.getDefault().unregister(this);
        return;

    }

    @FXML
    void initialize(){
        EventBus.getDefault().register(this);
        Message message=new Message(1,"getOpeningGif");
        try {
            SimpleClient.getClient().sendToServer(message);
        }catch(Exception e){
            e.printStackTrace();
        }
        welcomeLabel.setAlignment(Pos.CENTER);


    }
}

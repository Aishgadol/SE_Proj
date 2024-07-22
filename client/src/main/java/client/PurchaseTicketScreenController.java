package client;

import entities.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayInputStream;

public class PurchaseTicketScreenController {
    private int msgId;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String currUserID;
    private HBox imageContainer;




    @FXML
    private Button backButton;
    @FXML
    private HBox selectedMovieHBox;
    @FXML
    private ImageView backgroundImageView;

    public void setCurrUserID(String name){
        this.currUserID =name;
    }
    public void setImageContainer(HBox imageContainer){
        this.imageContainer=imageContainer;

    }
    @FXML
    private void backButtonPressed(ActionEvent event){
        try {
            EventBus.getDefault().unregister(this);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/customerMainScreen.fxml"));
            Parent root = loader.load();
            CustomerMainScreenController controller = loader.getController();
            controller.setCurrUserID(currUserID);
            controller.setInfoLabel(currUserID);
            stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
            scene=new Scene(root,1280,800);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setOnCloseRequest(some_event->handleClose(currUserID));
            stage.setTitle("Customer Main Screen");
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void setBackground(byte[] data){
        if(data!=null){
            try{
                Platform.runLater(()->{
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                Image image=new Image(bis);
                this.backgroundImageView.setImage(image);
                this.backgroundImageView.setOpacity(0.2);
                this.backgroundImageView.setFitWidth(1280.0);
                this.backgroundImageView.setFitHeight(800.0);
                this.backgroundImageView.setPreserveRatio(false);

                });
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }













    @Subscribe
    public void catchBackgroundImage(BackgroundImageEvent event){
        byte[] data=event.getMessage().getImageData();
        setBackground(data);
    }












    private void askDB(String title){
        try {
            Message message = new Message(msgId++, title);
            SimpleClient.getClient().sendToServer(message);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void handleClose(String name){
        askDB("disconnectCustomer "+name);
        stage.close();
    }
    @FXML
    private void initialize(){
        EventBus.getDefault().register(this);
        msgId=0;
        askDB("getBackgroundImage");

    }

}
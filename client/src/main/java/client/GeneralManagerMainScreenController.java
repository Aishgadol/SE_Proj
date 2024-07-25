package client;

import entities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeneralManagerMainScreenController {
    private int msgId;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private UserInfo currUserInfo;
    private List<UserInfo> userInfoList=new ArrayList<>();
    private List<MovieInfo> movieInfoList=new ArrayList<>();
    private List<TicketInfo> ticketInfoList=new ArrayList<>();
    private List<CinemaInfo> cinemaInfoList=new ArrayList<>();
    @FXML
    private Button disconnectButton;
    @FXML
    private ImageView backgroundImageView;
    @FXML
    private Label connectedAsLabel;

    //subscribe methods
    @Subscribe
    public void catchMovieRemoved(MovieRemovedSuccesfullyEvent event){
        askDB("getTitles");
    }
    @Subscribe
    public void catchMovieAdded(MovieAddedSuccesfullyEvent event){
        askDB("getTitles");
    }
    @Subscribe
    public void catchTicketInfoList(TicketInfoListEvent event){
        this.ticketInfoList=event.getMessage().getTicketInfoList();
    }
    @Subscribe
    public void catchCinemaInfoList(CinemaInfoListEvent event){
        this.cinemaInfoList=event.getMessage().getCinemaInfoList();
    }
    @Subscribe
    public void catchMovieInfoList(MovieInfoListEvent event){
        this.movieInfoList=event.getMessage().getMovieInfoList();
    }
    @Subscribe
    public void catchBackgroundImage(BackgroundImageEvent event){
        byte[] data=event.getMessage().getImageData();
        setBackground(data);
    }
    @Subscribe
    public void catchUserInfoList(UserInfoListEvent event){
        this.userInfoList=event.getMessage().getUserInfoList();
    }
    @Subscribe
    public void catchNewUserInfo(CustomerAddedEvent event){
        if (!event.getMessage().getMessage().contains("wasnt")){
            UserInfo u=event.getMessage().getUserInfo();
            this.userInfoList.add(u);
            //refresh whatever is needed
        }
    }



    //getters
     private UserInfo getUserInfoByName(String name){
        for(UserInfo u: userInfoList){
            if (u.getName().toLowerCase().equals(name)){
                return u;
            }
        }
        return null;
    }



    //setters
    public void setCurrUserInfo(UserInfo u){
        this.currUserInfo=u;
        this.connectedAsLabel.setText(currUserInfo.getName());
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
















    //FXML stuff

    @FXML
    void updateTimesButtonPressed(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/updateTimeScreen.fxml"));
            Parent root = loader.load();
            UpdateController c=loader.getController();
            c.setCurrUserInfo(currUserInfo);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1280, 800);
            stage.setScene(scene);
            stage.setOnCloseRequest(some_event->handleWorkerClose(currUserInfo.getName()));
            stage.setResizable(false);
            stage.setTitle("Update Screen");
            stage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @FXML
    private void disconnectButtonPressed(ActionEvent event) {
        askDB("disconnectWorker "+currUserInfo.getName());
        try {
            EventBus.getDefault().unregister(this);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/startingScreen.fxml"));
            root = loader.load();
            StartingScreenController controller = loader.getController();
            stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
            scene=new Scene(root,1280,800);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Welcome Screen");
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }






    //auxiliry functions
    private void askDB(String title){
        try {
            Message message = new Message(msgId++, title);
            SimpleClient.getClient().sendToServer(message);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void handleWorkerClose(String name){
        askDB("disconnectWorker "+name);
        stage.close();
    }














    //initialize
    @FXML
    void initialize(){
        EventBus.getDefault().register(this);
        msgId = 0;
        askDB("getBackgroundImage");
        askDB("getTitles");
        askDB("getUsers");
        askDB("getCinemas");
    }

}

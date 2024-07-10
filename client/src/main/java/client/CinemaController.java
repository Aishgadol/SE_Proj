package client;

import entities.MovieInfo;
import entities.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class CinemaController {
    private int msgId;
    private MovieInfo currMovieInfo;
    private SimpleChatClient chatclient;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private List<MovieInfo> currMovieInfos=new ArrayList<>();

    @FXML
    private Button updateButton;
    @FXML
    private ImageView automobiles_img;
    @FXML
    private ImageView background;
    @FXML
    private VBox mainScreen;
    @FXML
    private AnchorPane homeScreen;
    @FXML
    private ImageView house_of_cards_img;
    @FXML
    private ImageView margol_img;
    @FXML
    private ImageView pulp_fiction_img;
    @FXML
    private ImageView scary_movie_img;
    @FXML
    private ImageView the_boys_img;
    @FXML
    private HBox imageHBox;


    @FXML
    void clickedMargol(MouseEvent event) {askDB("getMovieInfo Margol");}

    @FXML
    void clickedHouse(MouseEvent event) {askDB("getMovieInfo House of Cards");}

    @FXML
    void clickedPulpFiction(MouseEvent event) {askDB("getMovieInfo Pulp Fiction");}

    @FXML
    void clickedScaryMovie(MouseEvent event) {askDB("getMovieInfo Scary Movie 5");}

    @FXML
    void clickedTheBoys(MouseEvent event) {
        askDB("getMovieInfo The Boys");
    }

    @FXML
    void clickedAutomobiles(MouseEvent event){
        askDB("getMovieInfo Automobiles");
    }


    private void popMessageWithMovieInfo(){
        Platform.runLater(()->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information on "+this.currMovieInfo.getName());
            alert.setHeaderText("This is information about the movie: "+this.currMovieInfo.getName()+"\n " +
                    "Released at: "+this.currMovieInfo.getReleaseDate());
            StringBuilder sb=new StringBuilder();
            for(String s : this.currMovieInfo.getDisplayTimes()){
                sb.append(s);
                sb.append("\n");
            }
            alert.setContentText("Showtimes: \n"+sb.toString());
            alert.show();
        });
    }
    @Subscribe
    public void catchMovieInfo(MovieInfoEvent event){
        this.currMovieInfo=event.getMessage().getMovieInfo();
        popMessageWithMovieInfo();
    }


    void askDB(String title){
        try{
            Message message=new Message(msgId++,title);
            SimpleClient.getClient().sendToServer(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /* might trash this later, might now
    @Subscribe
    public void movieInfoAndImage(ImageCatchEvent event){
        byte[] data=event.getMessage().getImageData();
        ImageView imageView = new ImageView();

        if(data!=null){
            try{
                ByteArrayInputStream bis=new ByteArrayInputStream(data);
                Image image=new Image(bis);
                imageView.setImage(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(250);
                imageView.setPreserveRatio(true);
                Label nameLabel= new Label(this.currMovieInfo.getName());
                nameLabel.setAlignment(Pos.CENTER);

                VBox imageContainer = new VBox();
                imageContainer.getChildren().addAll(imageView,nameLabel);
                imageContainer.setAlignment(Pos.CENTER);

                //add tooltip
                Tooltip tooltip = new Tooltip(this.currMovieInfo.getName());
                Tooltip.install(imageView,tooltip);

                imageHBox.getChildren().add(imageContainer);
                imageHBox.setAlignment(Pos.CENTER);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }*/
    @Subscribe
    public void catchMovieInfoList(MovieInfoListEvent event){
        this.currMovieInfos=event.getMessage().getList();
    }

    @FXML
    @Subscribe
    public void catchBackgroundImage(BackgroundImageEvent event){
        byte[] data=event.getMessage().getImageData();
        if(data!=null){
            try{
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                Image image=new Image(bis);
                ImageView imageView=new ImageView(image);
                imageView.setOpacity(0.25);
                homeScreen.getChildren().add(imageView);
                AnchorPane.setTopAnchor(imageView,0.0);
                AnchorPane.setLeftAnchor(imageView,0.0);
                imageView.setFitWidth(homeScreen.getWidth());
                imageView.setFitHeight(homeScreen.getHeight());

            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }
    
    @FXML
    private void displayAllMovies() {
        for (MovieInfo movieInfo : this.currMovieInfos) {
            byte[] data = movieInfo.getImageData();
            ImageView imageView = new ImageView();
            if (data != null) {
                try {
                    ByteArrayInputStream bis = new ByteArrayInputStream(data);
                    Image image = new Image(bis);
                    imageView.setImage(image);
                    imageView.setFitWidth(200);
                    imageView.setFitHeight(250);
                    imageView.setPreserveRatio(true);
                    Label nameLabel = new Label(movieInfo.getName());
                    nameLabel.setAlignment(Pos.CENTER);

                    VBox imageContainer = new VBox();
                    imageContainer.getChildren().addAll(imageView, nameLabel);
                    imageContainer.setAlignment(Pos.CENTER);

                    //add tooltip
                    Tooltip tooltip = new Tooltip(movieInfo.getName());
                    Tooltip.install(imageView, tooltip);

                    imageHBox.getChildren().add(imageContainer);
                    imageHBox.setAlignment(Pos.CENTER);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void changeToUpdateScreen(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        try {
            askDB("remove client");
            Parent root = FXMLLoader.load(getClass().getResource("/updateTimeScreen.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1280, 800);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Update Screen");
            stage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    void initialize(){
        EventBus.getDefault().register(this);
        msgId=0;
        askDB("getBackgroundImage");
        askDB("getTitles");
        displayAllMovies();
    }
}

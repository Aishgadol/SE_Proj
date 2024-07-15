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
    private ImageView backgroundImageView;
    @FXML
    private HBox imageHBox;

    private void popMessageWithMovieInfo(String title){
        for(MovieInfo m: this.currMovieInfos){
            if(m.getName().equals(title)){
                this.currMovieInfo=m;
            }
        }
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

    void askDB(String title){
        try{
            Message message=new Message(msgId++,title);
            SimpleClient.getClient().sendToServer(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void clearDisplay(){
        try{
            Platform.runLater(()->{
                this.imageHBox.getChildren().clear();

            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Subscribe
    public void catchMovieInfo(MovieInfoEvent event){
        this.currMovieInfo=event.getMessage().getMovieInfo();
    }
    @Subscribe
    public void catchMovieInfoList(MovieInfoListEvent event){
        this.currMovieInfos=event.getMessage().getList();
        clearDisplay();
        displayAllMovies();
    }
    @Subscribe
    public void catchBackgroundImage(BackgroundImageEvent event){
        byte[] data=event.getMessage().getImageData();
        setBackground(data);
    }
    @Subscribe
    public void catchMovieAdded(MovieAddedSuccesfullyEvent event){
        askDB("getTitles");
    }
    @Subscribe
    public void catchMovieRemovedSuccesfully(MovieRemovedSuccesfullyEvent event){
        askDB("getTitles");
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
                this.backgroundImageView.setFitWidth(1280);
                this.backgroundImageView.setFitHeight(800);
                this.backgroundImageView.setPreserveRatio(false);

                });
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void displayAllMovies() {
        for (MovieInfo movieInfo : this.currMovieInfos) {
            byte[] data = movieInfo.getImageData();
            if (data != null) {
                try {
                    Platform.runLater(()->{
                    ByteArrayInputStream bis = new ByteArrayInputStream(data);
                    Image image = new Image(bis);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(200);
                    imageView.setFitHeight(250);
                    imageView.setPreserveRatio(false);
                    imageView.setOnMouseClicked(this::handleImageClick);
                    imageView.setId(movieInfo.getName());

                    Label nameLabel = new Label(movieInfo.getName());
                    nameLabel.setStyle("-fx-font-family: Constantia; -fx-font-size: 19px; -fx-font-weight: bold;"); // Set font size and make text bold
                    nameLabel.setAlignment(Pos.CENTER);

                    VBox imageContainer = new VBox();
                    imageContainer.getChildren().addAll(imageView, nameLabel);
                    imageContainer.setAlignment(Pos.CENTER);
                    imageContainer.setMaxWidth(220);
                    imageContainer.setMaxHeight(260);
                    imageContainer.setStyle("-fx-border-color: black; -fx-border-width: 3;-fx-padding: 0");



                    //add tooltip
                    Tooltip tooltip = new Tooltip(movieInfo.getName());
                    Tooltip.install(imageView, tooltip);

                    imageHBox.getChildren().add(imageContainer);
                    imageHBox.setAlignment(Pos.CENTER);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleImageClick(MouseEvent event){
        ImageView clickedImageView=(ImageView) event.getSource();
        String title=clickedImageView.getId();
        popMessageWithMovieInfo(title);
    }

    @FXML
    void changeToUpdateScreen(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        try {
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
    }
}
package client;

import entities.Message;
import entities.MovieInfo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayInputStream;
import java.util.List;

public class startingScreenController {
    private MovieInfo currMovieInfo;
    private int msgId;
    private List<MovieInfo> currMovieInfos;
    @FXML
    private ImageView backgroundImageView;

    @FXML
    private TextField idTextField;

    @FXML
    private HBox imageHBox;

    @FXML
    private ScrollPane imageScrollPane;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    void loginButtonPressed(ActionEvent event) {

    }
    @FXML
    private void popMessageWithMovieInfo(String title){
        for(MovieInfo m: this.currMovieInfos){
            if(m.getName().equals(title)){
                currMovieInfo=m;
            }
        }
        if(currMovieInfo==null){
            Platform.runLater(()->{
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Movie not found in database");
                alert.setHeaderText("Error looking for movie information to show");
                alert.show();
            });
            return;
        }
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information on "+currMovieInfo.getName());
            alert.setHeaderText("This is information about the movie: "+currMovieInfo.getName()+"\n ");
            alert.setContentText(currMovieInfo.toString());
            alert.setResizable(true);
            alert.setWidth(400.0);
            alert.setHeight(600.0);
            alert.show();
        });
    }
    @FXML
    private void handleImageClick(MouseEvent event){
        ImageView clickedImageView=(ImageView) event.getSource();
        String title=clickedImageView.getId();
        popMessageWithMovieInfo(title);
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
    private void clearDisplay(){
        try{
            Platform.runLater(()->{
                this.imageHBox.getChildren().clear();

            });
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


    void askDB(String title){
        try {
            Message message = new Message(msgId++, title);
            SimpleClient.getClient().sendToServer(message);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void initialize(){
        EventBus.getDefault().register(this);
        msgId=0;
        askDB("getBackgroundImage");
        askDB("getTitles");
        this.passwordField.setVisible(false);
        //listener to limit inputID length and allowed characters
        this.idTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 9) {
                this.idTextField.setText(oldValue); // revert to old value if exceeds max length
            }
            if (!newValue.matches("[0-9]*")) {
                this.idTextField.setText(oldValue);
            }
        });
        //listener to limit password length and allowed characters
        this.passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 25) {
                this.passwordField.setText(oldValue); // revert to old value if exceeds max length
            }
            if (!newValue.matches("[a-zA-Z0-9., -!@#$%^&*()/]*")) {
                this.passwordField.setText(oldValue);
            }
        });
    }
}

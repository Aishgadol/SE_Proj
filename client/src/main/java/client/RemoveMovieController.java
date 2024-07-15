package client;

import entities.Message;
import entities.MovieInfo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RemoveMovieController {
    private MovieInfo movieInfoToDelete;
    private int msgId;
    private Stage stage;
    private Scene scene;
    private List<MovieInfo> movieInfos=new ArrayList<>();
    private MovieInfo chosenMovie;


    @FXML
    private ImageView backgroundImageView;
    @FXML
    private Button cancelSelectionButton;
    @FXML
    private Button deleteSelectedMovieButton;
    @FXML
    private ScrollPane imageScrollPane;
    @FXML
    private HBox imageHBox;
    @FXML
    private Label selectMovieLabel;

    void askDB(String title){
        try {
            Message message = new Message(msgId++, title);
            if(title.equals("removeMovie")){
                message.setMovieInfo(this.movieInfoToDelete);
            }
            SimpleClient.getClient().sendToServer(message);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setMovieInfos(){
        askDB("getTitles");
    }

    @Subscribe
    public void catchTitles(MovieInfoListEvent event){
        this.movieInfos=event.getMessage().getList();
        displayMovies();
    }

    @FXML
    private void goBackButtonPressed(ActionEvent event){
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

    @Subscribe
    public void catchMovieRemovedSuccesfully(MovieRemovedSuccesfullyEvent event){
        showConfirmationPopup();
    }

    @FXML
    private void showConfirmationPopup(){
        Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Movie Removed from database");
                alert.setHeaderText("A movie has been removed from the database");
                alert.setContentText("It is done.");
                alert.show();
                cancelSelection(new ActionEvent());
            });
    }

    @FXML
    private void handleImageClick(MouseEvent event){
        VBox clickedImageImageView=(VBox) event.getSource();
        String title=clickedImageImageView.getId();
        MovieInfo selectedMovieInfo=null;
        for(MovieInfo m: movieInfos){
            if(m.getName().equals(title)){
                selectedMovieInfo=m;
            }
        }
        cancelSelectionButton.setVisible(true);
        deleteSelectedMovieButton.setVisible(true);
        imageHBox.getChildren().clear();
        imageHBox.getChildren().add(clickedImageImageView);
        this.selectMovieLabel.setText("The movie you've selected: ");
        this.movieInfoToDelete=selectedMovieInfo;
    }

    @FXML
    private void cancelSelection(ActionEvent event){
        deleteSelectedMovieButton.setVisible(false);
        cancelSelectionButton.setVisible(false);
        this.imageHBox.getChildren().clear();
        this.selectMovieLabel.setText("Select a movie to delete");
        setMovieInfos();
    }

    @FXML
    private void deleteSelectedMovieButtonPressed(ActionEvent event){
        askDB("removeMovie");
    }

    @FXML
    private void displayMovies(){
        for (MovieInfo movieInfo : this.movieInfos) {
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
                    imageView.setId(movieInfo.getName());

                    Label nameLabel = new Label(movieInfo.getName());
                    nameLabel.setStyle("-fx-font-family: Constantia; -fx-font-size: 19px; -fx-font-weight: bold;"); // Set font size and make text bold
                    nameLabel.setAlignment(Pos.CENTER);

                    VBox imageContainer = new VBox();
                    imageContainer.getChildren().addAll(imageView, nameLabel);
                    imageContainer.setAlignment(Pos.CENTER);
                    imageContainer.setMaxWidth(220);
                    imageContainer.setMaxHeight(250);
                    imageContainer.setId(movieInfo.getName());
                    imageContainer.setOnMouseClicked(this::handleImageClick);

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

    @Subscribe
    public void catchBackgroundImage(BackgroundImageEvent event){
        byte[] data=event.getMessage().getImageData();
        setBackground(data);
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
                this.backgroundImageView.setFitWidth(600);
                this.backgroundImageView.setFitHeight(600);
                this.backgroundImageView.setPreserveRatio(false);

                });
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    void initialize(){
        EventBus.getDefault().register(this);
        msgId=0;
        askDB("getBackgroundImage");
        cancelSelectionButton.setVisible(false);
        deleteSelectedMovieButton.setVisible(false);
        setMovieInfos();
    }
}

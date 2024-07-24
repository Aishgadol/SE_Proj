package client;

import entities.Message;
import entities.MovieInfo;
import entities.UserInfo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class CustomerMainScreenController {
    private int msgId;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private List<MovieInfo> movieInfoList;
    private MovieInfo selectedMovieInfo=null;
    private UserInfo myUserInfo;
    private String currUserID;
    private HBox chosenImageContainer=null;
    private MovieInfo currMovieInfo;

    @FXML
    private Button purchaseTicketButton;
    @FXML
    private ScrollPane movieInfoScrollPane;
    @FXML
    private Button availableMoviesButton;
    @FXML
    private HBox selectedMovieHBox;
    @FXML
    private Label infoLabel;
    @FXML
    private Button cancelSelectionButton;
    @FXML
    private ImageView backgroundImageView;
    @FXML
    private Label movieInfoLabel;
    @FXML
    private Button disconnectButton;

    @FXML
    private HBox imageHBox;

    @FXML
    private ScrollPane imageScrollPane;

    @FXML
    private Button upcomingMoviesButton;



    @FXML
    private void purchaseTicketPressed(ActionEvent event){
        try {
            EventBus.getDefault().unregister(this);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/purchaseTicketScreen.fxml"));
            root = loader.load();
            PurchaseTicketScreenController controller = loader.getController();
            controller.setCurrUserID(this.currUserID);
            controller.setImageContainer(this.chosenImageContainer);
            stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
            scene=new Scene(root,800,800);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setOnCloseRequest(some_event->handleCustomerClose(currUserID));
            stage.setTitle("Purchase Ticket");
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void cancelSelection(ActionEvent event){
        askDB("getTitles");
        selectedMovieHBox.setDisable(true);
        selectedMovieHBox.setVisible(false);
        cancelSelectionButton.setVisible(false);
        this.purchaseTicketButton.setVisible(false);
        this.purchaseTicketButton.setDisable(true);
        cancelSelectionButton.setDisable(true);
        upcomingMoviesButton.setDisable(false);
        upcomingMoviesButton.setVisible(true);
        movieInfoScrollPane.setVisible(false);
        availableMoviesButton.setDisable(false);
        availableMoviesButton.setVisible(true);
        movieInfoLabel.setVisible(false);
        imageHBox.setVisible(true);
        imageHBox.setDisable(false);
        imageScrollPane.setVisible(true);
        imageScrollPane.setDisable(false);
        showAvailableMovies();
    }

    @FXML
    void setInfoLabel(String id){
        infoLabel.setText(id+ " is connected.");
    }

    @FXML
    private void disconnectButtonPressed(ActionEvent event) {
        askDB("disconnectCustomer "+currUserID);
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
    private List<MovieInfo> getAvailableMovieInfoList(){
        List<MovieInfo> list=new ArrayList<>();
        for(MovieInfo m: this.movieInfoList){
            if(m.getStatus().equals("Available")){
                list.add(m);
            }
        }
        return list;
    }
    private List<MovieInfo> getUpcomingMovieInfoList(){
        List<MovieInfo> list=new ArrayList<>();
        for(MovieInfo m: this.movieInfoList){
            if(m.getStatus().equals("Upcoming")){
                list.add(m);
            }
        }
        return list;
    }
    @FXML
    private void showUpcomingMovies(){
        this.upcomingMoviesButton.setDisable(true);
        this.availableMoviesButton.setDisable(false);
        clearDisplay();
        displayMovies(getUpcomingMovieInfoList());

    }
    @FXML
    private void showAvailableMovies(){
        this.upcomingMoviesButton.setDisable(false);
        this.availableMoviesButton.setDisable(true);
        clearDisplay();
        displayMovies(getAvailableMovieInfoList());
    }

    @FXML
    private void handleImageClick(MouseEvent event){
        VBox clickedImageImageView=(VBox) event.getSource();
        String title=clickedImageImageView.getId();
        for(MovieInfo m: this.movieInfoList){
            if(m.getName().equals(title)){
                this.selectedMovieInfo=m;
            }
        }
        movieInfoScrollPane.setVisible(true);
        movieInfoLabel.setText(this.selectedMovieInfo.toString());
        movieInfoLabel.setVisible(true);
        cancelSelectionButton.setVisible(true);
        cancelSelectionButton.setDisable(false);
        imageHBox.setVisible(false);
        imageHBox.setDisable(true);
        availableMoviesButton.setVisible(false);
        availableMoviesButton.setDisable(true);
        upcomingMoviesButton.setDisable(true);
        this.purchaseTicketButton.setVisible(true);
        this.purchaseTicketButton.setDisable(false);
        upcomingMoviesButton.setVisible(false);
        imageScrollPane.setDisable(true);
        imageScrollPane.setVisible(false);
        selectedMovieHBox.setVisible(true);
        selectedMovieHBox.getChildren().clear();
        selectedMovieHBox.getChildren().add(clickedImageImageView);
        this.chosenImageContainer=selectedMovieHBox;
        selectedMovieHBox.setAlignment(Pos.CENTER);
        clickedImageImageView.setScaleX(1.8);
        clickedImageImageView.setScaleY(1.8);
        //HBox.setHgrow(clickedImageImageView, Priority.ALWAYS);
    }

     @FXML
    private void displayMovies(List<MovieInfo> movieInfoList) {
        for (MovieInfo movieInfo : movieInfoList) {
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
                    imageContainer.setId(movieInfo.getName());
                    imageContainer.setMaxWidth(220);
                    imageContainer.setMaxHeight(260);
                    if(movieInfo.getStatus().equals("Available")){imageContainer.setOnMouseClicked(this::handleImageClick);}
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
        this.movieInfoList=event.getMessage().getMovieInfoList();
        clearDisplay();
        displayMovies(getAvailableMovieInfoList());
    }
    @Subscribe
    public void catchBackgroundImage(BackgroundImageEvent event){
        byte[] data=event.getMessage().getImageData();
        setBackground(data);
    }

    public void setCurrUserID(String id){
        this.currUserID=id;
    }

    private void askDB(String title){
        try {
            Message message = new Message(msgId++, title);
            SimpleClient.getClient().sendToServer(message);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void handleCustomerClose(String id){
        askDB("disconnectCustomer "+id);
        stage.close();
    }
    private void handleWorkerClose(String name){
        askDB("disconnectWorker "+name);
        stage.close();
    }

    @FXML
    private void initialize(){
        EventBus.getDefault().register(this);
        msgId=0;
        this.cancelSelectionButton.setDisable(true);
        this.cancelSelectionButton.setVisible(false);
        this.availableMoviesButton.setDisable(true);
        this.movieInfoScrollPane.setVisible(false);
        this.selectedMovieHBox.setVisible(false);
        this.imageHBox.setVisible(true);
        this.purchaseTicketButton.setVisible(false);
        this.purchaseTicketButton.setDisable(true);
        this.imageScrollPane.setVisible(true);
        askDB("getBackgroundImage");
        askDB("getTitles");
        askDB("getUsers");
    }

}

package client;

import entities.Message;
import entities.MovieInfo;
import entities.UserInfo;
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
import java.util.ArrayList;
import java.util.List;

public class StartingScreenController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private MovieInfo currMovieInfo;
    private int msgId;
    private List<MovieInfo> movieInfoList=new ArrayList<>();
    boolean customerLoginModeEnabled=true;
    boolean userConnected;
    private List<UserInfo> userInfoList=new ArrayList<>();
    @FXML
    private ImageView backgroundImageView;
    @FXML
    private Button upcomingMoviesButton;
    @FXML
    private Button availableMoviesButton;
    @FXML
    private Button workerLoginButton;
    @FXML
    private TextField nameTextField;

    @FXML
    private HBox imageHBox;

    @FXML
    private ScrollPane imageScrollPane;
    @FXML
    private Label statusLabel;
    @FXML
    private Button loginButton;
    @FXML
    private VBox screen;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button changeModeButton;


    @FXML
    void showUpcomingMovies(){
        this.upcomingMoviesButton.setDisable(true);
        this.availableMoviesButton.setDisable(false);
        clearDisplay();
        displayMovies(getUpcomingMovieInfoList());

    }
    @FXML
    void showAvailableMovies(){
        this.upcomingMoviesButton.setDisable(false);
        this.availableMoviesButton.setDisable(true);
        clearDisplay();
        displayMovies(getAvailableMovieInfoList());
    }
    @FXML
    void changeModeButtonPressed(ActionEvent event){
        if(customerLoginModeEnabled){
            //change to worker login
            statusLabel.setText("Current Login Mode: Worker Login");
            passwordField.setVisible(true);
            this.nameTextField.setPromptText("Enter Worker name");
            customerLoginModeEnabled=false;
        }
        else{
            //change to customer login
            statusLabel.setText("Current Login Mode: Customer Login");
            passwordField.setVisible(false);
            this.nameTextField.setPromptText("Enter Customer ID");
            customerLoginModeEnabled=true;
        }
    }
    @FXML
    void loginButtonPressed(ActionEvent event) {
        if(nameTextField.getText().isEmpty()){
            Platform.runLater(()->{
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Name error");
                alert.setHeaderText("Name inserted is too short");
                alert.show();
            });
        }
        else{
            //check user exists, then transfer to customer/worker page (update that the custoemr/worker has connected too)
            askDB("getUsers");
            String chosenName=nameTextField.getText().toLowerCase();
            UserInfo u0=null;
            for(UserInfo u: this.userInfoList) {
                if(u.getName().toLowerCase().equals(chosenName)){
                    u0=u;
                    break;
                }
            }
            if(u0==null){
                popUserDoesntExistMessage(chosenName);
                return;
            }
            if(customerLoginModeEnabled){
                if(u0.getRole().equals("Customer")){
                    if(u0.getConnected()==0) {
                        askDB("connectCustomer " + chosenName);
                        changeToCustomerScreen(chosenName, event);
                    }
                    else{
                        popUserAlreadyConnectedMessage(chosenName);
                    }
                }
                else{
                    popUserDoesntExistMessage(chosenName);
                }
            }
            else{
                //also check if password is good(we got here so id is good)
                if(!u0.getRole().equals("General Manager") && !u0.getRole().equals("Cinema Manager") && !u0.getRole().equals("Content Manager") && !u0.getRole().equals("Customer Complaint Worker")){
                    Platform.runLater(()->{
                        Alert a=new Alert(Alert.AlertType.ERROR);
                        a.setTitle("User is not a worker");
                        a.setHeaderText("User named: "+chosenName+" is not a worker.");
                        a.setResizable(false);
                        a.show();
                    });
                    return;
                }
                if(passwordField.getText().equals(u0.getPassword())) {
                    if(u0.getConnected()==0) {
                        askDB("connectWorker " + chosenName);
                        changeToWorkerScreen(chosenName, u0.getRole(), event); //same issue here
                    }
                    else{
                        popUserAlreadyConnectedMessage(chosenName);
                    }
                }
                else{
                    Platform.runLater(()->{
                        Alert a=new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Incorrect password!");
                        a.setHeaderText("The password you've enetered is incorrect");
                        a.show();
                    });
                    return;
                }
            }
        }
    }
    @FXML
    private void changeToCustomerScreen(String name,ActionEvent event){
        try {
            EventBus.getDefault().unregister(this);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/customerMainScreen.fxml"));
            Parent root = loader.load();
            CustomerMainScreenController controller = loader.getController();
            controller.setCurrUserName(name);
            controller.setInfoLabel(name);
            stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
            scene=new Scene(root,1280,800);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setOnCloseRequest(some_event->handleCustomerClose(name));
            stage.setTitle("Customer Main Screen");
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void changeToWorkerScreen(String name,String role,ActionEvent event){
        try {
            EventBus.getDefault().unregister(this);
            FXMLLoader loader=null;
            String title="";
            switch(role){
                case "General Manager":
                    title="General Manager";
                    loader=new FXMLLoader(getClass().getResource("/generalManagerMainScreen.fxml"));
                    root = loader.load();
                    GeneralManagerMainScreenController controller=loader.getController();
                    controller.setCurrUserName(name);
                    break;
                case "Cinema Manager":
                    title="Cinema Manager";
                    loader=new FXMLLoader(getClass().getResource("/cinemaManagerMainScreen.fxml"));
                    root = loader.load();
                    CinemaManagerMainScreenController controller1=loader.getController();
                    controller1.setCurrUserName(name);
                    break;
                case "Content Manager":
                    title="Content Manager";
                    loader=new FXMLLoader(getClass().getResource("/contentManagerMainScreen.fxml"));
                    root = loader.load();
                    ContentManagerMainScreenController controller2=loader.getController();
                    controller2.setCurrUserName(name);
                    break;
                case "Customer Complaint Worker":
                    title="Customer Complaint Worker";
                    loader=new FXMLLoader(getClass().getResource("/ccwMainScreen.fxml"));
                    root = loader.load();
                    CCWMainScreenController controller3=loader.getController();
                    controller3.setCurrUserName(name);
                    break;
                default:
                    System.out.println("Illegal role");
                    break;
            }
            stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
            scene=new Scene(root,1280,800);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setOnCloseRequest(some_event->handleWorkerClose(name));
            stage.setTitle(title+" Main Screen");
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void popUserAlreadyConnectedMessage(String name){
        Platform.runLater(()->{
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setTitle("User already connected!");
            a.setHeaderText("User "+name+" is already connected.");
            a.show();
        });
    }
    @FXML
    private void popUserDoesntExistMessage(String name){
        Platform.runLater(()->{
                        Alert a=new Alert(Alert.AlertType.ERROR);
                        a.setTitle("User does not exist!");
                        a.setHeaderText("User "+name+" does not exist.");
                        a.show();
                    });
    }
    @FXML
    private void popMessageWithMovieInfo(String title){
        for(MovieInfo m: this.movieInfoList){
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
    @Subscribe
    public void catchUserInfoList(UserInfoListEvent event){
        this.userInfoList=event.getMessage().getUserInfoList();
    }

    private boolean isCustomer(String name){
        for(UserInfo u: this.userInfoList){
            if (u.getName().toLowerCase().equals(name)) {
                return u.getRole().equals("Customer");
            }
        }
        return false;
    }
    private UserInfo getUserInfoByName(String name){
        for(UserInfo u: userInfoList){
            if (u.getName().toLowerCase().equals(name)){
                return u;
            }
        }
        return null;
    }
    private void askDB(String title){
        try {
            Message message = new Message(msgId++, title);
            SimpleClient.getClient().sendToServer(message);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void handleCustomerClose(String name){
        askDB("disconnectCustomer "+name);
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
        askDB("getBackgroundImage");
        askDB("getTitles");
        askDB("getUsers");
        this.passwordField.setVisible(false);
        this.availableMoviesButton.setDisable(true);
        //listener to limit inputID length and allowed characters
        this.nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 30) {
                this.nameTextField.setText(oldValue); // revert to old value if exceeds max length
            }
            if (!newValue.matches("[a-zA-z ']*")) {
                this.nameTextField.setText(oldValue);
            }
        });
        //listener to limit password length and allowed characters
        this.passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 25) {
                this.passwordField.setText(oldValue); // revert to old value if exceeds max length
            }
            if (!newValue.matches("[a-zA-Z0-9., !@#$%^&*()/]*")) {
                this.passwordField.setText(oldValue);
            }
        });
    }
}

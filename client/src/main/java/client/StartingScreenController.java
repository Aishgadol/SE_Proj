package client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import entities.CinemaInfo;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartingScreenController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private MovieInfo currMovieInfo;
    private int msgId;
    private List<MovieInfo> movieInfoList=new ArrayList<>();
    boolean customerLoginModeEnabled=true;
    boolean signupModeEnabled=false;
    boolean userConnected;
    private String selectedGenre;
    private String selectedDate;
    private String selectedCinemaName;
    private List<UserInfo> userInfoList=new ArrayList<>();
    @FXML
    private ImageView backgroundImageView;
    @FXML
    private ComboBox<String> dateFilter;
    @FXML
    private ComboBox<String> genreFilter;
    @FXML
    private Pane filterPane;
    @FXML
    private ComboBox<String> cinemaFilter;
    @FXML
    private Button upcomingMoviesButton;
    @FXML
    private Button availableMoviesButton;
    @FXML
    private Button workerLoginButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private Button selectFiltersButton;
    @FXML
    private HBox imageHBox;
    @FXML
    private Label signupLabel1;
    @FXML
    private Label signupLabel2;
    @FXML
    private Label loginHeadLabel;
    @FXML
    private Button signupButton;
    @FXML
    private TextField signupNameTextField;
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
    private Button applyFiltersButton;

    private List<CinemaInfo> cinemaInfoList=new ArrayList<>();

    @FXML
    private void selectFiltersButtonPressed(ActionEvent event){
        this.imageHBox.setVisible(false);
        this.imageScrollPane.setVisible(false);
        this.imageHBox.setDisable(true);
        this.imageScrollPane.setDisable(true);
        this.filterPane.setVisible(true);
        this.filterPane.setDisable(false);
        this.genreFilter.setVisible(true);
        this.genreFilter.setDisable(false);
        this.cinemaFilter.setVisible(true);
        this.cinemaFilter.setDisable(false);
        this.dateFilter.setVisible(true);
        this.dateFilter.setDisable(false);
        this.applyFiltersButton.setDisable(false);
        this.applyFiltersButton.setVisible(true);
    }
    @FXML
    private void applyFiltersButtonPressed(ActionEvent event){
        this.imageHBox.setVisible(true);
        this.imageScrollPane.setVisible(true);
        this.imageHBox.setDisable(false);
        this.imageScrollPane.setDisable(false);
        this.filterPane.setVisible(false);
        this.filterPane.setDisable(true);
        this.genreFilter.setVisible(false);
        this.genreFilter.setDisable(true);
        this.cinemaFilter.setVisible(false);
        this.cinemaFilter.setDisable(true);
        this.dateFilter.setVisible(false);
        this.dateFilter.setDisable(true);
        this.applyFiltersButton.setDisable(true);
        this.applyFiltersButton.setVisible(false);
    }

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
    }/*
    @FXML
    void showFilteredMovies(){
        clearDisplay();
        displayMovies(getFilteredMovieInfoList()); // havent wrote this funcion yet
        
    }*/
    @FXML
    void changeModeButtonPressed(ActionEvent event){
        if(customerLoginModeEnabled){
            //change to worker login
            statusLabel.setText("Current Login Mode: Worker Login");
            passwordField.setVisible(true);
            this.nameTextField.setPromptText("Enter Worker name");
            this.passwordField.clear();
            this.nameTextField.clear();
            customerLoginModeEnabled=false;
            this.signupLabel1.setVisible(false);
            this.signupLabel2.setVisible(false);
            this.signupButton.setDisable(true);
            this.signupButton.setVisible(false);
            this.passwordField.setDisable(false);
        }
        else{
            //change to customer login
            statusLabel.setText("Current Login Mode: Customer Login");
            this.nameTextField.clear();
            this.passwordField.clear();
            passwordField.setVisible(false);
            this.nameTextField.setPromptText("Enter Customer ID");
            customerLoginModeEnabled=true;
            this.signupLabel1.setVisible(true);
            this.signupLabel2.setVisible(true);
            this.signupButton.setDisable(false);
            this.signupButton.setVisible(true);
        }
    }
    @FXML
    void loginButtonPressed(ActionEvent event) {
        if(nameTextField.getText().length()!=9){
            Platform.runLater(()->{
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Name or ID error");
                alert.setHeaderText("Name or ID inserted is too short");
                alert.show();
            });
            return;
        }
        else{
            //check user exists, then transfer to customer/worker page (update that the custoemr/worker has connected too)
            askDB("getUsers");
            UserInfo u0=null;
            String chosenID="";
            String chosenName="";
            if(customerLoginModeEnabled){
                chosenID=nameTextField.getText();
                for(UserInfo u:this.userInfoList){
                    if(u.getId().equals(chosenID)){
                        u0=u;
                        break;
                    }
                }
                if(u0==null && signupModeEnabled){
                    //check name given is not empty
                    if(signupNameTextField.getText().isEmpty()){
                        Platform.runLater(()->{
                            Alert alert=new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Name error");
                            alert.setHeaderText("Name inserted is too short");
                            alert.show();
                        });
                        return;
                    }
                    //we checked user customer doesnt exist so we need to sign him up and then log him in
                    Message message=new Message(msgId++,"addCustomer");
                    u0=new UserInfo(this.nameTextField.getText(),this.signupNameTextField.getText());
                    this.userInfoList.add(u0);
                    message.setUserInfo(u0);
                    askDB(message);
                }
            }
            //if worker mode
            else{
                chosenName=nameTextField.getText().toLowerCase();
                for(UserInfo u: this.userInfoList) {
                    if(u.getName().toLowerCase().equals(chosenName)){
                        u0=u;
                        break;
                    }
                }
            }
            if(u0==null && !signupModeEnabled){
                popUserDoesntExistMessage(nameTextField.getText());
                return;
            }
            if(customerLoginModeEnabled){
                if(u0.getRole().equals("Customer")){
                    if(u0.getConnected()==0) {
                        askDB("connectCustomer " + chosenID);
                        u0.setConnected(1);
                        changeToCustomerScreen(chosenID, event);
                        return;
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
                //also check if password is good(we got here so worker name is good)
                if(!u0.getRole().equals("General Manager") && !u0.getRole().equals("Cinema Manager") && !u0.getRole().equals("Content Manager") && !u0.getRole().equals("Customer Complaint Worker")){
                    popNotAWorkerMessage(chosenName);
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
    private void changeToCustomerScreen(String id,ActionEvent event){
        try {
            EventBus.getDefault().unregister(this);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/customerMainScreen.fxml"));
            Parent root = loader.load();
            CustomerMainScreenController controller = loader.getController();
            controller.setCurrUserID(id);
            controller.setInfoLabel(id);
            stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
            scene=new Scene(root,1280,800);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setOnCloseRequest(some_event->handleCustomerClose(id));
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
                    return;
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
    private void popUserFailedToRegister(){
        Platform.runLater(()->{
            Alert a=new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Failed to register user");
                        a.setHeaderText("I failed :(");
                        a.show();
        });
    }
    @FXML
    private void popUserSuccesfullyAdded(String name, String id) {
        Platform.runLater(()->{
                        Alert a=new Alert(Alert.AlertType.INFORMATION);
                        a.setTitle("User successfully added!");
                        a.setHeaderText("User "+name+" with ID: "+id+" has been succefully added, moving to Customer screen!");
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
    private void popNotAWorkerMessage(String name){
        Platform.runLater(()->{
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setTitle("User is not a worker");
            a.setHeaderText("User named: "+name+" is not a worker.");
            a.setResizable(false);
            a.show();
        });
        return;
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
    private void signupButtonPressed(ActionEvent event){
        if(signupModeEnabled){ //this is we're already in the signup mode and we press the button to go back
            this.signupModeEnabled=false;
            this.signupButton.setText("Sign up");
            this.passwordField.setVisible(false);
            this.passwordField.setDisable(true);
            this.nameTextField.clear();
            this.passwordField.clear();
            this.signupLabel1.setVisible(true);
            this.signupLabel2.setVisible(true);
            this.loginButton.setText("Login");
            this.changeModeButton.setDisable(false);
            this.changeModeButton.setVisible(true);
            this.signupNameTextField.setDisable(true);
            this.signupNameTextField.setVisible(false);
            this.statusLabel.setVisible(true);
            this.signupNameTextField.clear();
            this.loginHeadLabel.setText("Login");
        }
        else { //this is the case where its in normal custoemr login and goes into signup mode
            this.signupModeEnabled = true;
            this.loginButton.setText("Signup and Login"); //need to add check that this user is not already registerd, by id
            this.passwordField.setVisible(false);
            this.passwordField.setDisable(true);
            this.signupLabel1.setVisible(false);
            this.signupLabel2.setVisible(false);
            this.nameTextField.clear();
            this.signupNameTextField.setDisable(false);
            this.signupNameTextField.setVisible(true);
            this.signupNameTextField.clear();
            this.signupNameTextField.setPromptText("Enter name");
            this.loginHeadLabel.setText("Sign up");
            this.passwordField.clear();
            this.signupButton.setText("Cancel");
            this.loginButton.setText("Complete Signup and Login");
            this.loginButton.setWrapText(true);
            this.changeModeButton.setDisable(true);
            this.changeModeButton.setVisible(false);
            this.statusLabel.setVisible(false);
        }
    }
    @FXML
    private void cancelSignup(ActionEvent event){

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
    public void catchCinemaInfoList(CinemaInfoListEvent event){
        this.cinemaInfoList=event.getMessage().getCinemaInfoList();
        for(CinemaInfo c:this.cinemaInfoList){
            this.cinemaFilter.getItems().clear();
            this.cinemaFilter.getItems().add(c.getName());
        }
    }
    @Subscribe
    public void catchNewUserInfo(CustomerAddedEvent event){
        if (event.getMessage().getMessage().contains("wasnt")){
            popUserFailedToRegister();
        }
        else{
            UserInfo u=event.getMessage().getUserInfo();
            this.userInfoList.add(u);
            popUserSuccesfullyAdded(u.getName(),u.getId());
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
    private void askDB(Message message){
        try{
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
    private void initialize() {
        EventBus.getDefault().register(this);
        msgId = 0;
        askDB("getBackgroundImage");
        askDB("getTitles");
        askDB("getUsers");
        this.nameTextField.setPromptText("Enter Customer ID");
        this.passwordField.setVisible(false);
        this.availableMoviesButton.setDisable(true);
        //listner for signupNameTextField to take only characters and spaces up to length 30
        this.signupNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
               if (newValue.length() > 30) {
                    this.signupNameTextField.setText(oldValue); // revert to old value if exceeds max length
                }
                if (!newValue.matches("[a-zA-z ']*")) {
                    this.signupNameTextField.setText(oldValue);
                }
        });
        //listener to limit inputID length and allowed characters
        this.nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (customerLoginModeEnabled) {
                if (newValue.length() > 9) {
                    this.nameTextField.setText(oldValue); // revert to old value if exceeds max length
                }
                if (!newValue.matches("[0-9]*")) {
                    this.nameTextField.setText(oldValue);
                }
            } else {
                if (newValue.length() > 30) {
                    this.nameTextField.setText(oldValue); // revert to old value if exceeds max length
                }
                if (!newValue.matches("[a-zA-z ']*")) {
                    this.nameTextField.setText(oldValue);
                }
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
        //seting genre filter combo box
        List<String> genres = Arrays.asList("Action", "Comedy", "Horror", "Drama", "Romance");
        this.genreFilter.getItems().addAll(genres);
        //setting date filter combobox
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate today = LocalDate.now();
        List<String> dateList = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            LocalDate date = today.plusDays(i);
            String formattedDate = date.format(formatter);
            dateList.add(formattedDate);
        }
        this.dateFilter.getItems().addAll(dateList);
        askDB("getCinemas");
        //setting cinema filter combobox <--- TO DO

    }
}

package client;

import entities.Message;
import entities.MovieInfo;
import entities.UserInfo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.awt.image.BufferedImage;

import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class AddMovieController {
    private int msgId;
    private boolean picChosen=false;
    private Stage stage;
    private Scene scene;
    private MovieInfo currMovieInfo;
    private byte[] currImageData;
    private UserInfo currUserInfo;
    @FXML
    private ImageView backgroundImageView;
    @FXML
    private Button goBackButton;
    @FXML
    private Button addMovieButton;
    @FXML
    private TextField movieNameTextField;
    @FXML
    private ComboBox<String> chooseYearComboBox;
    @FXML
    private Button uploadPhotoButton;
    @FXML
    private ImageView currImage;
    @FXML
    private ComboBox<String> genreComboBox;
    @FXML
    private TextArea summaryTextArea;
    @FXML
    private TextField actorsTextField;
    @FXML
    private TextField producerTextField;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private Label connectedAsLabel;

    public void setCurrUserInfo(UserInfo u){
        this.currUserInfo=u;
        this.connectedAsLabel.setText(this.currUserInfo.getName());
    }

    private void setCurrMovieInfo(){
        this.currMovieInfo=new MovieInfo(this.movieNameTextField.getText(),
                this.chooseYearComboBox.getValue(),this.genreComboBox.getValue(),
                this.producerTextField.getText(),this.actorsTextField.getText(),this.summaryTextArea.getText(),this.statusComboBox.getValue());
        this.currMovieInfo.setImageData(this.currImageData);
    }

    @FXML
    private void uploadPhotoButtonPressed(ActionEvent event){
        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files","*.png","*.jpg","*.jpeg"));
        List<File> files=fileChooser.showOpenMultipleDialog(this.movieNameTextField.getScene().getWindow());
        if(files!=null){
            if(files.size()>0 && files.size()<4) {
                for (File f : files) {
                    try (FileInputStream fis = new FileInputStream(f);
                         ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }
                        this.currImageData = bos.toByteArray();
                        //convert file to byte array so we can send in message
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Image image = new Image(f.toURI().toString());
                    this.currImage.setPreserveRatio(false);
                    Tooltip tooltip = new Tooltip("The photo you uploaded");
                    Tooltip.install(this.currImage, tooltip);
                    this.currImage.setImage(image);
                }
            }
            else{
                System.out.println("error");
            }
        }
        if(this.currImage.getImage()!=null){
            picChosen=true;
        }
        else{
            picChosen=false;
        }
    }
    @FXML
    private void addMovieButtonPressed(ActionEvent event){
        if(this.movieNameTextField.getText()!=null && this.movieNameTextField.getText().matches(".*[a-zA-Z]+.*")){
            if(this.chooseYearComboBox.getValue()!=null){
                if(this.currImage.getImage()!=null){
                    if(this.actorsTextField!=null){
                        if(this.summaryTextArea!=null){
                            if(this.producerTextField!=null){
                                if(this.genreComboBox.getValue()!=null){
                                    setCurrMovieInfo();
                                    askDB("addMovie");
                                }
                                else{
                                    showErrorPopUp("a Genre");
                                }
                            }
                            else{
                                showErrorPopUp("a Producer Name");
                            }
                        }
                        else{
                            showErrorPopUp("a ShortSummary");
                        }
                    }
                    else{
                        showErrorPopUp("Names of Actors");
                    }
                }
                else{
                    showErrorPopUp("a Movie Poster");
                }
            }
            else{
                showErrorPopUp("a Year of Release");
            }
        }
        else{
            showErrorPopUp("a Movie Name");
        }
    }
    @FXML
    private void showErrorPopUp(String error){
        Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error, You forgot to choose "+error);
                alert.setHeaderText("You forgot to choose "+error+".");
                alert.setContentText("Please close this window, and make sure to select "+error);
                alert.show();
            });
    }

    @FXML
    private void goBackButtonPressed(ActionEvent event){
        EventBus.getDefault().unregister(this);
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/updateTimeScreen.fxml"));
            Parent root = loader.load();
            UpdateController c=loader.getController();
            c.setCurrUserInfo(currUserInfo);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1280, 800);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setOnCloseRequest(some_event->handleWorkerClose(currUserInfo.getName()));
            stage.setTitle("Update Screen");
            stage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
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
                this.backgroundImageView.setFitWidth(600);
                this.backgroundImageView.setFitHeight(600);
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

    @Subscribe
    public void catchMovieAdded(MovieAddedSuccesfullyEvent event){
        Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Added movie to database succesfully!");
                alert.setHeaderText("Succesfully added the movie to the database");
                alert.setContentText("The movie was added to the database, succesfully!");
                alert.show();
            });
    }
    @Subscribe
    public void catchMovieAlreadyExists(MovieAlreadyExistsEvent event){
        Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Movie already exists!");
                alert.setHeaderText("You are trying to add a movie that already exists!");
                alert.setContentText("The movie you tried to add already exists in the database");
                alert.show();
            });
    }

    void askDB(String title){
        try{
            Message message=new Message(msgId++,title);
            if(title.equals("addMovie")){
                setCurrMovieInfo();
                message.setMovieInfo(this.currMovieInfo);
            }
            SimpleClient.getClient().sendToServer(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private List<String> generateYears(){
        List<String> years=new ArrayList<>();
        int curYear= Year.now().getValue();
        for(int i=1915;i<=curYear;i++){
            years.add(String.valueOf(i));
        }
        return years;
    }
    private void handleWorkerClose(String name){
        askDB("disconnectWorker "+name);
        stage.close();
    }
    @FXML
    void initialize(){
        EventBus.getDefault().register(this);
        msgId=0;
        askDB("getBackgroundImage");
        List<String> times=generateYears();//initialization of timeslots
        this.chooseYearComboBox.getItems().addAll(times);
        this.genreComboBox.getItems().addAll("Action", "Comedy", "Romance","Drama","Horror");
        this.statusComboBox.getItems().addAll("Available","Upcoming");
        this.summaryTextArea.setWrapText(true);

        //listener to limit name length
        this.movieNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 50) {
                this.movieNameTextField.setText(oldValue); // revert to old value if exceeds max length
            }
            if (!newValue.matches("[a-zA-Z0-9.,' -]*")) {
                this.movieNameTextField.setText(oldValue);
            }
        });
        //listener to limit usable characters of summary and length
        this.summaryTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9., -]*")) {
                this.summaryTextArea.setText(oldValue);
            }
            if (newValue.length() > 150) {
                this.summaryTextArea.setText(oldValue); // revert to old value if exceeds max length
            }
        });
        //listener to limit actors length and allowed characters
        this.actorsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 100) {
                this.actorsTextField.setText(oldValue); // revert to old value if exceeds max length
            }
            if (!newValue.matches("[a-zA-Z0-9., -]*")) {
                this.actorsTextField.setText(oldValue);
            }
        });
        //listener to limit proucer length and allowed characters
        this.producerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 40) {
                this.producerTextField.setText(oldValue); // revert to old value if exceeds max length
            }
            if (!newValue.matches("[a-zA-Z0-9., -]*")) {
                this.producerTextField.setText(oldValue);
            }
        });

    }
}

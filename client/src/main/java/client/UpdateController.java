package client;

import entities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import javafx.util.Callback;
import javafx.scene.control.Button;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UpdateController{

    CinemaController cinemaController;
    public VBox update;
    private int msgId;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private MovieInfo movieInfo;
    private List<MovieInfo> movieInfos=new ArrayList<>();
    private List<DisplayTimeInfo> displayTimeInfoList=new ArrayList<>();
    private UserInfo currUserInfo;
    private boolean isMovieSelected=false;
    private List<CinemaInfo> cinemaInfoList=new ArrayList<>();

    @FXML
    private Label connectedAsLabel;
    @FXML
    private Button backButton;
    @FXML
    private ImageView backgroundImageView;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ImageView background;
    @FXML
    private ComboBox<String> titlesComboBox;
    @FXML
    private ListView<String> myListView;
    @FXML
    private Button addTimeButton;
    @FXML
    private Label addLabel;
    @FXML
    private ComboBox<String> availableTimesComboBox;
    @FXML
    private Button removeTimeButton;
    @FXML
    private Label removeLabel;
    @FXML
    private ComboBox<String> timePicker;
    @FXML
    private Button removeMovieButton;
    @FXML
    private Button addMovieButton;
    @FXML
    private ComboBox<String> cinemaPicker;

    @FXML
    void timeToAddWasChosen(){}
    @FXML
    void dateToAddWasChosen(){}

    public void setCurrUserInfo(UserInfo u){
        this.currUserInfo=u;this.connectedAsLabel.setText(currUserInfo.getName());
    }

    @FXML
    void removeTimeButtonPressed(ActionEvent event){
        if(availableTimesComboBox.getValue() == null){
            return;
        }
        String displayDateToRemove=availableTimesComboBox.getValue();
        askDB("removetime "+displayDateToRemove);
        resetAll();
        updateAll();
    }

    @FXML
    void addTimeButtonPressed(ActionEvent event){
        String hour=timePicker.getValue();
        String date=datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String cinemaName=cinemaPicker.getValue();
        askDB("addtime "+hour+", "+date+", Movie: "+titlesComboBox.getValue()+", Cinema: "+cinemaName);
        resetAll();
        updateAll();
    }

    @FXML
    void timeToRemoveSelected(ActionEvent event){}

    @FXML
    private void removeMovieButtonPressed(ActionEvent event){
        EventBus.getDefault().unregister(this);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/removeMovieScreen.fxml"));
            Parent root = loader.load();
            RemoveMovieController controller = loader.getController();
            controller.setCurrUserInfo(currUserInfo);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 600, 600);
            stage.setScene(scene);
            stage.setOnCloseRequest(some_event2->handleWorkerClose(currUserInfo.getName()));
            stage.setResizable(false);
            stage.setTitle("Remove Movie");
            stage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }



    @FXML
    private void addMovieButtonPressed(ActionEvent event){
        EventBus.getDefault().unregister(this);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addMovieScreen.fxml"));
            Parent root = loader.load();
            AddMovieController controller = loader.getController();
            controller.setCurrUserInfo(currUserInfo);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 600, 600);
            stage.setScene(scene);
            stage.setOnCloseRequest(some_event->handleWorkerClose(currUserInfo.getName()));
            stage.setResizable(false);
            stage.setTitle("Add Movie");
            stage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    @FXML
    void resetAll() {
        Platform.runLater(() -> {
            datePicker.setValue(LocalDate.now());
            timePicker.setValue("10:00");
            cinemaPicker.setValue(null);
        });
    }
    @FXML
    void updateAll() {
        Platform.runLater(() -> {
            myListView.getItems().clear();
            availableTimesComboBox.getItems().clear();
            for(DisplayTimeInfo di: this.movieInfo.getDisplayTimeInfoList()){
                myListView.getItems().add(di.getDisplayTime());
                availableTimesComboBox.getItems().add(di.getDisplayTime());
            }
        });
    }



    void askDB(String title){
        try{
            Message message=new Message(msgId++,title);
            message.setMovieInfo(movieInfo);
            SimpleClient.getClient().sendToServer(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void showThings() {
        Platform.runLater(() -> {
            isMovieSelected = true;
            addLabel.setVisible(true);
            addTimeButton.setVisible(true);
            cinemaPicker.setVisible(true);
            datePicker.setVisible(true);
            timePicker.setVisible(true);
            myListView.setVisible(true);
            removeTimeButton.setVisible(true);
            removeLabel.setVisible(true);
            availableTimesComboBox.setVisible(true);
        });
    }

    @FXML
    void MovieHasBeenSelected(ActionEvent event) {
        askDB("getMovieInfo " + titlesComboBox.getValue());
        if (!isMovieSelected) {
            showThings();
        }
        resetAll();
        //updateAll();
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
    void goBackButton(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        String role=currUserInfo.getRole();
        Parent root=null;
        FXMLLoader loader;
        try {
            switch(role){
                case "General Manager":
                    loader=new FXMLLoader(getClass().getResource("/generalManagerMainScreen.fxml"));
                    root = loader.load();
                    GeneralManagerMainScreenController controller=loader.getController();
                    controller.setCurrUserInfo(currUserInfo);
                    break;
                case "Customer Complaint Worker":
                    loader=new FXMLLoader(getClass().getResource("/ccwMainScreen.fxml"));
                    root = loader.load();
                    CCWMainScreenController controller3=loader.getController();
                    controller3.setCurrUserInfo(currUserInfo);
                    break;
                case "Content Manager":
                    loader=new FXMLLoader(getClass().getResource("/contentManagerMainScreen.fxml"));
                    root = loader.load();
                    ContentManagerMainScreenController controller2=loader.getController();
                    controller2.setCurrUserInfo(currUserInfo);
                    break;
                case "Cinema Manager":
                    loader=new FXMLLoader(getClass().getResource("/cinemaManagerMainScreen.fxml"));
                    root = loader.load();
                    CinemaManagerMainScreenController controller1=loader.getController();
                    controller1.setCurrUserInfo(currUserInfo);
                    break;
                default:
                    root=FXMLLoader.load(getClass().getResource("/startingScreen.fxml"));
                    break;
            }
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1280, 800);
            stage.setScene(scene);
            stage.setTitle("Cinema");
            stage.setOnCloseRequest(some_event->handleWorkerClose(this.currUserInfo.getName()));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void catchBackgroundImage(BackgroundImageEvent event){
        byte[] data=event.getMessage().getImageData();
        setBackground(data);
    }
    @FXML
    @Subscribe
    public void getMoviesFromDB(MovieInfoListEvent event) {
        Message message = event.getMessage();
        this.movieInfos = message.getMovieInfoList();
        resetAll();
        //updateAll();
        Platform.runLater(() -> {
            titlesComboBox.getItems().clear();
            for (MovieInfo movieInfo : movieInfos) {
                titlesComboBox.getItems().add(movieInfo.getName());
            }
        });
    }
    @FXML
    @Subscribe
    public void getMovieInfoFromDB(MovieInfoEvent event){
        this.movieInfo=event.getMessage().getMovieInfo();
        this.displayTimeInfoList=this.movieInfo.getDisplayTimeInfoList();

        resetAll(); //already runs in platform.runlater();
        updateAll(); //same
    }
    @FXML
    @Subscribe
    public void timeUpdateRecieved(TimeUpdateEvent event) {
        if(!this.movieInfo.getName().equals(event.getMessage().getMovieInfo().getName())){
            resetAll();
            updateAll();
            return;
        }
        this.movieInfo = event.getMessage().getMovieInfo();
        this.displayTimeInfoList = this.movieInfo.getDisplayTimeInfoList();
        resetAll();
        updateAll();
    }
     @Subscribe
    public void catchMovieRemoved(MovieRemovedSuccesfullyEvent event){
        askDB("getTitles");
        resetAll();
        updateAll();
    }
    @Subscribe
    public void catchMovieAdded(MovieAddedSuccesfullyEvent event){
        askDB("getTitles");
        resetAll();
        updateAll();
    }
    @FXML
    @Subscribe
    public void catchCinemaInfoList(CinemaInfoListEvent event){
        this.cinemaInfoList=event.getMessage().getCinemaInfoList();
        for(CinemaInfo ci: this.cinemaInfoList){
            cinemaPicker.getItems().add(ci.getName());
        }
    }


    private List<String> generateTimeSlots(){
        List<String> times=new ArrayList<>();
        LocalTime startTime= LocalTime.of(11,0);
        LocalTime endTime=LocalTime.of(1,0);
        while(!startTime.equals(endTime)){
            times.add(startTime.toString());
            startTime=startTime.plusMinutes(15);
        }
        return times;
    }
    private void handleWorkerClose(String name){
        askDB("disconnectWorker "+name);
        stage.close();
    }
    @FXML
    void initialize(){
        EventBus.getDefault().register(this);
        msgId=0;
        //initialization of datepicker
        LocalDate minDate=LocalDate.now();
        LocalDate maxDate=minDate.plusYears(1);
        datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item,boolean empty){
                        super.updateItem(item,empty);
                        if(item.isBefore(minDate) || item.isAfter(maxDate)){
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        });
        datePicker.setValue(minDate);

        List<String> times=generateTimeSlots();//initialization of timeslots
        timePicker.getItems().addAll(times);

        askDB("getBackgroundImage");
        askDB("getTitles");
        askDB("getCinemas");
    }
}


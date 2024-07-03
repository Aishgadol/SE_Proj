package client;

import entities.MovieInfo;
import entities.Message;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.time.LocalDate;
import javafx.util.Callback;
import javafx.scene.Parent;
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
    private List<MovieInfo> movieInfos;
    private List<String> displayTimes;

    private boolean isMovieSelected=false;

    @FXML
    private Button backButton;

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
    void timeToAddWasChosen(){

    }
    @FXML
    void dateToAddWasChosen(){

    }
    @FXML
    void removeTimeButtonPressed(ActionEvent event){

    }

    @FXML
    void addTimeButtonPressed(ActionEvent event){
        String hour=timePicker.getValue();
        String date=datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        askDB("addtime "+hour+", "+date);
        resetAll();


    }
    @Subscribe
    public void timeTaken(TimeTakenEvent event){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Time already in use");
            alert.setHeaderText("The date and time you're trying to add are already taken by this movie");
            alert.show();
        });
    }

    @FXML
    void timeToRemoveSelected(ActionEvent event){

    }

    @Subscribe // this will probably never happen
    public void getMovieInfoFromDB(MovieInfoEvent event){
        this.movieInfo=event.getMessage().getMovieInfo();
        this.displayTimes=this.movieInfo.getDisplayTimes();
        resetAll();
        for(String d : this.displayTimes){
            availableTimesComboBox.getItems().add(d);
            myListView.getItems().add(d);
        }
    }

    void resetAll(){
        datePicker.setValue(LocalDate.now());
        timePicker.setValue("10:00");
        availableTimesComboBox.getItems().clear();
        myListView.getItems().clear();
    }


    @Subscribe
    public void getMoviesFromDB(MovieInfoListEvent event){
        Message message=event.getMessage();
        List<MovieInfo> movieInfos=(List<MovieInfo>)message.getList();
        this.movieInfos=movieInfos;
        for(MovieInfo movieInfo: movieInfos){
            titlesComboBox.getItems().add(movieInfo.getName());
        }

    }

    @Subscribe
    public void timeUpdateRecieved(TimeUpdateEvent event){
        this.movieInfo=event.getMessage().getMovieInfo();
        this.displayTimes=this.movieInfo.getDisplayTimes();
        for(String d : this.displayTimes){
            myListView.getItems().add((d));
            availableTimesComboBox.getItems().add(d);
        }
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

    void showThings(){
            isMovieSelected=true;
            addLabel.setVisible(true);
            addTimeButton.setVisible(true);
            datePicker.setVisible(true);
            timePicker.setVisible(true);
            myListView.setVisible(true);
            removeTimeButton.setVisible(true);
            removeLabel.setVisible(true);
            availableTimesComboBox.setVisible(true);
    }

    @FXML
    void MovieHasBeenSelected(){
        String title=titlesComboBox.getValue();
        askDB("getMovieInfo "+title);
        if(!isMovieSelected){
            showThings();
        }

        resetAll();
        //this area updates the screen that shows list of times for that movie
        for(String d : displayTimes){
            myListView.getItems().setAll(displayTimes);
            availableTimesComboBox.getItems().setAll(displayTimes);
        }
    }

    @FXML
    void goBackButton(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        Parent root=FXMLLoader.load(getClass().getResource("/cinema.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root,1280,800);
        stage.setScene(scene);
        stage.setTitle("Cinema");
        stage.setResizable(false);
        stage.show();
    }
    @FXML
    void choseDate(ActionEvent event){

    }


    private List<String> generateTimeSlots(){
        List<String> times=new ArrayList<>();
        LocalTime startTime= LocalTime.of(10,0);
        LocalTime endTime=LocalTime.of(1,0);
        while(!startTime.equals(endTime)){
            times.add(startTime.toString());
            startTime=startTime.plusMinutes(30);
        }
        return times;
    }

    @FXML
    void initialize(){
        EventBus.getDefault().register(this);
        msgId=0;
        try {
			Message message = new Message(msgId, "add client");
			SimpleClient.getClient().sendToServer(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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



        askDB("getTitles");
    }
}


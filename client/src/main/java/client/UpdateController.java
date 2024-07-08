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
    private List<MovieInfo> movieInfos=new ArrayList<>();
    private List<String> displayTimes=new ArrayList<>();

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

        String displayDateToRemove=availableTimesComboBox.getValue();
        askDB("removetime "+displayDateToRemove);
        resetAll();
        updateAll();
    }


    @FXML
    void addTimeButtonPressed(ActionEvent event){
        String hour=timePicker.getValue();
        String date=datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        askDB("addtime "+hour+", "+date);
        resetAll();
        updateAll();
    }
    @Subscribe // no use for this function for now
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

    @FXML
    @Subscribe
    public void getMovieInfoFromDB(MovieInfoEvent event){
        this.movieInfo=event.getMessage().getMovieInfo();
        this.displayTimes=this.movieInfo.getDisplayTimes();
        resetAll(); //already runs in platform.runlater();
        updateAll(); //same
    }

    @FXML
    void resetAll() {
        Platform.runLater(() -> {
            datePicker.setValue(LocalDate.now());
            timePicker.setValue("10:00");
            //availableTimesComboBox.getItems().clear();
            //myListView.getItems().clear();
        });
    }
    @FXML
    void updateAll() {
        Platform.runLater(() -> {
            myListView.getItems().setAll(this.displayTimes);
            availableTimesComboBox.getItems().setAll(this.displayTimes);
        });
    }

    @FXML
    @Subscribe
    public void getMoviesFromDB(MovieInfoListEvent event) {
        Message message = event.getMessage();
        this.movieInfos = message.getList();
        Platform.runLater(() -> {
            for (MovieInfo movieInfo : movieInfos) {
                titlesComboBox.getItems().add(movieInfo.getName());
            }
        });
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
        this.displayTimes = this.movieInfo.getDisplayTimes();
        resetAll();
        updateAll();
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
            datePicker.setVisible(true);
            timePicker.setVisible(true);
            myListView.setVisible(true);
            removeTimeButton.setVisible(true);
            removeLabel.setVisible(true);
            availableTimesComboBox.setVisible(true);
        });
    }

    @FXML
    void MovieHasBeenSelected() {
        askDB("getMovieInfo " + titlesComboBox.getValue());
        if (!isMovieSelected) {
            showThings();
        }
        resetAll();
        updateAll();
    }

    @FXML
    void goBackButton(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        try {
            askDB("remove client");
            Parent root = FXMLLoader.load(getClass().getResource("/cinema.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1280, 800);
            stage.setScene(scene);
            stage.setTitle("Cinema");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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


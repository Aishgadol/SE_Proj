package client;

import entities.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.io.IOException;
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
    private boolean isMovieSelected=false;



    @FXML
    private Button backButton;

    @FXML
    private ImageView background;

    @FXML
    private ComboBox<String> titlesComboBox;

    @FXML
    private ListView<String> myListView;

    @FXML
    private VBox cinema;

    @FXML
    private Button addTimeButton;

    @FXML
    private Label addLabel;

    @FXML
    private ComboBox<String> availableDayComboBox;

    @FXML
    private ComboBox<String> availableHourComboBox;

    @FXML
    private ComboBox<String> availableMinuteComboBox;

    @FXML
    private ComboBox<String> availableMonthComboBox;

    @FXML
    private ComboBox<String> availableTimesComboBox;

    @FXML
    private ComboBox<String> availableYearComboBox;

    @FXML
    private Button removeTimeButton;

    @FXML
    private Label removeLabel;

    @FXML
    void addTime(ActionEvent event){

    }


    @FXML
    void selectedDay(ActionEvent event) {

    }

    @FXML
    void selectedHour(ActionEvent event) {

    }

    @FXML
    void selectedMinute(ActionEvent event) {

    }

    @FXML
    void selectedMonth(ActionEvent event) {

    }

    @FXML
    void selectedYear(ActionEvent event){

    }

    @FXML
    void selectedTimeToRemove(ActionEvent event){

    }



    @Subscribe
    public void getMovieInfoFromDB(MovieInfo movieInfo){
        this.movieInfo=movieInfo;

    }

    @Subscribe
    public void getMoviesFromDB(MessageEvent event){
        Message message=event.getMessage();
        List<MovieInfo> movieInfos=(List<MovieInfo>)message.getList();
        this.movieInfos=movieInfos;
        for(MovieInfo movieInfo: movieInfos){
            titlesComboBox.getItems().add(movieInfo.getName());
        }
    }

    void askDB(String title){
        try{
            Message message=new Message(msgId++,title);
            SimpleClient.getClient().sendToServer(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    void showThings(){
            isMovieSelected=true;
            addLabel.setVisible(true);
            addTimeButton.setVisible(true);
            availableDayComboBox.setVisible(true);
            availableMonthComboBox.setVisible(true);
            availableYearComboBox.setVisible(true);
            availableHourComboBox.setVisible(true);
            availableMinuteComboBox.setVisible(true);
            myListView.setVisible(true);
            removeTimeButton.setVisible(true);
            removeLabel.setVisible(true);
            availableTimesComboBox.setVisible(true);
    }

    @FXML
    void selectedMovie(){
        String title=titlesComboBox.getValue();
        askDB(title);
        if(!isMovieSelected){
            showThings();
        }
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
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
    void initialize(){
        EventBus.getDefault().register(this);
        msgId=0;
        askDB("getTitles");
        try {
			Message message = new Message(msgId, "add client");
			SimpleClient.getClient().sendToServer(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}


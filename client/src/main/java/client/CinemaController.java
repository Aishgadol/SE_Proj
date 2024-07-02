package client;

import entities.DisplayTime;
import entities.MovieInfo;
import entities.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.hibernate.sql.Update;


public class CinemaController {
    private int msgId;
    private MovieInfo movieInfo;
    private SimpleChatClient chatclient;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button updateButton;
    @FXML
    private ImageView automobiles_img;
    @FXML
    private ImageView background;
    @FXML
    private VBox cinema;
    @FXML
    private ImageView house_of_cards_img;
    @FXML
    private ImageView margol_img;
    @FXML
    private ImageView pulp_fiction_img;
    @FXML
    private ImageView scary_movie_img;
    @FXML
    private ImageView the_boys_img;



    @FXML
    void clickedMargol(MouseEvent event) {
        //askServer("Margol");
        //MovieInfo margolInfo=getMovieInfo("Margol");

        askDB("getMovieInfo Margol");
    }

    @FXML
    void clickedHouse(MouseEvent event) {

        askDB("getMovieInfo House of Cards");
    }

    @FXML
    void clickedPulpFiction(MouseEvent event) {

        askDB("getMovieInfo Pulp Fiction");
    }

    @FXML
    void clickedScaryMovie(MouseEvent event) {

        askDB("getMovieInfo Scary Movie");
    }

    @FXML
    void clickedTheBoys(MouseEvent event) {
        askDB("getMovieInfo The Boys");
    }

    @FXML
    void clickedAutomobiles(MouseEvent event){
        askDB("getMovieInfo Automobiles");
    }

    private String getAllDisplayTimes(List<DisplayTime> displayTimes){
        StringBuilder sb=new StringBuilder();
        for (DisplayTime displayTime : displayTimes) {
            sb.append(displayTime.toString());
        }
        return sb.toString();
    }

    @Subscribe
    public void catchMovieInfo(MovieInfo movieInfo){
        System.out.println("I've been invoked 1");
        this.movieInfo=movieInfo;
        Platform.runLater(()->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information on "+this.movieInfo.getName());
            alert.setHeaderText("This is information about the movie: "+this.movieInfo.getName()+"\n " +
                    "Released at: "+this.movieInfo.getReleasedate());
            alert.setContentText("Showtimes: "+getAllDisplayTimes(this.movieInfo.getDisplayTimes()));
            alert.show();
        });

    }

    @Subscribe
    public void catchSomethingElse(MovieInfo movieInfo){
        System.out.println("I've been invoked 2");
    }

    void askDB(String title){
        try{
            Message message=new Message(msgId++,title);
            SimpleClient.getClient().sendToServer(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    /*
    @FXML
    void sendToDb(ActionEvent event) {
        try {
            Message message = new Message(msgId++, textField.getText());
            textField.clear();
            SimpleClient.getClient().sendToServer(message);

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Subscribe
	public void setDataFromServerTF(Message msg) {
        board.setText(msg.getMessage());
	}

*/
    @FXML
    void changeToUpdateScreen(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        Parent root=FXMLLoader.load(getClass().getResource("/update.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root,1280,800);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Update Screen");
        stage.show();
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
    }
}

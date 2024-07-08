package client;

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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


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

        askDB("getMovieInfo Scary Movie 5");
    }

    @FXML
    void clickedTheBoys(MouseEvent event) {
        askDB("getMovieInfo The Boys");
    }

    @FXML
    void clickedAutomobiles(MouseEvent event){
        askDB("getMovieInfo Automobiles");
    }



    @Subscribe
    public void catchMovieInfo(MovieInfoEvent event){
        this.movieInfo=event.getMessage().getMovieInfo();
        Platform.runLater(()->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information on "+this.movieInfo.getName());
            alert.setHeaderText("This is information about the movie: "+this.movieInfo.getName()+"\n " +
                    "Released at: "+this.movieInfo.getReleaseDate());
            StringBuilder sb=new StringBuilder();
            for(String s : this.movieInfo.getDisplayTimes()){
                sb.append(s);
                sb.append("\n");
            }
            alert.setContentText("Showtimes: \n"+sb.toString());
            alert.show();
        });
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
        try {
            askDB("remove client");
            Parent root = FXMLLoader.load(getClass().getResource("/update.fxml"));
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

package client;

import entities.MovieInfo;
import entities.Message;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;


public class CinemaController {
    private int msgId;
    private MovieInfo movieInfo;

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


    private MovieInfo getMovieInfo(String title){
        return null;//placeholder
    }
    private void askServer(String title){
        return;//placeholder
    }

    private void popInformation(MovieInfo movieInfo){
        //Platform.runLater();
    }
    @FXML
    void clickedMargol(MouseEvent event) {
        //askServer("Margol");
        //MovieInfo margolInfo=getMovieInfo("Margol");

        popInformation(null);
        //this approach is useful cuz Platform.runLater blocks the window untill closed so u cant fuck around while reading movie info
        Platform.runLater(()->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information on Margol");
            alert.setHeaderText("This is header text");
            alert.setContentText("This is the content text");
            alert.show();
        });
    }

    @FXML
    void clickedHouse(MouseEvent event) {

        //this approach is useful cuz Platform.runLater blocks the window untill closed so u cant fuck around while reading movie info
        Platform.runLater(()->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information on House Of Cards");
            alert.setHeaderText("This is header text");
            alert.setContentText("This is the content text");
            alert.show();
        });
    }

    @FXML
    void clickedPulpFiction(MouseEvent event) {

        //this approach is useful cuz Platform.runLater blocks the window untill closed so u cant fuck around while reading movie info
        Platform.runLater(()->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information on Pulp Fiction");
            alert.setHeaderText("This is header text");
            alert.setContentText("This is the content text");
            alert.show();
        });

    }

    @FXML
    void clickedScaryMovie(MouseEvent event) {

        //this approach is useful cuz Platform.runLater blocks the window untill closed so u cant fuck around while reading movie info
        Platform.runLater(()->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information on Scary Movie 5");
            alert.setHeaderText("This is header text");
            alert.setContentText("This is the content text");
            alert.show();
        });
    }

    @FXML
    void clickedTheBoys(MouseEvent event) {

        //this approach is useful cuz Platform.runLater blocks the window untill closed so u cant fuck around while reading movie info
        Platform.runLater(()->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information on The Boys");
            alert.setHeaderText("This is header text");
            alert.setContentText("This is the content text");
            alert.show();
        });
    }

    @FXML
    void clickedAutomobiles(MouseEvent event){

        //this approach is useful cuz Platform.runLater blocks the window untill closed so u cant fuck around while reading movie info
        Platform.runLater(()->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information on Automobiles(Cars parody)");
            alert.setHeaderText("This is header text");
            alert.setContentText("This is the content text");
            alert.show();
        });
    }

    @Subscribe
    public void doNothing(Message msg){
        System.out.println("Message got was: "+msg.getMessage());
    }

    void askDB(String title){
        try{
            Message message=new Message(msgId++,title);
            SimpleClient.getClient().sendToServer(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Subscribe
    public void getMovieInfoFromDb(MovieInfo movieInfo){
      this.movieInfo=movieInfo;
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

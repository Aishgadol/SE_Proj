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
    public void catchMovieInfo(MovieInfo movieInfo){
        this.movieInfo=movieInfo;

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
    void changeToUpdateScreen(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        Parent root=FXMLLoader.load(getClass().getResource("/update.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root,1280,800);
        stage.setScene(scene);
        stage.setResizable(false);
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

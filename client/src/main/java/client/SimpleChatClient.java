package client;

import entities.Message;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class SimpleChatClient extends Application {

    private static Scene scene;
    private SimpleClient client;
    private Stage primaryStage;


    @Override
    public void start(Stage stage) throws IOException {
    	EventBus.getDefault().register(this);
    	client = SimpleClient.getClient();
    	client.openConnection();
        client.sendToServer(new Message(0,"add client"));
        primaryStage=stage;
        scene=new Scene(loadFXML("openingScreen"),600,600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        PauseTransition delay=new PauseTransition(Duration.millis(1798.0));
        delay.setOnFinished(event ->{
            try{
                //scene=new Scene(loadFXML("mainScreen"),1280,800);
                scene=new Scene(loadFXML("startingScreen"),1280,800);
                primaryStage.setScene(scene);
                primaryStage.setResizable(false);
                primaryStage.setTitle("Welcome Screen");
                primaryStage.show();
            } catch (IOException e){
                e.printStackTrace();
            }
        });
        delay.play();
        primaryStage.setTitle("Cinema");

    }


    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChatClient.class.getResource("/"+fxml + ".fxml"));

        return fxmlLoader.load();
    }


    @Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
    	EventBus.getDefault().unregister(this);
		super.stop();
	}

    @Subscribe
    public void onMessageEvent(MessageEvent message) {
        if(message.getMessage().getMessage().startsWith("EMPTY MESSAGE")){
            Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR,
                                     String.format("RECIEVED EMPTY MESSAGE, NOT DOING ANYTHING"));
            alert.setTitle("EMPTY MESSAGE ALERT");
            alert.setHeaderText("<WHAT IS THIS>");
            alert.show();
        });
        }
    }


	public static void main(String[] args) {
        launch();
    }

}
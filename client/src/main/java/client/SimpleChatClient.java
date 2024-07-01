package client;

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

    private void switchToScene(String fxml, int width, int height) throws IOException {
        Parent root = loadFXML(fxml);
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    private void switchToScene(String sceneName){
        PauseTransition delay=new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(event ->{
            try{
                switchToScene(sceneName,1280,800);
            } catch (IOException e){
                e.printStackTrace();
            }
        });
        delay.play();
    }


    @Override
    public void start(Stage stage) throws IOException {
    	EventBus.getDefault().register(this);
    	client = SimpleClient.getClient();
    	client.openConnection();
        primaryStage=stage;

        //initial scene setup
        /*
        scene=new Scene(loadFXML("opening"),600,400);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        */
        scene=new Scene(loadFXML("opening"),600,600);
        primaryStage.setScene(scene);
        primaryStage.show();
        switchToScene("cinema");
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

    /*
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
*/

	public static void main(String[] args) {
        launch();
    }

}
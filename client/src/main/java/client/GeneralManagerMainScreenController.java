package client;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GeneralManagerMainScreenController {
    private int msgId;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String currUserId;

    public void setCurrUserId(String id){
        this.currUserId=id;
    }
}

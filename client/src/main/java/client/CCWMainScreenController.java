package client;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CCWMainScreenController {

    private int msgId;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String currUserName;

    public void setCurrUserName(String name){
        this.currUserName=name;
    }
}

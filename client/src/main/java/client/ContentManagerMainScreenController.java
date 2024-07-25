package client;

import entities.UserInfo;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ContentManagerMainScreenController {
    private int msgId;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private UserInfo currUserInfo;

    public void setCurrUserInfo(UserInfo u){
        this.currUserInfo=u;
    }
}

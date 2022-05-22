package ykn.sovava.myclient.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Description: TODO
 *
 * @author: ykn
 * @date: 2022年05月22日 18:20
 **/
public class Login {
    public static void load(Stage stage){
        try {
            Parent root = FXMLLoader.load(Login.class.getResource("/fxml/login.fxml"));
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

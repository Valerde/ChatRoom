package ykn.sovava.myserver;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Description: 服务器程序入口
 * @author: ykn
 * @date: 2022年05月28日 14:45
 **/
public class ServerMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        new Server(primaryStage);
    }

    public static void main(String[] args) {

        launch(args);
    }
}

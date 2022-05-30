package ykn.sovava.myclient;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Description:客户端入口,TODO:有时间禁用群成员点击
 *
 * @author: ykn
 * @date: 2022年05月22日 18:15
 **/
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Director.getInstance().init(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

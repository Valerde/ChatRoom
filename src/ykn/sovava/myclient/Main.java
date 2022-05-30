package ykn.sovava.myclient;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Description:
 * TODO: (服务器不可以显示和发送群消息,不做了),最后完成界面优化,如果可能,增加聊天记录分界面显示
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

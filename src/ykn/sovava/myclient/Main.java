package ykn.sovava.myclient;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Description:
 * TODO: 完善群聊功能,比如创建多个群聊,最后完成界面优化,如果可能,增加聊天记录分界面显示
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

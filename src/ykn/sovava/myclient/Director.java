package ykn.sovava.myclient;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ykn.sovava.myclient.scene.ClientRun;
import ykn.sovava.myclient.scene.Login;

/**
 * Description:客户端导演类
 *
 * @author: ykn
 * @date: 2022年05月22日 18:15
 **/
public class Director {

    public static final double HEIGHT = 500, WIDTH = 700;

    private static Director instance = new Director();

    private Stage stage = null;

    private ClientRun client = null;

    private Director() {
    }

    public static Director getInstance() {
        return instance;
    }

    /**
     * Description: 布局和画布舞台的初始化
     *
     * @param stage:
     * @author: ykn
     * @date: 2022/5/21 13:56
     * @return: void
     */
    public void init(Stage stage) {
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Chat");
        stage.getIcons().add(new Image("/image/icon.png"));
        stage.setHeight(HEIGHT);
        stage.setWidth(WIDTH);
        stage.setResizable(false);
        this.stage = stage;
        toLogin();
        stage.show();
    }

    /**
     * Description: 启动初始界面
     *
     * @author: ykn
     * @date: 2022/5/21 13:57
     * @return: void
     */
    public void toLogin() {
        Login.load(stage);
    }

    /**
     * Description: 启动聊天界面
     *
     * @author: ykn
     * @date: 2022/5/21 13:58
     * @return: void
     */
    public void chatStart(String nickName) {
        client = new ClientRun(stage,nickName);
        Thread t = new Thread(client);
        t.start();
    }

}

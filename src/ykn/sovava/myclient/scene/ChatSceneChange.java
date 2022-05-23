package ykn.sovava.myclient.scene;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ykn.sovava.myclient.util.Header;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Description: TODO
 *
 * @author: ykn
 * @date: 2022年05月22日 18:01
 **/
public abstract class ChatSceneChange extends ChatScene {

    public Socket s;
    public PrintStream ps;
    public BufferedReader br;
    public Stage stage;
    public String nickName;
    public String msg;

    public ChatSceneChange(Stage stage) {
        super(stage);
        this.stage = stage;
        try {
            s = new Socket("127.0.0.1", 9999);
            ps = new PrintStream(s.getOutputStream());
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //显示朋友列表
        friendsButton.setOnAction(event -> {

        });
        //显示群列表
        groupButton.setOnAction(event -> {

        });
        //设置回车发送消息
        msgText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) { //判断是否按下回车
                event.consume();
                msg = msgText.getText();
                send(msg);
                if (msgText != null) msgText.clear();
            }
        });
        //设置发送按钮发送消息
        sendButton.setOnAction(event -> {
            msg = msgText.getText();
            send(msg);
            if (msgText != null) msgText.clear();
        });
        //关闭UI线程时同时关闭各子线程
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                ps.println(Header.I_LEAVE);
                System.exit(0);
            }
        });
    }

    protected void send(String msg) {
        if (msg != null && !msg.equals(""))
            ps.println(Header.UPLOAD_MSG + "|" + msg);
    }


}

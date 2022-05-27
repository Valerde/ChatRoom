package ykn.sovava.myclient.scene;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ykn.sovava.myclient.util.Header;
import ykn.sovava.myserver.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * Description:
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
    public StringBuilder f = null;

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


        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                ps.println(Header.I_LEAVE + "| | ");
                System.exit(0);
            }
        });
    }

    protected void sendMSG() {

        Set<String> friendToSend = new HashSet<>();
        clientListView.getSelectionModel().selectedItemProperty().addListener(ov -> {
            friendToSend.clear();
            friendToSend.addAll(clientListView.getSelectionModel().getSelectedItems());
        });
        //设置发送按钮发送消息
        sendButton.setOnAction(e -> {
            send(friendToSend);
        });
        //设置回车发送消息
        msgText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) { //判断是否按下回车
                event.consume();
                send(friendToSend);
            }
        });


    }

    private void send(Set<String> friendToSend) {
        f = new StringBuilder();
        for (String h : friendToSend) {
            assert false;
            f.append(h).append(",");
        }
        f.deleteCharAt(f.length() - 1);
        msg = msgText.getText();
        receivedMsgArea.appendText(nickName + ":" + msg + "\r\n");
        if (msg != null && !msg.equals("")) {
            assert false;
            ps.println(Header.UPLOAD_MSG + "|" + f.toString() + "|" + msg);
            f = null;
            if (msgText != null) msgText.clear();
            ps.flush();
        }
    }


}

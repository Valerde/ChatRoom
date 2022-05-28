package ykn.sovava.myclient.scene;

import javafx.application.Platform;
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
    public int friendsOrGroups = -1;//表示选择,-1表示缺省,0表示friends,1表示groups
    Set<String> friendToSend = new HashSet<>();

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
            friendsOrGroups = 0;

            Platform.runLater(() -> {
                friendToSend.clear();
                clientListView.setPrefHeight(100);
                groupListView.setPrefHeight(5);
                grouperListView.setPrefHeight(5);
            });

        });
        //显示群列表
        groupButton.setOnAction(event -> {
            friendsOrGroups = 1;
            Platform.runLater(() -> {
                friendToSend.clear();
                clientListView.setPrefHeight(5);
                groupListView.setPrefHeight(100);
                grouperListView.setPrefHeight(100);
            });
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

        clientListView.getSelectionModel().selectedItemProperty().addListener(ov -> {
            groupListView.getSelectionModel().clearSelection();
//            friendToSend.clear();
            if (friendsOrGroups == 0) {
                friendToSend.clear();
                friendToSend.addAll(clientListView.getSelectionModel().getSelectedItems());
            }
        });

        groupListView.getSelectionModel().selectedItemProperty().addListener(ov -> {
            clientListView.getSelectionModel().clearSelection();
            if (friendsOrGroups == 1) {
                friendToSend.clear();
                friendToSend.addAll(groupListView.getSelectionModel().getSelectedItems());
            }
        });


        //设置发送按钮发送消息
        sendButton.setOnAction(e -> {
            send();
        });
        //设置回车发送消息
        msgText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) { //判断是否按下回车
                event.consume();
                send();
            }
        });

    }

    private void send() {
        f = new StringBuilder();
        for (String h : friendToSend) {
            assert false;
            f.append(h).append(",");
        }
        System.out.println(friendsOrGroups);
        System.out.println(friendToSend);
        f.deleteCharAt(f.length() - 1);
        msg = msgText.getText();
        if (friendsOrGroups == 0) {
            receivedMsgArea.appendText("我:" + msg + "\r\n");
        } else if (friendsOrGroups == 1) {
            receivedMsgArea.appendText("我在"+group.get(0) + "中说:" + msg + "\r\n");
        }

        if (msg != null && !msg.equals("")) {
            assert false;
            ps.println(Header.UPLOAD_MSG + "|" + f.toString() + "|" + msg);
//            f = null;
            if (msgText != null) msgText.clear();
            //ps.flush();
        }
    }

    public void updateForDisConnect(String nickName) {
        Platform.runLater(() -> {
            clients.remove(nickName);
            receivedMsgArea.appendText(nickName + " out of connected.." + "\n");
        });
    }
}

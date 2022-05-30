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
 * Description:控件更改
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
                clientListView.setPrefHeight(100);
                groupListView.setPrefHeight(5);
                grouperListView.setPrefHeight(5);
            });

        });
        //显示群列表
        groupButton.setOnAction(event -> {
            friendsOrGroups = 1;
            Platform.runLater(() -> {
                clientListView.setPrefHeight(5);
                groupListView.setPrefHeight(100);
                grouperListView.setPrefHeight(100);
            });
        });
        //设置关闭
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                ps.println(Header.I_LEAVE + "| | ");
                System.exit(0);
            }
        });
    }

    /**
     * Description: 发送消息的事件监听
     *
     * @author: ykn
     * @date: 2022/5/28 16:21
     * @return: void
     */
    protected void sendMSG() {
        //选择朋友
        clientListView.getSelectionModel().selectedItemProperty().addListener(ov -> {
            //清除群组的选择
            Platform.runLater(() -> {
                groupListView.getSelectionModel().clearSelection();

                if (friendsOrGroups == 0) {
                    friendToSend.clear();
                    friendToSend.addAll(clientListView.getSelectionModel().getSelectedItems());
                    System.out.println(friendsOrGroups + "---" + friendToSend);
                }
            });

        });
        //选择群组
        groupListView.getSelectionModel().selectedItemProperty().addListener(ov -> {
            Platform.runLater(() -> {
                //清除朋友的选择
                clientListView.getSelectionModel().clearSelection();
                grouper.clear();
//                System.out.println("-"+groupListView.getSelectionModel().getSelectedItems().get(0));
                grouper.addAll(groupMap.get(groupListView.getSelectionModel().getSelectedItems().get(0)));
//                System.out.println("选择群组"+grouper);
                if (friendsOrGroups == 1) {
                    friendToSend.clear();
                    friendToSend.addAll(groupListView.getSelectionModel().getSelectedItems());
                    System.out.println(friendsOrGroups + "---" + friendToSend);
                }
            });

        });
        //设置发送按钮发送消息
        sendButton.setOnAction(e -> {
            Platform.runLater(this::send);
        });
        //设置回车发送消息
        msgText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) { //判断是否按下回车
                event.consume();
                Platform.runLater(this::send);
            }
        });

    }

    /**
     * Description: 正式发送消息
     *
     * @author: ykn
     * @date: 2022/5/28 16:21
     * @return: void
     */
    private void send() {
        f = new StringBuilder();
        for (String h : friendToSend) {
            assert false;
            f.append(h).append(",");
        }

        f.deleteCharAt(f.length() - 1);
        msg = msgText.getText();
        //本地显示说话
        if (friendsOrGroups == 0) {
            receivedMsgArea.appendText("我对" + f + ":" + msg + "\r\n");
        } else if (friendsOrGroups == 1) {
            receivedMsgArea.appendText("我在" + group.get(0) + "中说:" + msg + "\r\n");
        }
        //发送
        if (msg != null && !msg.equals("")) {
            assert false;
            ps.println(Header.UPLOAD_MSG + "|" + f.toString() + "|" + msg);
            if (msgText != null) msgText.clear();

        }
    }

    /**
     * Description: 好友离线
     * @param nickName: 离线好友的昵称
     * @author: ykn
     * @date: 2022/5/28 16:30
     * @return: void
     */
    public void updateForDisConnect(String nickName) {
        Platform.runLater(() -> {
            clients.remove(nickName);
            receivedMsgArea.appendText(nickName + " out of connected.." + "\n");
        });
    }
}

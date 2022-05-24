package ykn.sovava.myserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Server implements Runnable {

    //Server端监听的端口号
    public static final int PORT = 9999;
    //映射表 存放每个socket地址(IP:Port)及其对应的PrintWriter
    //为群发消息做准备
    Map<String, PrintWriter> map = new HashMap<>();//TODO :gd
    //存放已连接socket地址(IP:Port)，用于clientListView
    ObservableList<String> clients;
    ListView<String> clientListView;
    TextField ipText;
    TextField portText;
    TextArea sendMsgArea;
    Button sendButton;
    TextArea receivedMsgArea;

    public Server() {

    }

    public Server(TextField ipText, TextField portText, TextArea sendMsgArea, Button sendButton,
                  TextArea receivedMsgArea, ObservableList<String> clients, ListView<String> clientListView) {
        super();
        this.ipText = ipText;
        this.portText = portText;
        this.sendMsgArea = sendMsgArea;
        this.sendButton = sendButton;
        this.receivedMsgArea = receivedMsgArea;
        this.clients = clients;
        this.clientListView = clientListView;
    }

    /**
     * 更新UI界面的IP和Port
     */
    public void updateIpAndPort() {
        //用于在非UI线程更新UI界面
        Platform.runLater(() -> {
            ipText.setText("127.0.0.1");
            portText.setText(String.valueOf(PORT));
        });
    }

    @Override
    public void run() {
        updateIpAndPort();
        ServerSocket server;
        Socket socket;
        try {
            server = new ServerSocket(PORT);
            while (true) {
                socket = server.accept();
                //一个客户端接入就启动一个handler线程去处理
                new Thread(new Handler(map, socket, sendMsgArea,  sendButton, receivedMsgArea, clients, clientListView)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package ykn.sovava.myserver;

import java.io.*;
import java.net.Socket;
import java.util.*;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import ykn.sovava.myclient.util.Header;

import java.text.SimpleDateFormat;

/**
 * @className: handler
 * @description:
 * @author: ykn
 * @date: 2022/5/19
 **/
public class Handler implements Runnable {

    Socket socket;
    TextArea sendMsgArea;
    Button sendButton;
    TextArea receivedMsgArea;
    ObservableList<String> clients;
    ListView<String> clientListView;
    Map<String, Handler> map;
    BufferedReader br;
    public PrintStream ps;
    public String nickName;
    public msgHandle mh;

    public Handler() {
        super();
    }

    public Handler(Map<String, Handler> map, Socket socket, TextArea sendMsgArea, Button sendButton,
                   TextArea receivedMsgArea, ObservableList<String> clients, ListView<String> clientListView) {
        super();
        this.map = map;
        this.socket = socket;
        this.sendMsgArea = sendMsgArea;
        this.sendButton = sendButton;
        this.receivedMsgArea = receivedMsgArea;
        this.clients = clients;
        this.clientListView = clientListView;
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ps = new PrintStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接入客户端后，更新UI界面
     * 1.添加新接入客户端的地址信息
     * 2.receivedMsgarea打印成功连接信息
     * 3.statusText更新成功连接个数
     */
    public void updateForConnect(String nickName) {
        Platform.runLater(() -> {
            clients.add(nickName);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            receivedMsgArea.appendText(String.valueOf(clients.size()) + " Connected from " + nickName + " " + sdf.format(new Date()) + "\n");
            //statusText.setText(String.valueOf(clients.size()) + " Connect success.");
        });
    }

    /**
     * 断开客户端后，更新UI界面
     * 1.移除断开客户端的地址信息
     * 2.receivedMsgarea打印断开连接信息
     * 3.statusText更新成功连接个数
     * 4.移除map中对应的remoteSocketAddress
     */
    public void updateForDisConnect(String nickName) {
        Platform.runLater(() -> {
            clients.remove(nickName);
            //statusText.setText(String.valueOf(clients.size()) + " Connect success.");
            receivedMsgArea.appendText(nickName + " out of connected.." + "\n");
            map.remove(nickName);
        });
    }

    /**
     * 单发及群发消息
     * 1.为clientListView设置监听器
     * 1.1获取已选择的项(IP:Port)
     * 1.2从映射表中取出对应printWriter放入printWriters集合
     * 2.为sendButton设置鼠标点击事件
     * 2.1遍历printWriters集合
     * 2.2写入待发送的消息
     */
    public void sendMessage() {
        Set<Handler> handlers = new HashSet<>();
        clientListView.getSelectionModel().selectedItemProperty().addListener(ov -> {
            handlers.clear();
            for (String key : clientListView.getSelectionModel().getSelectedItems()) {
                handlers.add(map.get(key));
            }
        });
        sendButton.setOnAction(e -> {
            for (Handler h : handlers) {
                h.ps.println("Server" + "|" + sendMsgArea.getText() + "| " + "\r\n");
                //.write("127.0.0.1:9999" + "  " + sendMsgArea.getText() + "\r\n");
                ps.flush();
            }
        });
    }

    @Override
    public void run() {
//        String remoteSocketAddress = socket.getRemoteSocketAddress().toString().substring(1);

        try {

            //发消息
            sendMessage();
            //收消息
            String msg;
            while (true) {
                msg = br.readLine();
                System.out.println(msg);
                mh = new msgHandle(msg);
                handlerMSG();
            }
        } catch (IOException e) {
//            updateForDisConnect(remoteSocketAddress);
        }
    }

    public void handlerMSG() {
        switch (mh.getHeader()) {
            case Header.MY_LOGIN_NAME: {
                nickName = mh.getNickName();
//                System.out.println(nickName);
                updateForConnect(nickName);
                map.put(nickName, this);
                //通知已登录的客户端,说我登录了
                for (String nn : map.keySet()) {
                    if (!nn.equals(nickName)) {
                        map.get(nn).ps.println(Header.SOMEONE_LOGIN_NAME + "|" + nickName + "| ");
                    }
                }
                //通知新登录的客户端,说已经有这些老大哥登陆了
                StringBuilder sb = new StringBuilder();
                for (String nn : map.keySet()) {
                    if (!nn.equals(nickName)) {
                        sb.append(nn).append(":");
                    }
                }
                if (sb.length() > 0)
                    sb.deleteCharAt(sb.length() - 1);
                System.out.println(Header.LOG_IN_ED + "|" + sb + "| ");
                ps.println(Header.LOG_IN_ED + "|" + sb + "| ");
                break;
            }
            case Header.UPLOAD_MSG: {
                System.out.println(mh.getContext());
                receivedMsgArea.appendText(nickName + ":" + mh.getContext() + "\n");
                List<String> fl = mh.getGrouperName();
                for (String s : fl) {
                    map.get(s).ps.println(Header.ISSUED_MSG + "|" + nickName + "|" + mh.getContext());
                }
                break;
            }
            case Header.I_LEAVE: {

                for (String nn : map.keySet()) {
                    if (!nn.equals(nickName)) {
                        map.get(nn).ps.println(Header.SOMEONE_LEAVE + "|" + nickName + "| ");
                    }
                }
                updateForDisConnect(nickName);
            }

        }
    }

}
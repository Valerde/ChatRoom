package ykn.sovava.myserver;

import javafx.application.Platform;
import javafx.stage.Stage;
import ykn.sovava.myclient.util.Header;
//import ykn.sovava.myserver.util.msgHandle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 客户处理
 *
 * @author: ykn
 * @date: 2022年05月28日 16:01
 **/
public class Handler extends ServerUI implements Runnable {

    Socket socket;
    BufferedReader br;
    public PrintStream ps;
    public String nickName;
    public msgHandle mh;
    public Map<String, Handler> map;


    public Handler(Socket socket, Map<String, Handler> map) {
        super();

        this.socket = socket;
        this.map = map;
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ps = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        makeGroup();
        kickOutSomeone();
        //发消息
        sendMessage();
        //收消息
        String msg;
        while (true) {
            try {
                msg = br.readLine();
//                    System.out.println(msg);
                mh = new msgHandle(msg);
                handlerMSG();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void handlerMSG() {
        switch (mh.getHeader()) {
            case Header.MY_LOGIN_NAME: {
                nickName = mh.getNickName();
                updateForConnect(nickName);
                StringBuilder sb = new StringBuilder();
                for (String nn : map.keySet()) {
                    map.get(nn).ps.println(Header.SOMEONE_LOGIN_NAME + "|" + nickName + "| ");
                    sb.append(nn).append(",");
                }

                map.put(nickName, this);
                if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);

                this.ps.println(Header.LOG_IN_ED + "|" + sb + "| ");
                break;
            }
            case Header.UPLOAD_MSG: {
                System.out.println(mh.getContext());
                receivedMsgArea.appendText(nickName + ":" + mh.getContext() + "\n");
                List<String> fl = mh.getGrouperName();
                if (groupMap.get(fl.get(0))!=null ) {
                    for (String s : groupMap.get(fl.get(0))) {
                        if (!s.equals(nickName))
                            map.get(s).ps.println(Header.ISSUED_MSG + "|" + fl.get(0) + "中" + nickName + "|" + mh.getContext());
                    }
                } else {
                    for (String s : fl) {
                        map.get(s).ps.println(Header.ISSUED_MSG + "|" + nickName + "|" + mh.getContext());
                    }
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
                break;
            }
        }
    }

    /**
     * 接入客户端后，更新UI界面
     */
    public void updateForConnect(String nickName) {
        Platform.runLater(() -> {
            clients.add(nickName);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            receivedMsgArea.appendText(String.valueOf(clients.size()) + " Connected from " + nickName + " " + sdf.format(new Date()) + "\n");
        });
    }

    /**
     * 断开客户端后，更新UI界面
     */
    public void updateForDisConnect(String nickName) {
        Platform.runLater(() -> {
            clients.remove(nickName);
            map.remove(nickName);
            receivedMsgArea.appendText(nickName + " out of connected.." + "\n");
        });
    }

    /**
     * 单发及群发消息
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
                h.ps.println(Header.ISSUED_MSG + "|" + "Server" + "|" + sendMsgArea.getText());
                h.ps.flush();
            }
            sendMsgArea.clear();
        });
    }

    /**
     * Description: 踢出某人
     */
    protected void kickOutSomeone() {

        Set<String> kickClients = new HashSet<>();
        clientListView.getSelectionModel().selectedItemProperty().addListener(ov -> {
            kickClients.clear();
            kickClients.addAll(clientListView.getSelectionModel().getSelectedItems());
        });

        kickOutButton.setOnAction(e -> {
            for (String c : kickClients) {
                map.get(c).ps.println(Header.KICK_OUT + "| | ");
            }
        });
    }

    /**
     * Description: 建群
     */
    public void makeGroup() {
        Set<String> grouper = new HashSet<>();
        clientListView.getSelectionModel().selectedItemProperty().addListener(ov -> {
            grouper.clear();
            grouper.addAll(clientListView.getSelectionModel().getSelectedItems());
        });
        groupButton.setOnAction(e -> {

            String groupName = "新建群聊" + Integer.toString((int) (Math.random() * 100));
            groups.add(groupName);
            groupers.addAll(grouper);

            groupMap.put(groupName, new ArrayList<>(grouper));
            System.out.println(groupMap);
            StringBuilder f = new StringBuilder();
            for (String h : grouper) {
                assert false;
                f.append(h).append(",");
            }
            f.deleteCharAt(f.length() - 1);
            for (String c : grouper) {
                map.get(c).ps.println(Header.YOUR_GROUP + "|" + f + "|" + groupName);
            }
            grouper.clear();
        });

    }

}//内部类完

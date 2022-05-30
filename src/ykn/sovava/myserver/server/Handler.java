package ykn.sovava.myserver.server;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import ykn.sovava.myclient.util.Header;
import ykn.sovava.myserver.util.msgHandle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Description: 客户处理
 *
 * @author: ykn
 * @date: 2022年05月28日 16:01
 **/
public class Handler extends ServerUI implements Runnable {

    public Socket socket;
    public BufferedReader br;
    public PrintStream ps;
    public String nickName;
    public msgHandle mh;
    public Map<String, Handler> map;
    public Set<String> friendToSend = new HashSet<>();
    public int friendsOrGroups = -1;//表示选择,-1表示缺省,0表示friends,1表示groups
    public String gN;

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
                mh = new msgHandle(msg);
                handlerMSG();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

        }
    }

    /**
     * Description: 消息处理
     * @author: ykn
     * @date: 2022/5/30 20:51
     * @return: void
     */
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
                receivedMsgArea.appendText(nickName + "对" + mh.getGrouperName() + "说:" + mh.getContext() + "\n");
                List<String> fl = mh.getGrouperName();
                if (groupMap.get(fl.get(0)) != null) {
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

        clientListView.getSelectionModel().selectedItemProperty().addListener(ov -> {
            friendsOrGroups = 0;
            //清除群组的选择
            Platform.runLater(() -> {
                groupListView.getSelectionModel().clearSelection();

                if (friendsOrGroups == 0) {
                    friendToSend.clear();
                    friendToSend.addAll(clientListView.getSelectionModel().getSelectedItems());
                }
            });

        });
        //选择群组
        groupListView.getSelectionModel().selectedItemProperty().addListener(ov -> {
            friendsOrGroups = 1;
            Platform.runLater(() -> {
                //清除朋友的选择
                clientListView.getSelectionModel().clearSelection();
                groupers.clear();

                gN = groupListView.getSelectionModel().getSelectedItems().get(0);
                groupers.addAll(groupMap.get(gN));

                if (friendsOrGroups == 1) {
                    friendToSend.clear();
                    friendToSend.addAll(groupListView.getSelectionModel().getSelectedItems());
                }
            });

        });
        //设置发送按钮发送消息
        sendButton.setOnAction(e -> {
            Platform.runLater(this::send);
        });
        //设置回车发送消息
        sendMsgArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) { //判断是否按下回车
                event.consume();
                Platform.runLater(this::send);
            }
        });
    }

    /**
     * Description: 正式发送消息
     * @author: ykn
     * @date: 2022/5/30 20:46
     * @return: void
     */
    private void send() {
        String msg = sendMsgArea.getText();
        if (friendsOrGroups == 0) {
            for (String s : friendToSend) {
                receivedMsgArea.appendText("我对" + s + "说:" + msg + "\r\n");
                map.get(s).ps.println(Header.ISSUED_MSG + "|" + "Server" + "|" + msg);
            }
        } else if (friendsOrGroups == 1) {
            receivedMsgArea.appendText("我在" + gN + "中说:" + msg + "\r\n");
            ArrayList<String> list = groupMap.get(gN);
            for (String s : list) {
                map.get(s).ps.println(Header.ISSUED_MSG + "|" + "Server" + "|" + msg);
            }
        }
        //发送
        if (msg != null && !msg.equals("")) {
            if (sendMsgArea != null) sendMsgArea.clear();
        }
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
            friendsOrGroups = 0;
            Platform.runLater(() -> {
                grouper.clear();
                grouper.addAll(clientListView.getSelectionModel().getSelectedItems());
            });

        });

        groupButton.setOnAction(e -> {
            Platform.runLater(() -> {
                String groupName = "新建群聊" + Integer.toString((int) (Math.random() * 100));
                groupers.clear();
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

        });

    }

}

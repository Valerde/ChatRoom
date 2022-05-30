package ykn.sovava.myclient.scene;

import javafx.application.Platform;
import javafx.stage.Stage;
import ykn.sovava.myclient.util.Header;
import ykn.sovava.myclient.util.msgHandle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:客户端主线程
 *
 * @author: ykn
 * @date: 2022年05月22日 18:08
 **/
public class ClientRun extends ChatSceneChange implements Runnable {
    public msgHandle mh;

    public ClientRun(Stage stage, String nickName) {
        super(stage);
        this.nickName = nickName;
        nameText.setText(nickName);
        ps.println(Header.MY_LOGIN_NAME + "|" + nickName + "| ");
    }

    @Override
    public void run() {
        while (true) {
            try {

                sendMSG();

                String getMSG = br.readLine();
                System.out.println(getMSG);
                mh = new msgHandle(getMSG);
                msgHandle();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Description: 客户端消息处理
     *
     * @author: ykn
     * @date: 2022/5/28 10:48
     * @return: void
     */
    private void msgHandle() {
        switch (mh.getHeader()) {
            case Header.ISSUED_MSG: {
                Platform.runLater(() -> {
                    receivedMsgArea.appendText(mh.getNickName() + ":" + mh.getContext() + "\r\n");
//                    String s = "a\r\nb\r\nc\r\n";
//                    receivedMsgArea.appendText("test1:"+"\r\n"+"test2"+s);
                });
                break;
            }
            case Header.KICK_OUT: {
                ps.println(Header.I_LEAVE + "| | ");
                System.exit(0);
                break;
            }
            case Header.SOMEONE_LOGIN_NAME: {
                Platform.runLater(() -> {
                    clients.add(mh.getNickName());
                });
                break;
            }
            case Header.YOUR_GROUP: {
                Platform.runLater(() -> {
                    grouper.clear();
                    group.add(mh.getGroupName());
//                    System.out.println("group.add(mh.getGroupName());"+group);
                    grouper.addAll(mh.getGrouperName());
//                    System.out.println("mh.getGrouperName()"+mh.getGrouperName());
//                    System.out.println("grouper.addAll(mh.getGrouperName());"+grouper);
                    ArrayList<String> temp = new ArrayList<>(grouper);
                    groupMap.put(mh.getGroupName(), temp);
//                    System.out.println(groupMap);
                });
                break;
            }
            case Header.SOMEONE_LEAVE: {
                updateForDisConnect(mh.getNickName());
                break;//傻逼就不写break;
            }
            case Header.LOG_IN_ED: {
                List<String> fl = mh.getGrouperName();
                Platform.runLater(() -> {
                    if (fl.get(0).equals("")) return;
                    clients.addAll(fl);
                });
                break;
            }
        }
    }


}

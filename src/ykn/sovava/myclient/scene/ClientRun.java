package ykn.sovava.myclient.scene;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import ykn.sovava.myclient.util.Header;
import ykn.sovava.myclient.util.msgHandle;

import java.io.IOException;
import java.util.List;

/**
 * Description:
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
                mh = new msgHandle(getMSG);
                msgHandle();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void msgHandle() {
        switch (mh.getHeader()) {
            case Header.ISSUED_MSG: {
                Platform.runLater(() -> {
                    receivedMsgArea.appendText(mh.getNickName() + ":" + mh.getContext() + "\r\n");
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
                    group.add(mh.getGroupName());
                    grouper = (ObservableList<String>) mh.getGrouperName();
                });
                break;
            }
            case Header.SOMEONE_LEAVE: {
                updateForDisConnect(mh.getNickName());
            }
            case Header.LOG_IN_ED: {
                List<String> fl = mh.getGrouperName();
                Platform.runLater(() -> {
                    if (!fl.get(0).equals(""))
//                        System.out.println("-" +  + "-");
                    clients.addAll(fl);
                });
            }
        }
    }

    public void updateForDisConnect(String nickName) {
        Platform.runLater(() -> {
            clients.remove(nickName);
            receivedMsgArea.appendText(nickName + " out of connected.." + "\n");
        });
    }
}

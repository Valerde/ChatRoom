package ykn.sovava.myclient.scene;

import javafx.collections.ObservableList;
import javafx.stage.Stage;
import ykn.sovava.myclient.util.Header;
import ykn.sovava.myclient.util.msgHandle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


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

        ps.println(Header.MY_LOGIN_NAME + "|" + nickName);
//
    }

    @Override
    public void run() {
        while (true) {
            try {
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
                msgText.appendText(mh.context() + "\r\n");
                break;
            }
            case Header.KICK_OUT: {
                ps.println(Header.I_LEAVE);
                System.exit(0);
                break;
            }
            case Header.SOMEONE_LOGIN_NAME: {
                clients.add(mh.context());
                break;
            }
            case Header.YOUR_GROUP: {
                group.add(mh.context());
                grouper = (ObservableList<String>) mh.grouper();
                break;
            }
        }
    }
}

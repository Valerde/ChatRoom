package ykn.sovava.myclient.scene;

import javafx.stage.Stage;
import ykn.sovava.myclient.util.Header;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


/**
 * Description: TODO
 *
 * @author: ykn
 * @date: 2022年05月22日 18:08
 **/
public class ClientRun extends ChatSceneChange implements Runnable {


    public ClientRun(Stage stage, String nickName) {
        super(stage);
        this.nickName = nickName;
        nameText.setText(nickName);
        try {
            s = new Socket("127.0.0.1", 9999);
            ps = new PrintStream(s.getOutputStream());
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ps.println(Header.MY_LOGIN_NAME + ":" + nickName);

    }

    @Override
    public void run() {
        while (true){
            try {
                String getMSG = br.readLine();
                msgHandle(getMSG);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void msgHandle(String getMSG) {
        switch (getMSG.split("|")[0]){
            case Header.ISSUED_MSG:{
                break;
            }
            case Header.KICK_OUT:{
                break;
            }
            case Header.SOMEONE_LOGIN_NAME:{
                break;
            }
            case Header.YOUR_GROUP:{
                break;
            }
        }
    }
}

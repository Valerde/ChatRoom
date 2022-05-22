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
    public String nickName;
    public Socket s;
    public PrintStream ps;
    public BufferedReader br;

    public ClientRun(Stage stage, String nickName) {
        super(stage);
        this.nickName = nickName;
        try {
            s = new Socket("127.0.0.1", 9999);
            ps = new PrintStream(s.getOutputStream());
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ps.println(Header.MY_LOGIN_NAME+":"+nickName);

    }

    @Override
    public void run() {

    }
}

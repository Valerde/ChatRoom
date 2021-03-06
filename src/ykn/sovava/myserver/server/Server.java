package ykn.sovava.myserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import javafx.stage.Stage;

/**
 * Description: 服务器主线程
 * @author: ykn
 * @date: 2022/5/30 20:52
 */
public class Server extends ServerUI implements Runnable {

    public Map<String, Handler> map = new HashMap<>();
    public Stage stage;

    public Server(Stage stage) {
        super(stage);
        this.stage = stage;
        new Thread(this).start();
    }

    @Override
    public void run() {

        try {
            ServerSocket server = new ServerSocket(9999);
            while (true) {
                Socket socket = server.accept();
                new Thread(new Handler(socket,map)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

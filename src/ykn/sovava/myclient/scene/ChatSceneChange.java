package ykn.sovava.myclient.scene;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ykn.sovava.myclient.scene.ChatScene;

import java.io.BufferedReader;
import java.io.PrintStream;

/**
 * Description: TODO
 *
 * @author: ykn
 * @date: 2022年05月22日 18:01
 **/
public abstract class ChatSceneChange extends ChatScene {

    public Stage stage;


    public ChatSceneChange(Stage stage) {
        super(stage);
        this.stage = stage;

    }




}

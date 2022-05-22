package ykn.sovava.myclient.scene;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Description: TODO
 *
 * @author: ykn
 * @date: 2022年05月22日 17:54
 **/
public abstract class ChatScene {
    public TextArea receivedMsgArea;
    public TextField ipText;
    public TextField portText;
    public TextArea sendMsgArea;
    public TextField statusText;
    public Button sendButton;
    public Stage stage;
    public GridPane rightPane;

    public ChatScene(Stage stage) {
        this.stage = stage;


        //右边 Received Message
        rightPane = new GridPane();
        rightPane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        rightPane.setHgap(5.5);
        rightPane.setVgap(5.5);
        rightPane.add(new Label("Received Message:"), 0, 0);
        receivedMsgArea = new TextArea();
        receivedMsgArea.setWrapText(true);
        receivedMsgArea.setEditable(false);
        receivedMsgArea.setMaxWidth(350);
        receivedMsgArea.setPrefHeight(410);
        rightPane.add(receivedMsgArea, 0, 1);

        //左边 IPAddress+Port
        GridPane leftPane1 = new GridPane();
        leftPane1.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        leftPane1.setHgap(5.5);
        leftPane1.setVgap(5.5);
        leftPane1.add(new Label("IPAdress:"), 0, 0);
        ipText = new TextField();
        ipText.setEditable(false);
        leftPane1.add(ipText, 1, 0);
        leftPane1.add(new Label("Port:"), 0, 1);
        portText = new TextField();
        portText.setEditable(false);
        leftPane1.add(portText, 1, 1);

        //左边 Send Message
        GridPane leftPane2 = new GridPane();
        leftPane2.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        leftPane2.setHgap(5.5);
        leftPane2.setVgap(5.5);
        leftPane2.add(new Label("Send Message:"), 0, 2);
        sendMsgArea = new TextArea();
        sendMsgArea.setPrefHeight(250);
        sendMsgArea.setMaxWidth(275);
        sendMsgArea.setWrapText(true);
        leftPane2.add(sendMsgArea, 0, 3);

        //左边 Connect Status + button
        GridPane leftPane3 = new GridPane();
        leftPane3.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        leftPane3.setHgap(5.5);
        leftPane3.setVgap(5.5);
        statusText = new TextField();
        statusText.setEditable(false);
        leftPane3.add(statusText, 0, 0);
        sendButton = new Button("Send");
        leftPane3.add(sendButton, 1, 0);


        //组合
        VBox vBox = new VBox();
        vBox.getChildren().addAll(leftPane1, leftPane2, leftPane3);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(vBox, rightPane);

        Scene scene = new Scene(hBox);
        stage.setTitle("client");
        stage.setScene(scene);
        //关闭UI线程时同时关闭各子线程
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        stage.show();
    }
}

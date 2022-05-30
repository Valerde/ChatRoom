package ykn.sovava.myserver;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className: ServerUI
 * @description:
 * @author: ykn
 * @date: 2022/5/19
 **/
public abstract class ServerUI {
    public static TextArea receivedMsgArea = new TextArea();
    public static TextField ipText = new TextField();
    public static TextField portText = new TextField();
    public static TextArea sendMsgArea = new TextArea();
    public static Button sendButton = new Button(" Send ");
    public static Button groupButton = new Button(" make ");
    public static Button kickOutButton = new Button("kick");
    public static ObservableList<String> clients = FXCollections.observableArrayList();
    public static ListView<String> clientListView = new ListView<>(clients);
    public static ObservableList<String> groups = FXCollections.observableArrayList();
    public static ListView<String> groupListView = new ListView<>(groups);
    public static ObservableList<String> groupers = FXCollections.observableArrayList();
    public static ListView<String> grouperListView = new ListView<>(groupers);
    //public static Map<String, List<String>> groupMap = new HashMap<>();
    public static Stage stage;
    public static Map<String, ArrayList<String>> groupMap = new HashMap<>();

    public ServerUI() {
    }

    public ServerUI(Stage stage) {
        ServerUI.stage = stage;
        initUI();
    }

    public void initUI(){
        //右边 Received Message
        GridPane rightPane = new GridPane();
        rightPane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        rightPane.setHgap(5.5);
        rightPane.setVgap(5.5);
        rightPane.add(new Label("Received Message:"), 0, 0);
        receivedMsgArea.setWrapText(true);
        receivedMsgArea.setEditable(false);
        receivedMsgArea.setMaxWidth(350);
        receivedMsgArea.setPrefHeight(410);
        rightPane.add(receivedMsgArea, 0, 1);

        //左边 IPAdress+Port
        GridPane leftPane1 = new GridPane();
        leftPane1.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        leftPane1.setHgap(5.5);
        leftPane1.setVgap(5.5);
        leftPane1.add(new Label("IPAddress:"), 0, 0);
        ipText.setText("127.0.0.1");
        ipText.setEditable(false);
        leftPane1.add(ipText, 1, 0);
        leftPane1.add(new Label("Port:"), 0, 1);
        portText.setText(String.valueOf(9999));
        portText.setEditable(false);
        leftPane1.add(portText, 1, 1);

        //左边 Choose Client
        GridPane leftPane2 = new GridPane();
        leftPane2.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        leftPane2.setHgap(5.5);
        leftPane2.setVgap(5.5);
        leftPane2.add(new Label("Choose Client:"), 0, 0);

        clientListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        clientListView.setMaxHeight(100);
        clientListView.setMaxWidth(275);
        leftPane2.add(clientListView, 0, 1, 2, 1);
        groupButton = new Button("make");
        leftPane2.add(groupButton, 1, 0);
        leftPane2.add(kickOutButton,2,0);
        groupListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        groupListView.setMaxHeight(100);
        groupListView.setMaxWidth(120);
        leftPane2.add(groupListView, 0, 3);
        grouperListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        grouperListView.setMaxHeight(100);
        grouperListView.setMaxWidth(120);
        leftPane2.add(grouperListView, 1, 3);
        //左边 Send Message

        leftPane2.add(new Label("group:"), 0, 2);
        sendMsgArea.setMaxHeight(50);
        sendMsgArea.setMaxWidth(240);
        sendMsgArea.setWrapText(true);
        sendMsgArea.setPromptText("和好友愉快的聊天吧");
        leftPane2.add(sendMsgArea, 0, 4, 2, 1);
        leftPane2.add(sendButton, 2, 4);

        //组合
        VBox vBox = new VBox();
        vBox.getChildren().addAll(leftPane1, leftPane2);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(vBox, rightPane);

        Scene scene = new Scene(hBox);
        stage.setTitle("server");
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

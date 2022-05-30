package ykn.sovava.myclient.scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:聊天界面初始化
 * @author: ykn
 * @date: 2022年05月22日 17:54
 **/
public class ChatScene {
    public TextArea receivedMsgArea;
    public TextField nameText;
    public Button friendsButton;
    public Button groupButton;
    public TextArea msgText;
    public Button sendButton;
    public Stage stage;
    public GridPane rightPane;
    public ObservableList<String> clients = FXCollections.observableArrayList();
    public ListView<String> clientListView = new ListView<>(clients);
    public static ObservableList<String> group = FXCollections.observableArrayList();
    public ListView<String> groupListView = new ListView<>(group);
    public static ObservableList<String> grouper = FXCollections.observableArrayList();
    public ListView<String> grouperListView = new ListView<>(grouper);
    public static Map<String, ArrayList<String>> groupMap = new HashMap<>();

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
        leftPane1.add(new Label("My Name:"), 0, 0);
        nameText = new TextField();
        nameText.setMaxWidth(150);
        nameText.setEditable(false);
        leftPane1.add(nameText, 1, 0);

        //左边 Send Message
        GridPane leftPane2 = new GridPane();
        leftPane2.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        leftPane2.setHgap(5.5);
        leftPane2.setVgap(5.5);

        friendsButton = new Button("Friends");
        leftPane2.add(friendsButton, 0, 1);
        clientListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        clientListView.setPrefHeight(100);
        clientListView.setMaxWidth(275);
        leftPane2.add(clientListView, 0, 2,2,1);

        groupButton = new Button("Groups");
        leftPane2.add(groupButton, 0, 3);
        groupListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        groupListView.setPrefHeight(100);
        groupListView.setMaxWidth(120);

        leftPane2.add(groupListView, 0, 4);
        grouperListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        grouperListView.setPrefHeight(5);
        grouperListView.setMaxWidth(120);
        leftPane2.add(grouperListView, 1, 4);


        //左边 Connect Status + button
        GridPane leftPane3 = new GridPane();
        leftPane3.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        leftPane3.setHgap(5.5);
        leftPane3.setVgap(5.5);
        msgText = new TextArea();
        msgText.setEditable(true);
        msgText.setPrefWidth(185);
        msgText.setMaxHeight(50);
        msgText.setWrapText(true);
        msgText.setPromptText("和好友愉快的聊天吧");
        leftPane3.add(msgText, 0, 0,2,1);
        sendButton = new Button("Send");
        leftPane3.add(sendButton, 2, 0);

        //组合
        VBox vBox = new VBox();
        vBox.getChildren().addAll(leftPane1, leftPane2, leftPane3);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(vBox, rightPane);

        Scene scene = new Scene(hBox);
        stage.setTitle("client");
        stage.setScene(scene);

        stage.show();
    }
}

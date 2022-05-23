package ykn.sovava.myclient.scene;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    public TextField nameText;
    public Button friendsButton;
    public Button groupButton;
    public TextField msgText;
    public Button sendButton;
    public Stage stage;
    public GridPane rightPane;
    public ListView<String> clientListView = new ListView<>();
    public ListView<String> groupListView = new ListView<>();
    public ListView<String> grouperListView = new ListView<>();
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
        //nameText.setText();
        nameText.setEditable(false);
        leftPane1.add(nameText, 1, 0);
//        ipText = new TextField();
//        ipText.setEditable(false);
//        leftPane1.add(ipText, 1, 0);
//        leftPane1.add(new Label("Port:"), 0, 1);
//        portText = new TextField();
//        portText.setEditable(false);
//        leftPane1.add(portText, 1, 1);

        //左边 Send Message
        GridPane leftPane2 = new GridPane();
        leftPane2.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        leftPane2.setHgap(5.5);
        leftPane2.setVgap(5.5);
        friendsButton = new Button("Friends");
        //friendsButton.setMaxWidth(50);
        leftPane2.add(friendsButton, 0, 1);
        //leftPane2.add(new Label("Friends:"), 0, 1);
        clientListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        clientListView.setMaxHeight(100);
        clientListView.setMaxWidth(275);
        leftPane2.add(clientListView, 0, 2,2,1);
        groupButton = new Button("Groups");
        leftPane2.add(groupButton, 0, 3);
        groupListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        groupListView.setMaxHeight(100);
        groupListView.setMaxWidth(120);
        leftPane2.add(groupListView, 0, 4);
        grouperListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        grouperListView.setMaxHeight(100);
        grouperListView.setMaxWidth(120);
        leftPane2.add(grouperListView, 1, 4);


        //左边 Connect Status + button
        GridPane leftPane3 = new GridPane();
        leftPane3.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        leftPane3.setHgap(5.5);
        leftPane3.setVgap(5.5);
        msgText = new TextField();
        msgText.setEditable(true);
        msgText.setPrefWidth(185);
        leftPane3.add(msgText, 0, 0);
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

        stage.show();
    }
}

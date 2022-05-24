package ykn.sovava.myserver;

/**
 * @className: ServerUI
 * @description:
 * @author: ykn
 * @date: 2022/5/19
 **/


import javafx.application.Application;
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


/**
 * @className: ServerUI
 * @description:
 * @author: ykn
 * @date: 2022/5/19
 **/
public class ServerUI extends Application {
    TextArea receivedMsgArea = new TextArea();
    TextField ipText = new TextField();
    TextField portText = new TextField();
    TextArea sendMsgArea = new TextArea();
//    TextField statusText = new TextField();
    Button sendButton = new Button(" Send ");
    Button groupButton = new Button(" make ");
    ObservableList<String> clients = FXCollections.observableArrayList();
    //    ListView<String> clientListView = new ListView<>(clients);
    public ListView<String> clientListView = new ListView<>();
    public ListView<String> groupListView = new ListView<>();
    public ListView<String> grouperListView = new ListView<>();

    public void start(Stage primaryStage) throws Exception {

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
        ipText.setEditable(false);
        leftPane1.add(ipText, 1, 0);
        leftPane1.add(new Label("Port:"), 0, 1);
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
        primaryStage.setTitle("server");
        primaryStage.setScene(scene);
        //关闭UI线程时同时关闭各子线程
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        primaryStage.show();

//        statusText.setText("0 Connect success.");
        //启动server线程
        new Thread(new Server(ipText, portText, sendMsgArea,  sendButton, receivedMsgArea, clients, clientListView)).start();
    }

}

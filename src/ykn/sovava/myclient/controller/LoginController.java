package ykn.sovava.myclient.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ykn.sovava.myclient.Director;

public class LoginController {

    @FXML
    private TextField nickNameText;

    @FXML
    private Button loginButton;


    @FXML
    void clickToLogin(ActionEvent event) {
        String nickName = nickNameText.getText();
        Director.getInstance().chatStart(nickName);
    }
}


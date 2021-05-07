package ru.runov.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.nio.channels.SocketChannel;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private NetworkProider provider;
    @FXML
    TextField TextFld;
    @FXML
    TextArea TextCommandLogFromSerer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        provider = new NetworkProider((args -> {
            TextCommandLogFromSerer.appendText((String)args[0]);
        }));
    }

    public void sendMsgAction(ActionEvent actionEvent) {
        provider.sendCommand(TextFld.getText());
    }
}

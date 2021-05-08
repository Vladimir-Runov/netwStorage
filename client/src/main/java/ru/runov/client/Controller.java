package ru.runov.client;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    private NetworkProider provider;
    @FXML
    TextField TextFld;
    @FXML
    TextArea TextCommandLogFromSerer;
    @FXML
    TableView TableServ;
    @FXML
    TableView<FileInfoClient> panelLocal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn<FileInfoClient, String> fcolType = new TableColumn<>();
        fcolType.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fcolType.setPrefWidth(10);

        TableColumn<FileInfoClient,String> fcolName = new TableColumn<>("Name");
        fcolName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileName()));
        fcolName.setPrefWidth(200);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TableColumn<FileInfoClient,String> fcolDate = new TableColumn<>("Date");
        fcolDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModifiedTime().format(dtf)));
        fcolDate.setPrefWidth(100);

        TableColumn<FileInfoClient,Long> fcolSize = new TableColumn<>("Size");
        fcolSize.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getSize()));
        fcolSize.setPrefWidth(80);
        fcolSize.setCellFactory(column -> {
            return new TableCell<FileInfoClient,Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(""); setStyle("");
                    } else {
                        String tx = String.format("%,d bt.",item);
                        if (item == -1)
                            tx = "[DIR]";
                        setText(tx);
                    }
                }
            };
        });

        panelLocal.getColumns().addAll(fcolType,fcolName,fcolSize,fcolDate);

        panelLocal.getSortOrder().add(fcolType);
        updateLocalFileList(Paths.get("."));

        provider = new NetworkProider((args -> {
            TextCommandLogFromSerer.appendText((String)args[0]);
        }));
    }

    public void updateLocalFileList(Path initPath) {
        panelLocal.getItems().clear();
        try {
            panelLocal.getItems().addAll(Files.list(initPath).map(FileInfoClient::new).collect(Collectors.toList()));
            panelLocal.sort();
        } catch (IOException e) {
            //Alert Alert(e.printStackTrace();
        }


    }
    public void sendMsgAction(ActionEvent actionEvent) {
        provider.sendCommand(TextFld.getText());
    }

    public void send2Serv(ActionEvent actionEvent) {
    }

    public void downloadFromSrv(ActionEvent actionEvent) {
    }

    public void cmdDeleteFile(ActionEvent actionEvent) {
    }

    public void btmMenuExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void btnOnActionTestSerer(ActionEvent actionEvent) {
     //   test server
    }
}

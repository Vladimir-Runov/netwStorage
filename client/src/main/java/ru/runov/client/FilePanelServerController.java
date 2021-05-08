package ru.runov.client;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FilePanelServerController implements Initializable {

    private NetworkProider provider;
    @FXML
    TextArea TextCommandLogFromSerer;  // log (debug) inf from serer
    @FXML
    TableView<FileInfoClient> fileTableServ;

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

        fileTableServ.getColumns().addAll(fcolType,fcolName,fcolSize,fcolDate);

        fileTableServ.getSortOrder().add(fcolType);
        updateLocalFileList(Paths.get("."));

        provider = new NetworkProider((args -> {
            TextCommandLogFromSerer.appendText((String)args[0]);
        }));
    }

    public void updateLocalFileList(Path initPath) {
        fileTableServ.getItems().clear();
        try {
            fileTableServ.getItems().addAll(Files.list(initPath).map(FileInfoClient::new).collect(Collectors.toList()));
            fileTableServ.sort();
        } catch (IOException e) {
            //Alert Alert(e.printStackTrace();
        }
    }

    public void btnOnActionSerRefresh(ActionEvent actionEvent) {
    }
}

package ru.runov.client;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.nio.channels.SocketChannel;
import java.nio.file.*;
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
    @FXML   //  Co
    ComboBox<String> diskSelectInput;
    @FXML
    TextField pathCurrent;

    @FXML
    VBox panelServer;

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

        panelLocal.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    Path p = Paths.get(pathCurrent.getText()).resolve(panelLocal.getSelectionModel().getSelectedItem().getFileName());
                    if(Files.isDirectory(p))
                        updateLocalFileList(p);
                }
            }
        });
        // Combo box Disks A B S D
        diskSelectInput.getItems().clear();
        for (Path p: FileSystems.getDefault().getRootDirectories()) {
            diskSelectInput.getItems().add(p.toString());
        }
        diskSelectInput.getSelectionModel().select(0);
        diskSelectInput.setPrefWidth(30);
  //      exitBtn.setOnAction(e -> Platform.exit());

        provider = new NetworkProider((args -> {
            TextCommandLogFromSerer.appendText((String)args[0]);
        }));
    }

    public void updateLocalFileList(Path initPath) {
        pathCurrent.setText(initPath.normalize().toAbsolutePath().toString());
        panelLocal.getItems().clear();
        try {
            panelLocal.getItems().addAll(Files.list(initPath).map(FileInfoClient::new).collect(Collectors.toList()));
            panelLocal.sort();

        } catch (IOException e) {
            //Alert Alert(e.printStackTrace();
        }


    }

//    public void sendMsgAction(ActionEvent actionEvent) {
//        provider.sendCommand(TextFld.getText());
//    }

    public void upload2Serv(ActionEvent actionEvent) {
        // upl curr file
        FilePanelServerController ctrlServ = (FilePanelServerController)panelServer.getProperties().get("ctrl");
        //Controller ctrlLocal = (Controller)panelLocal.getProperties().get("ctrl");
        if (this.getCurrentPathLocal() == "")  {
            Alert alrt = new Alert(Alert.AlertType.INFORMATION, "No file selected !", ButtonType.APPLY);
            alrt.showAndWait();
            return;
        }
        System.out.println(this.getCurrentPathLocal());
        Path srcPath = Paths.get(this.pathCurrent.getText(), this.getCurrentSelectedFileLocal());

//        System.out.println(srcPath);

        provider.uploadFile(srcPath);

        //ctrlServ.sendRequest

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

    public void btnOnActionPathUp(ActionEvent actionEvent) {
        // button UP path in local
        Path p = Paths.get(pathCurrent.getText()).getParent();
        if (p != null)
            updateLocalFileList(p);

    }

    public void btnOnActionSelectDisk(ActionEvent actionEvent) {
        ComboBox<String > el = (ComboBox<String >)actionEvent.getSource();
        updateLocalFileList(Paths.get(el.getSelectionModel().getSelectedItem()));
    }

    public String getCurrentSelectedFileLocal() {
       // if(panelLocal.isFocused())
            return panelLocal.getSelectionModel().getSelectedItem().getFileName();
        //return "";
    }

    public String getCurrentPathLocal() {
        return pathCurrent.getText();
    }


}

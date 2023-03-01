package dev.claybradley.industrialautomationtoolkit.modbus;

import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
@Component
@Scope("prototype")
public class ModbusMainController implements Initializable {
    @FXML
    private VBox RootVBox;
    @FXML
    private HBox BodyHBox;
    @Autowired
    ModbusMainModel modbusMainModel;
    @FXML
    private TreeView treeView;
    @FXML
    private TextField portNumberTextField;
    TreeItem<String> rootItem;
    TreeItem<String> branchServers;
    TreeItem<String> branchClients;
    @FXML
    private void clickConnectBtn(ActionEvent actionEvent) throws ExecutionException, InterruptedException {

        System.out.println("Clicked Submit");
        int portNumber = Integer.valueOf(portNumberTextField.getText());
        ModbusSlave existingModbusSlave = modbusMainModel.getSlave(portNumber);
        if(existingModbusSlave != null){
            return;
        }
        ModbusSlave newModbusSlave = modbusMainModel.addSlave("192.168.1.16", portNumber);
        if (newModbusSlave != null){
            newModbusSlave.start();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    TreeItem<String> branchTestServer = new TreeItem<>(newModbusSlave.getIpAddress() + " " + String.valueOf(newModbusSlave.getPort()));
                    branchServers.getChildren().add(branchTestServer);
                }
            });
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        portNumberTextField.setText("5020");
        portNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
        });

        rootItem = new TreeItem<>("Modbus TCP Devices", new ImageView(new Image("images/47988_folder_icon.png")));

        branchServers = new TreeItem<>("Servers", new ImageView(new Image("images/47988_folder_icon.png")));
        branchClients = new TreeItem<>("Clients", new ImageView(new Image("images/47988_folder_icon.png")));

        rootItem.getChildren().addAll(branchServers, branchClients);

        treeView.setRoot(rootItem);

    }
    public void selectItem() {
        TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
        if(item != null){
            System.out.println(item.getValue());
        }
    }

}

package dev.claybradley.industrialautomationtoolkit.modbus;

import dev.claybradley.industrialautomationtoolkit.main.MainController;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.ModbusSlaveTabPaneModel;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.pollingtab.ModbusPollingTabModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Scope("prototype")
public class ModbusMainController implements Initializable {
    @FXML
    private HBox BodyHBox;
    @Autowired
    ApplicationContext applicationContext;
    @FXML
    private TextField addSlavePortNumTextField;
    @FXML
    private ChoiceBox removeSlavePortNumChoiceBox;
    @Autowired
    ModbusMainModel modbusMainModel;
    @FXML
    private TreeView treeView;
    private TreeItem<String> branchServers;
    private TreeItem<String> branchClients;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addSlavePortNumTextField.setText("5020");
        addSlavePortNumTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
        });

        TreeItem<String> rootItem = new TreeItem<>("Modbus TCP Devices", new ImageView(new Image("images/47988_folder_icon.png")));

        branchServers = new TreeItem<>("Servers", new ImageView(new Image("images/47988_folder_icon.png")));
        branchClients = new TreeItem<>("Clients", new ImageView(new Image("images/47988_folder_icon.png")));

        rootItem.getChildren().addAll(branchServers, branchClients);

        treeView.setRoot(rootItem);
        updateBranchServers();
    }

    @FXML
    private void clickAddSlave(ActionEvent actionEvent) throws ExecutionException, InterruptedException {
        System.out.println("Clicked Submit");
        int portNumber = Integer.valueOf(addSlavePortNumTextField.getText());
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
                    updateBranchServers();
                }
            });
        }
        ModbusSlaveTabPaneModel modbusSlaveTabPaneModel = new ModbusSlaveTabPaneModel(portNumber);
        modbusMainModel.addModbusSlaveTabPaneModel(modbusSlaveTabPaneModel);
    }

    @FXML
    private void clickRemoveSlave(ActionEvent actionEvent) {
        String portString = (String) removeSlavePortNumChoiceBox.getSelectionModel().getSelectedItem();
        int port = Integer.valueOf(portString);
        if(port == modbusMainModel.getSelectedSlave().getPort()){
            modbusMainModel.setSelectedSlave(null);
            hideModbusSlaveTabPane();
        }
        ModbusSlaveTabPaneModel modbusSlaveTabPaneModel = modbusMainModel.getModbusSlaveTabPaneModel(port);
        modbusMainModel.removeModbusSlaveTabPaneModel(modbusSlaveTabPaneModel);

        modbusMainModel.removeSlave(port);
        updateBranchServers();
    }

    @FXML
    private void clickSelectTreeItem(MouseEvent contextMenuEvent) {
        TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
        if(item != null){
            System.out.println(item.getValue());
            TreeItem parent = item.getParent();
            if(parent != null){
                if (parent.getValue().equals("Servers")){
                    String ipAndPort = item.getValue();
                    String portString = ipAndPort.substring(ipAndPort.lastIndexOf(":") + 1);
                    int port = Integer.valueOf(portString);
                    ModbusSlave newSelectedSlave = modbusMainModel.getSlave(port);
                    if (newSelectedSlave != null){
                        modbusMainModel.setSelectedSlave(newSelectedSlave);
                        showModbusSlaveTabPane();
                    }
                    System.out.println("New Selected Slave: " + String.valueOf(modbusMainModel.getSelectedSlave().getPort()));
                }
            }
        }
    }

    private void showModbusSlaveTabPane() {
            if (BodyHBox.getChildren().size() > 1) {
                BodyHBox.getChildren().remove(1);
            }
            Parent root = null;
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/modbus/ModbusSlaveTabPane.fxml"));
                fxmlLoader.setControllerFactory(applicationContext::getBean);
                root = fxmlLoader.load();
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            BodyHBox.getChildren().add(root);
    }

    private void hideModbusSlaveTabPane(){
        if (BodyHBox.getChildren().size() > 1) {
            BodyHBox.getChildren().remove(1);
        }
    }

    public void updateBranchServers(){
        ArrayList<ModbusSlave> slaves = modbusMainModel.getSlaves();
        branchServers.getChildren().clear();
        for(ModbusSlave modbusSlave: slaves){
            TreeItem<String> branchServer = new TreeItem<>(modbusSlave.getIpAddress() + ":" + String.valueOf(modbusSlave.getPort()));
            branchServers.getChildren().add(branchServer);
        }
        updateRemoveSlavePortNumChoiceBox();
    }

    public void updateRemoveSlavePortNumChoiceBox(){
        ArrayList<ModbusSlave> slaves = modbusMainModel.getSlaves();
        removeSlavePortNumChoiceBox.getItems().clear();
        for(ModbusSlave modbusSlave: slaves){
            String portNumber = String.valueOf(modbusSlave.getPort());
            Label label = new Label(portNumber);
            removeSlavePortNumChoiceBox.getItems().add(portNumber);
        }

    }


}

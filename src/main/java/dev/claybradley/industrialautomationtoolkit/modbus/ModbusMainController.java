package dev.claybradley.industrialautomationtoolkit.modbus;

import dev.claybradley.industrialautomationtoolkit.JavaFxApplication;
import dev.claybradley.industrialautomationtoolkit.main.MainController;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.ModbusSlaveTabPaneModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
    public Button addDeviceBtn;
    @FXML
    private Label addDeviceLabel;
    @FXML
    private Label removeDeviceLabel;
    @FXML
    private HBox ModbusMainBodyHBox;
    @FXML
    private VBox ModbusMain;
    @Autowired
    ApplicationContext applicationContext;
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
    buildTreeView();
    }

    public void buildTreeView(){
        TreeItem<String> rootItem = new TreeItem<>(" Modbus TCP Devices", new ImageView(new Image("images/47988_folder_icon.png")));

        branchServers = new TreeItem<>(" Servers", new ImageView(new Image("images/47988_folder_icon.png")));
        branchClients = new TreeItem<>(" Clients", new ImageView(new Image("images/47988_folder_icon.png")));

        rootItem.getChildren().addAll(branchServers, branchClients);

        treeView.setRoot(rootItem);
        updateBranchServers();

        ContextMenu addDeviceContextMenu = new ContextMenu();
        MenuItem addClient = new MenuItem("Add Client");
        MenuItem addServer = new MenuItem("Add Server");
        addDeviceContextMenu.getItems().addAll(addClient, addServer);

        addDeviceLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                addDeviceContextMenu.show(addDeviceLabel, mouseEvent.getScreenX(),mouseEvent.getScreenY());
            }
        });

        addServer.setOnAction(e -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/modbus/popups/AddServerPopup.fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Scene scene = new Scene(root, 500, 500);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    updateBranchServers();
                }
            });
            stage.show();

        });
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
            TreeItem parent = item.getParent();
            if(parent != null){
                if (parent.getValue().equals(" Servers")){
                    String ipAndPort = item.getValue();
                    String portString = ipAndPort.substring(ipAndPort.lastIndexOf(":") + 1);
                    int port = Integer.valueOf(portString);
                    ModbusSlave newSelectedSlave = modbusMainModel.getSlave(port);
                    if (newSelectedSlave != null){
                        modbusMainModel.setSelectedSlave(newSelectedSlave);
                        showModbusSlaveTabPane();
                    }
                }
            }
        }
    }

    private void showModbusSlaveTabPane() {
            if (ModbusMainBodyHBox.getChildren().size() > 1) {
                ModbusMainBodyHBox.getChildren().remove(1);
            }
            Parent root = null;
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/modbus/ModbusSlaveTabPane.fxml"));
                fxmlLoader.setControllerFactory(applicationContext::getBean);
                root = fxmlLoader.load();
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (root != null) {
                ModbusMainBodyHBox.getChildren().add(root);
                HBox.setHgrow(root, Priority.ALWAYS);
            }
    }

    private void hideModbusSlaveTabPane(){
        if (ModbusMainBodyHBox.getChildren().size() > 1) {
            ModbusMainBodyHBox.getChildren().remove(1);
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

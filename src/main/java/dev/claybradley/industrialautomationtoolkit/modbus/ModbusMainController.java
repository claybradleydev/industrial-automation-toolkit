package dev.claybradley.industrialautomationtoolkit.modbus;

import dev.claybradley.industrialautomationtoolkit.main.MainController;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.ModbusSlaveTabPaneController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.event.ChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Scope("prototype")
public class ModbusMainController implements Initializable {

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ModbusMainModel modbusMainModel;

    private TreeItem<String> branchServers;
    private TreeItem<String> branchClients;
    private Timer timer;

    @FXML
    private BorderPane ModbusMain;
    @FXML
    private Label connectionStatusLabel;
    @FXML
    private Label ipAddressLabel;
    @FXML
    private Label portNumberLabel;
    @FXML
    private FlowPane selectedSlaveFlowPane;
    @FXML
    private Label addDeviceLabel;
    @FXML
    private Label removeDeviceLabel;

    @FXML
    private ChoiceBox removeSlavePortNumChoiceBox;

    @FXML
    private TreeView treeView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        buildTreeView();
        setUpContextMenus();

        this.timer = new Timer();

        bindServersTreeBranch();

        if(modbusMainModel.getSelectedSlave() != null){
            showSelectedSlave();
        } else {
            selectedSlaveFlowPane.setVisible(false);
        }

    }


    public void bindServersTreeBranch(){
        updateServersTreeBranch();
        modbusMainModel.getSlaves().addListener(new ListChangeListener<ModbusSlave>() {
            @Override
            public void onChanged(Change<? extends ModbusSlave> change) {
                updateServersTreeBranch();
            }
        });
    }

    private void updateServersTreeBranch() {
        branchServers.getChildren().clear();
       ObservableList<ModbusSlave> slaves = modbusMainModel.getSlaves();
       for(ModbusSlave slave: slaves){
           if(slave.isRunning().get()){
           TreeItem treeItem = new TreeItem<>(" Localhost : " + slave.getPort(), new ImageView(new Image("images/PlcPng.png")));
           branchServers.getChildren().add(treeItem);
           } else{
               TreeItem treeItem = new TreeItem<>(" Localhost : " + slave.getPort(), new ImageView(new Image("images/381599_error_icon.png")));
               branchServers.getChildren().add(treeItem);
           }
       }
    }

    @FXML
    private void clickSelectTreeItem(MouseEvent contextMenuEvent) {
        TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
        if(item != null){
            TreeItem parent = item.getParent();
            if(parent != null){
                if (parent.getValue().equals(" Servers")){
                    String ipAndPort = item.getValue().trim();
                    String portString = ipAndPort.substring(ipAndPort.lastIndexOf(":") + 1).trim();
                    int port = Integer.valueOf(portString);
                    ModbusSlave newSelectedSlave = modbusMainModel.getSlave(port);
                    if (newSelectedSlave != null){
                        modbusMainModel.setSelectedSlave(newSelectedSlave);
                        showSelectedSlave();
                    }
                }
            }
        }
    }

    public void showSelectedSlave(){
        Parent root = null;
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/modbus/ModbusSlaveTabPane.fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            root = fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (root != null) {
            ModbusMain.setCenter(root);
            updateSelectedSlaveFowPane();
            modbusMainModel.getSelectedSlave().isRunning().addListener(new javafx.beans.value.ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    updateSelectedSlaveFowPane();
                }
            });
            selectedSlaveFlowPane.setVisible(true);
        }
        updateSelectedSlaveFowPane();
    }

    public void updateSelectedSlaveFowPane(){
        if(modbusMainModel.getSelectedSlave() != null) {
            ipAddressLabel.setText(modbusMainModel.getSelectedSlave().getIpAddress());
            portNumberLabel.setText(String.valueOf(modbusMainModel.getSelectedSlave().getPort()));
            boolean isRunning = modbusMainModel.getSelectedSlave().isRunning().getValue();
            if (isRunning) {
                connectionStatusLabel.setText("Connected");
                connectionStatusLabel.setStyle("-fx-text-fill: green");
            } else {
                connectionStatusLabel.setText("Not Connected");
                connectionStatusLabel.setStyle("-fx-text-fill: red");
            }
            selectedSlaveFlowPane.setVisible(true);
        } else{
            selectedSlaveFlowPane.setVisible(false);
            ModbusMain.setCenter(null);
        }
    }

    private void hideModbusSlaveTabPane(){
        ModbusMain.setCenter(null);
    }

    public void buildTreeView(){
        TreeItem<String> rootItem = new TreeItem<>(" Modbus TCP Devices", new ImageView(new Image("images/47988_folder_icon.png")));

        branchServers = new TreeItem<>(" Servers", new ImageView(new Image("images/47988_folder_icon.png")));
        branchClients = new TreeItem<>(" Clients", new ImageView(new Image("images/47988_folder_icon.png")));

        rootItem.getChildren().addAll(branchServers, branchClients);

        treeView.setRoot(rootItem);
    }


    private void setUpContextMenus() {
        setUpAddDeviceContextMenu();
        setUpRemoveDeviceContextMenu();
    }


    private void setUpRemoveDeviceContextMenu(){
        ContextMenu removeDeviceContextMenu = new ContextMenu();
        MenuItem removeClient = new MenuItem("Remove Client");

        MenuItem removeServer = new MenuItem("Remove Server");
        removeDeviceContextMenu.getItems().addAll(removeClient, removeServer);

        removeDeviceLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                removeDeviceContextMenu.show(removeDeviceLabel, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            }
        });

        removeServer.setOnAction(e -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/modbus/popups/RemoveSlavePopup.fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Scene scene = new Scene(root, 300, 150);
            Stage stage = new Stage();
            stage.setScene(scene);
        });
    }


    private void setUpAddDeviceContextMenu() {
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/modbus/popups/AddSlavePopup.fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Scene scene = new Scene(root, 300, 150);
            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        });
    }
}

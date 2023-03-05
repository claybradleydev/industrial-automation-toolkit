package dev.claybradley.industrialautomationtoolkit.modbus;

import dev.claybradley.industrialautomationtoolkit.main.MainController;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.ModbusSlaveTabPaneController;
import javafx.beans.property.SimpleBooleanProperty;
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
    @FXML
    private BorderPane ModbusMain;
    @FXML
    private Label connectionStatusLabel;
    @FXML
    private Label ipAddressLabel;
    @FXML
    private Label portNumberLabel;
    @FXML
    private VBox BodyVBoxForTabPane;
    @FXML
    private FlowPane selectedSlaveFlowPane;
    @FXML
    private Label addDeviceLabel;
    @FXML
    private Label removeDeviceLabel;
    @FXML
    private HBox ModbusMainBodyHBox;

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

    private Timer timer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.timer = new Timer();

        buildTreeView();

        if(modbusMainModel.getSelectedSlave() != null) {
            bindSelectedSlave();
        }

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateBranchServers();
            }
        }, 0, 1000);
    }

    private void bindSelectedSlave() {
        connectionStatusLabel.textProperty().bind(modbusMainModel.getSelectedSlave().runningAsStringProperty());
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
            stage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {

                    updateBranchServers();
                }
            });
            stage.show();
        });

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
            stage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    hideModbusSlaveTabPane();
                }
            });
            stage.show();
        });

    }


    @FXML
    private void clickSelectTreeItem(MouseEvent contextMenuEvent) {
        TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
        if(item != null){
            TreeItem parent = item.getParent();
            if(parent != null){
                if (parent.getValue().equals(" Servers")){
                    String ipAndPort = item.getValue().trim();
                    String portString = ipAndPort.substring(ipAndPort.lastIndexOf(":") + 1);
                    int port = Integer.valueOf(portString);
                    ModbusSlave newSelectedSlave = modbusMainModel.getSlave(port);
                    if (newSelectedSlave != null){
                        modbusMainModel.setSelectedSlave(newSelectedSlave);
                        showModbusSlaveTabPane();
                        bindSelectedSlave();
                    }
                }
            }
        }
    }

    private void showModbusSlaveTabPane() {
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

            }

    }

    private void addSelectedSlaveFlowPane() {
        ModbusSlave selectedSlave = modbusMainModel.getSelectedSlave();
        String ipAddress = selectedSlave.getIpAddress();
        String portNumber = String.valueOf(selectedSlave.getPort());

        BodyVBoxForTabPane.getChildren().add(0, selectedSlaveFlowPane);
        ipAddressLabel.setText(ipAddress);
        portNumberLabel.setText(portNumber);
    }


    private void hideModbusSlaveTabPane(){
        if (BodyVBoxForTabPane.getChildren().size() > 0) {
            ModbusMainBodyHBox.getChildren().clear();
        }
    }

    public void updateBranchServers(){
        ArrayList<ModbusSlave> slaves = modbusMainModel.getSlaves();
        branchServers.getChildren().clear();
        for(ModbusSlave modbusSlave: slaves){
            if(modbusSlave.isRunning().get()) {
                TreeItem<String> branchServer = new TreeItem<>(" " + modbusSlave.getIpAddress() + ":" + String.valueOf(modbusSlave.getPort()), new ImageView(new Image("images/PlcPng.png")));
                branchServers.getChildren().add(branchServer);
            } else{
                TreeItem<String> branchServer = new TreeItem<>(" " + modbusSlave.getIpAddress() + ":" + String.valueOf(modbusSlave.getPort()), new ImageView(new Image("images/381599_error_icon.png")));
                branchServers.getChildren().add(branchServer);
            }

        }
    }




}

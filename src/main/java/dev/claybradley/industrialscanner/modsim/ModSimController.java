package dev.claybradley.industrialscanner.modsim;

import dev.claybradley.industrialscanner.modbus.slave.ModbusSlave;
import dev.claybradley.industrialscanner.modbus.slave.ModbusSlaveService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class ModSimController implements Initializable {
    private final ModbusSlaveService modbusSlaveService;
    @FXML
    private TreeView treeView;
    @FXML
    private FlowPane devicesFlowPane;
    @FXML
    private FlowPane addressValueFlowPane;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private TextField portNumberTextField;
    @FXML
    private TextField unitIdTextField;

    TreeItem<String> rootItem;
    TreeItem<String> branchServers;
    TreeItem<String> branchClients;

    public ModSimController(){
        this.modbusSlaveService = new ModbusSlaveService();
        Timer myTimer = new Timer();

        TimerTask myTimerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        updateAddressLabelFlowPane();
                    }
                });

            }
        };
        myTimer.scheduleAtFixedRate(myTimerTask, 1000, 1000);
    }
    @FXML
    private void clickConnectBtn(ActionEvent actionEvent) throws ExecutionException, InterruptedException {
        System.out.println("Clicked Submit");
        int portNumber = Integer.valueOf(portNumberTextField.getText());
        ModbusSlave existingModbusSlave = modbusSlaveService.getSlave(portNumber);
        if(existingModbusSlave != null){
            return;
        }
        ModbusSlave newModbusSlave = modbusSlaveService.addSlave("192.168.1.16", portNumber);
        if (newModbusSlave != null){
            newModbusSlave.start();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                TreeItem<String> branchTestServer = new TreeItem<>("localhost port 502");
                branchServers.getChildren().add(branchTestServer);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        portNumberTextField.setText("5020");
        unitIdTextField.setText("0");
        addressTextField.setText("0");
        quantityTextField.setText("100");
        unitIdTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
        });
        portNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
        });
        addressTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
        });
        quantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
        });

        rootItem = new TreeItem<>("Modbus TCP Devices", new ImageView(new Image("47988_folder_icon.png")));

        branchServers = new TreeItem<>("Servers", new ImageView(new Image("47988_folder_icon.png")));
        branchClients = new TreeItem<>("Clients", new ImageView(new Image("47988_folder_icon.png")));

        rootItem.getChildren().addAll(branchServers, branchClients);

        treeView.setRoot(rootItem);
    }
    @FXML
    private void clickUpdateLabelsBtn(ActionEvent actionEvent) {
        updateAddressLabelFlowPane();
    }

    private void updateAddressLabelFlowPane() {

        addressValueFlowPane.getChildren().clear();
        devicesFlowPane.getChildren().clear();

        int address = Integer.valueOf(addressTextField.getText());
        int quantity = Integer.valueOf(quantityTextField.getText());
        int portNumber = Integer.valueOf(portNumberTextField.getText());
        ModbusSlave modbusSlave = modbusSlaveService.getSlave(portNumber);
        if (modbusSlave == null){
            return;
        }
        int [] holdingRegisters = modbusSlave.getRequestHandler().getModbusSlaveMemory().getHoldingRegisters();

        for(int i = 0; i <  quantity; ++i){
            Label label = new Label("Address: " + (address + i)  + " Value: " + holdingRegisters[address + i]);
            addressValueFlowPane.getChildren().add(label);
            label.setStyle("-fx-text-fill: white;" + "-fx-pref-width: 150;");
        }
        ArrayList<ModbusSlave> slaves = modbusSlaveService.getSlaves();
        for(ModbusSlave slave: slaves){
            Label label = new Label(String.valueOf(slave.getPort()));
            devicesFlowPane.getChildren().add(label);
            label.setStyle("-fx-text-fill: white;" + "-fx-pref-width: 150;");
        }
    }

    public void selectItem() {
        TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
        if(item != null){
            System.out.println(item.getValue());
        }


    }
}

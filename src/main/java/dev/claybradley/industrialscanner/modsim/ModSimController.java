package dev.claybradley.industrialscanner.modsim;

import dev.claybradley.industrialscanner.modbus.slave.ModbusSlave;
import dev.claybradley.industrialscanner.modbus.slave.ModbusSlaveService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class ModSimController implements Initializable {
    private final ModbusSlaveService modbusSlaveService;
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
    @FXML
    private Label connectedLabel;

    public ModSimController(){
        this.modbusSlaveService = new ModbusSlaveService();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                updateAddressLabelFlowPane();
            }
        };
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
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        portNumberTextField.setText("5020");
        unitIdTextField.setText("0");
        addressTextField.setText("0");
        quantityTextField.setText("100");
        connectedLabel.setText("Not Connected");
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
    }
    @FXML
    private void clickUpdateLabelsBtn(ActionEvent actionEvent) {
        updateAddressLabelFlowPane();
    }

    private void updateAddressLabelFlowPane() {
        addressValueFlowPane.getChildren().clear();
        int address = Integer.valueOf(addressTextField.getText());
        int quantity = Integer.valueOf(quantityTextField.getText());
        int portNumber = Integer.valueOf(portNumberTextField.getText());
        ModbusSlave modbusSlave = modbusSlaveService.getSlave(Integer.valueOf(portNumberTextField.getText()));
        int [] holdingRegisters = modbusSlave.getRequestHandlerIml().getModbusSlaveMemory().getHoldingRegisters();

        for(int i = 0; i <  quantity; ++i){
            Label label = new Label("Address: " + (address + i)  + " Value: " + holdingRegisters[i]);
            addressValueFlowPane.getChildren().add(label);
        }
    }
}

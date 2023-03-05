package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.configtab;

import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMainController;
import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMainModel;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

@Component
@Scope("prototype")
public class ConfigTabController implements Initializable {

    @Autowired
    ModbusMainModel modbusMainModel;
    @FXML
    private Label ipAddressLabel;
    @FXML
    private Label portNumberLabel;

    @FXML
    private Label deviceConnectedLabel;
    @FXML
    private Button connectBtn;
    @FXML
    private Button disconnectBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateDeviceConnectedLabel();
        updateIpAndPortLabels();

    }
    public void clickConnectBtn(ActionEvent actionEvent) throws ExecutionException, InterruptedException {
        ModbusSlave selectedSlave = modbusMainModel.getSelectedSlave();
        if(selectedSlave != null){
            if (!selectedSlave.isRunning().get()){
                selectedSlave.start();
            }
        }
        updateDeviceConnectedLabel();
    }

    public void clickDisconnectBtn(ActionEvent actionEvent) {
        ModbusSlave selectedSlave = modbusMainModel.getSelectedSlave();
        if(selectedSlave != null){
            if (selectedSlave.isRunning().get()){
                selectedSlave.stop();
            }
        }
        updateDeviceConnectedLabel();

    }

    private void updateDeviceConnectedLabel() {
        ModbusSlave selectedSlave = modbusMainModel.getSelectedSlave();
        if(selectedSlave != null){
            if(selectedSlave.isRunning().get()) {
                deviceConnectedLabel.setText("Device Connected");
            } else{
                deviceConnectedLabel.setText("Device Not Connected");
            }
        } else{
            deviceConnectedLabel.setText("Error");
        }
    }

    private void updateIpAndPortLabels(){
        ModbusSlave selectedSlave = modbusMainModel.getSelectedSlave();
        if(selectedSlave != null){
            ipAddressLabel.setText(selectedSlave.getIpAddress());
            portNumberLabel.setText(String.valueOf(selectedSlave.getPort()));
        }
    }


}

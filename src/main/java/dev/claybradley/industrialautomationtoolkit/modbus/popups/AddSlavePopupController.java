package dev.claybradley.industrialautomationtoolkit.modbus.popups;

import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMainModel;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.ModbusSlaveTabPaneModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

@Component
public class AddSlavePopupController implements Initializable {
    @Autowired
    public ModbusMainModel modbusMainModel;
    @FXML
    public HBox AddServerPopup;
    @FXML
    private TextField addSlavePortNumTextField;
    @FXML
    private void clickAddSlave(ActionEvent actionEvent) throws ExecutionException, InterruptedException {
        int portNumber = Integer.valueOf(addSlavePortNumTextField.getText());
        ModbusSlave existingModbusSlave = modbusMainModel.getSlave(portNumber);
        if(existingModbusSlave != null){
            return;
        }
        ModbusSlave newModbusSlave = modbusMainModel.addSlave("192.168.1.16", portNumber);
        ModbusSlaveTabPaneModel modbusSlaveTabPaneModel = new ModbusSlaveTabPaneModel(newModbusSlave);
        modbusMainModel.addModbusSlaveTabPaneModel(modbusSlaveTabPaneModel);
        Stage stage = (Stage) AddServerPopup.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addSlavePortNumTextField.setText("5020");


    }
}

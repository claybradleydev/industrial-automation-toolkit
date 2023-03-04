package dev.claybradley.industrialautomationtoolkit.modbus.popups;

import dev.claybradley.industrialautomationtoolkit.main.MainController;
import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMainController;
import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMainModel;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.ModbusSlaveTabPaneModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

@Component
public class AddServerPopupController implements Initializable {
    @Autowired
    public ModbusMainModel modbusMainModel;
    public AnchorPane rootAnchorPane;
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

        ModbusSlaveTabPaneModel modbusSlaveTabPaneModel = new ModbusSlaveTabPaneModel(portNumber, newModbusSlave);
        modbusMainModel.addModbusSlaveTabPaneModel(modbusSlaveTabPaneModel);

        Stage stage = (Stage) rootAnchorPane.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addSlavePortNumTextField.setText("5020");
        addSlavePortNumTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
        });
    }
}

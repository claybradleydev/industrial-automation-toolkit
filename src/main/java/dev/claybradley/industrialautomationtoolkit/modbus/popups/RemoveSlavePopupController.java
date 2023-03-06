package dev.claybradley.industrialautomationtoolkit.modbus.popups;

import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMainModel;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.ModbusSlaveTabPaneModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
public class RemoveSlavePopupController implements Initializable {
    @Autowired
    public ModbusMainModel modbusMainModel;
    @FXML
    private ChoiceBox removeSlavePortNumChoiceBox;
    @FXML
    private HBox RemoveServerPopup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateRemoveSlavePortNumChoiceBox();
    }

    @FXML
    public void clickRemoveSlave(ActionEvent actionEvent) {
        String portString = (String) removeSlavePortNumChoiceBox.getSelectionModel().getSelectedItem();
        int port = Integer.valueOf(portString);
        ModbusSlave modbusSlave = modbusMainModel.getSlave(port);
        modbusMainModel.removeSlave(port);
        Stage stage = (Stage) RemoveServerPopup.getScene().getWindow();
        stage.close();
    }

    public void updateRemoveSlavePortNumChoiceBox(){
        ObservableList<ModbusSlave> slaves = modbusMainModel.getSlaves();
        removeSlavePortNumChoiceBox.getItems().clear();
        for(ModbusSlave modbusSlave: slaves){
            String portNumber = String.valueOf(modbusSlave.getPort());
            Label label = new Label(portNumber);
            removeSlavePortNumChoiceBox.getItems().add(portNumber);
        }

    }
}

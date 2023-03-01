package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.pollingtab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class ModbusPollingTabController implements Initializable {
    public VBox PollingTab;

    private ModbusPollingTabModel modbusPollingTabModel = new ModbusPollingTabModel();
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private TextField unitIdTextField;
    @FXML
    private FlowPane addressValueFlowPane;
    @FXML
    private void clickUpdateLabelsBtn(ActionEvent actionEvent) {
        updateAddressLabelFlowPane();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        unitIdTextField.setText(String.valueOf(modbusPollingTabModel.getUnitId()));
        addressTextField.setText(String.valueOf(modbusPollingTabModel.getAddress()));
        quantityTextField.setText(String.valueOf(modbusPollingTabModel.getQuantity()));

        unitIdTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Unit ID Text Field changed from " + oldValue + " to " + newValue);
            modbusPollingTabModel.setUnitId(Integer.valueOf(newValue));
        });

        addressTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Unit ID Text Field changed from " + oldValue + " to " + newValue);
            modbusPollingTabModel.setAddress(Integer.valueOf(newValue));
        });

        quantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Unit ID Text Field changed from " + oldValue + " to " + newValue);
            modbusPollingTabModel.setQuantity(Integer.valueOf(newValue));
        });

    }

    private void updateAddressLabelFlowPane() {
/*
        addressValueFlowPane.getChildren().clear();
        ModbusSlave modbusSlave = modbusModel.getSlave(5020);
        if (modbusSlave == null){
            return;
        }
        int [] holdingRegisters = modbusSlave.getRequestHandler().getModbusSlaveMemory().getHoldingRegisters();
        int quantity = Integer.valueOf(quantityTextField.getText());
        int address = Integer.valueOf(addressTextField.getText());
        for(int i = 0; i <  quantity; ++i){
            Label label = new Label("Address: " + (address + i)  + " Value: " + holdingRegisters[address + i]);
            addressValueFlowPane.getChildren().add(label);
            label.setStyle("-fx-text-fill: white;" + "-fx-pref-width: 150;");
        }

 */
    }


}

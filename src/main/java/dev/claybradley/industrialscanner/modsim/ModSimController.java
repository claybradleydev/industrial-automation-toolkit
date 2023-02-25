package dev.claybradley.industrialscanner.modsim;

import dev.claybradley.industrialscanner.modbus.slave.ModbusSlave;
import dev.claybradley.industrialscanner.modbus.slave.ModbusSlaveService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ModSimController implements Initializable {

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

    @FXML
    private void clickConnectBtn(ActionEvent actionEvent) throws ExecutionException, InterruptedException {

        System.out.println("Clicked Submit");

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
}

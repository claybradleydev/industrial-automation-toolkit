package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.pollingtab;

import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMainModel;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

@Component
@Scope("prototype")
public class ModbusPollingTabController implements Initializable {

    public VBox PollingTab;
    public TextField pollingRateTextField;
    public Label pollingStatusLabel;
    public VBox ModbusPollingTab;
    private Timer timer;

    @Autowired
    ModbusMainModel modbusMainModel;
    private ModbusPollingTabModel modbusPollingTabModel;
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

    public ModbusPollingTabController(){
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.timer = new Timer();

        int port = modbusMainModel.getSelectedSlave().getPort();
        this.modbusPollingTabModel = modbusMainModel.getModbusSlaveTabPaneModel(port).getModbusPollingTabModel();
        if(modbusPollingTabModel.isPolling()){
            startPolling();
        }
        unitIdTextField.setText(String.valueOf(modbusPollingTabModel.getUnitId()));
        addressTextField.setText(String.valueOf(modbusPollingTabModel.getAddress()));
        quantityTextField.setText(String.valueOf(modbusPollingTabModel.getQuantity()));
        pollingRateTextField.setText(String.valueOf(modbusPollingTabModel.getPollingRate()));
        pollingStatusLabel.setText(String.valueOf(modbusPollingTabModel.isPolling()));

        unitIdTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            modbusPollingTabModel.setUnitId(Integer.valueOf(newValue));
        });

        addressTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            modbusPollingTabModel.setAddress(Integer.valueOf(newValue));
        });

        quantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            modbusPollingTabModel.setQuantity(Integer.valueOf(newValue));
        });

        pollingRateTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            modbusPollingTabModel.setPollingRate(Integer.valueOf(newValue));
            if(modbusPollingTabModel.isPolling()){
                timer.cancel();
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        updateAddressLabelFlowPane();
                    }
                }, 0, modbusPollingTabModel.getPollingRate());
            }
        });
    }

    private void updateAddressLabelFlowPane() {
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            addressValueFlowPane.getChildren().clear();
            ArrayList<String> values = modbusPollingTabModel.pollSlave();
            for (int i = 0; i < values.size(); ++i) {
                Label label = new Label(values.get(i));
                label.setStyle("-fx-text-fill: white;" + "-fx-pref-width: 150;");
                addressValueFlowPane.getChildren().add(label);
            }
        }
    };
    Platform.runLater(runnable);
    }

    private void updatePollingStatusLabel(){
        pollingStatusLabel.setText(String.valueOf(modbusPollingTabModel.isPolling()));
    }

    public void clickStartPolling(ActionEvent actionEvent) {
        startPolling();
    }

    public void startPolling(){
        modbusPollingTabModel.setPolling(true);
        updatePollingStatusLabel();
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateAddressLabelFlowPane();
            }
        }, 0, modbusPollingTabModel.getPollingRate());
    }

    public void clickStopPolling(ActionEvent actionEvent) {
        modbusPollingTabModel.setPolling(false);
        updatePollingStatusLabel();
        timer.cancel();
        timer = new Timer();
    }


}

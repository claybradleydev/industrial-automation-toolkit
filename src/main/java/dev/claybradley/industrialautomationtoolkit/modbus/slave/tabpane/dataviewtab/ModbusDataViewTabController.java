package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.dataviewtab;

import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMainModel;
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
public class ModbusDataViewTabController implements Initializable {

    public VBox PollingTab;
    public VBox ModbusDataViewTab;
    private Timer timer;

    @Autowired
    ModbusMainModel modbusMainModel;
    private ModbusDataViewTabModel modbusDataViewTabModel;
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

    public ModbusDataViewTabController(){
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.timer = new Timer();

        int port = modbusMainModel.getSelectedSlave().getPort();
        this.modbusDataViewTabModel = modbusMainModel.getModbusSlaveTabPaneModel(port).getModbusPollingTabModel();
        if(modbusDataViewTabModel.isPolling()){
            startPolling();
        }
        unitIdTextField.setText(String.valueOf(modbusDataViewTabModel.getUnitId()));
        addressTextField.setText(String.valueOf(modbusDataViewTabModel.getAddress()));
        quantityTextField.setText(String.valueOf(modbusDataViewTabModel.getQuantity()));

        unitIdTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            modbusDataViewTabModel.setUnitId(Integer.valueOf(newValue));
        });

        addressTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            modbusDataViewTabModel.setAddress(Integer.valueOf(newValue));
        });

        quantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            modbusDataViewTabModel.setQuantity(Integer.valueOf(newValue));
        });

        startPolling();

    }

    private void updateAddressLabelFlowPane() {
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            addressValueFlowPane.getChildren().clear();
            ArrayList<String> values = modbusDataViewTabModel.pollSlave();
            for (int i = 0; i < values.size(); ++i) {
                Label label = new Label(values.get(i));
                label.setStyle("-fx-text-fill: white;" + "-fx-pref-width: 150;");
                addressValueFlowPane.getChildren().add(label);
            }
        }
    };
    Platform.runLater(runnable);
    }

    public void startPolling(){
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateAddressLabelFlowPane();
            }
        }, 0, 500);
    }


}

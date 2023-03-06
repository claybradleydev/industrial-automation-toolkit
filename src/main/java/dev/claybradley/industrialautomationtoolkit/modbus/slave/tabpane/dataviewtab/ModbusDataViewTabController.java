package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.dataviewtab;

import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMainModel;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.*;

@Component
@Scope("prototype")
public class ModbusDataViewTabController implements Initializable {

    @Autowired
    ModbusMainModel modbusMainModel;

    private ModbusDataViewTabModel modbusDataViewTabModel;

    private Timer timer;

    @FXML
    private ChoiceBox functionChoiceBox;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private TextField unitIdTextField;
    @FXML
    private FlowPane addressValueFlowPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ModbusSlave modbusSlave = modbusMainModel.getSelectedSlave();
        this.modbusDataViewTabModel = modbusMainModel.getSelectedSlave().getModbusSlaveTabPaneModel().getModbusDataViewTabModel();

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

        initializeFunctionCodeChoiceBox();

        modbusDataViewTabModel.startPolling();

        modbusDataViewTabModel.getDataValues().addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change change) {
                updateAddressLabelFlowPane();
            }
        });
    }


    private void updateAddressLabelFlowPane() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                addressValueFlowPane.getChildren().clear();
                ObservableList<StringProperty> dataValues = modbusDataViewTabModel.getDataValues();
                Iterator<StringProperty> iterator = dataValues.iterator();
                while(iterator.hasNext()) {
                    Label label = new Label(iterator.next().getValue());
                    label.setStyle("-fx-pref-width: 100;");
                    addressValueFlowPane.getChildren().add(label);
                }
            }
        };

        Platform.runLater(runnable);
    }

    public void initializeFunctionCodeChoiceBox(){
        String readCoilsLabel = "Read Coils";
        String readDiscreteInputsLabel = "Read Discrete Inputs";
        String readHoldingRegistersLabel = "Read Holding Registers";
        String readInputRegistersLabel = "Read Input Registers";

        functionChoiceBox.getItems().addAll(readCoilsLabel, readDiscreteInputsLabel, readHoldingRegistersLabel, readInputRegistersLabel);
        functionChoiceBox.getSelectionModel().select(modbusDataViewTabModel.getFunctionCode() - 1);

        functionChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                modbusDataViewTabModel.setFunctionCode(number2.intValue() + 1);
            }
        });
    }

}

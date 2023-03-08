package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.dataviewtab;

import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMainModel;
import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMemoryArea;
import dev.claybradley.industrialautomationtoolkit.modbus.displayformat.ModbusDataFormat;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
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
    private ChoiceBox memoryAreaChoiceBox;
    @FXML
    private ChoiceBox dataFormatChoiceBox;
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
        initializeDataFormatChoiceBox();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateAddressLabelFlowPane();
            }
        }, 0, 500);

    }


    private void updateAddressLabelFlowPane() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                addressValueFlowPane.getChildren().clear();
                addressValueFlowPane.getChildren().addAll(modbusDataViewTabModel.pollSlave());
            }
        };

        Platform.runLater(runnable);
    }

    public void initializeFunctionCodeChoiceBox(){
        String readCoilsLabel = "Coils";
        String readDiscreteInputsLabel = "Discrete Inputs";
        String readHoldingRegistersLabel = "Holding Registers";
        String readInputRegistersLabel = "Input Registers";

        memoryAreaChoiceBox.getItems().addAll(readCoilsLabel, readDiscreteInputsLabel, readHoldingRegistersLabel, readInputRegistersLabel);
        memoryAreaChoiceBox.getSelectionModel().select(modbusDataViewTabModel.getModbusMemoryArea().getArea());

        memoryAreaChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                Optional<ModbusMemoryArea> optionalModbusMemoryArea = ModbusMemoryArea.fromArea(number2.intValue());
                if(optionalModbusMemoryArea.isPresent()){
                    modbusDataViewTabModel.setModbusMemoryArea(optionalModbusMemoryArea.get());
                }

            }
        });
    }

    public void initializeDataFormatChoiceBox(){
        String binaryLabel = "Binary";
        String decimalLabel = "Decimal";
        String hexLabel = "Hex";
        String longLabel = "Long";
        String longSwappedLabel = "Long Swapped";
        String floatLabel = "Float";
        String floatSwappedLabel = "Float Swapped";
        String float64BitLabel = "Float 64 Bit";
        String swapped64BitLabel = "Swapped 64 Bit";

        dataFormatChoiceBox.getItems().addAll(
                binaryLabel,
                decimalLabel,
                hexLabel,
                longLabel,
                longSwappedLabel,
                floatLabel,
                floatSwappedLabel,
                float64BitLabel,
                swapped64BitLabel
                );

        dataFormatChoiceBox.getSelectionModel().select(modbusDataViewTabModel.getModbusDataFormat().ordinal());

        dataFormatChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                Optional<ModbusDataFormat> optionalModbusDataFormat = ModbusDataFormat.fromFormat(dataFormatChoiceBox.getSelectionModel().getSelectedIndex());
                if(optionalModbusDataFormat.isPresent()){
                    modbusDataViewTabModel.setModbusDataFormat(optionalModbusDataFormat.get());
                }

            }
        });}

}

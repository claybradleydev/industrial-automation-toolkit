package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.dataviewtab;

import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMainModel;
import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMemoryArea;
import dev.claybradley.industrialautomationtoolkit.modbus.displayformat.ModbusAddressFormat;
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
    private ChoiceBox addressFormatChoiceBox;
    @FXML
    private ChoiceBox memoryAreaChoiceBox;
    @FXML
    private ChoiceBox dataFormatChoiceBox;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private FlowPane addressValueFlowPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ModbusSlave modbusSlave = modbusMainModel.getSelectedSlave();
        this.modbusDataViewTabModel = modbusMainModel.getSelectedSlave().getModbusSlaveTabPaneModel().getModbusDataViewTabModel();

        addressTextField.setText(String.valueOf(modbusDataViewTabModel.getAddress()));
        quantityTextField.setText(String.valueOf(modbusDataViewTabModel.getQuantity()));

        addressTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!observable.getValue().isEmpty()) {
                try {
                    int address = Integer.valueOf(newValue);
                    if (requestIsValid(Integer.valueOf(newValue), modbusDataViewTabModel.getQuantity())) {
                        modbusDataViewTabModel.setAddress(Integer.valueOf(newValue));
                    } else {
                        addressTextField.setText(oldValue);
                    }
                }catch(Exception e){
                    addressTextField.setText(oldValue);
                }
            }

        });
        quantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!observable.getValue().isEmpty()) {
                try {
                    int quantity = Integer.valueOf(newValue);
                    if (requestIsValid(modbusDataViewTabModel.getAddress(), Integer.valueOf(newValue))) {
                        modbusDataViewTabModel.setQuantity(Integer.valueOf(newValue));
                    } else {
                        quantityTextField.setText(oldValue);
                    }
                } catch (Exception e) {
                    quantityTextField.setText(oldValue);
                }
            }
        });

        initializeAddressFormatChoiceBox();
        initializeFunctionCodeChoiceBox();
        initializeDataFormatChoiceBox();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateAddressLabelFlowPane();
            }
        }, 0, 500);

        addressValueFlowPane.setHgap(50);
        addressValueFlowPane.setVgap(10);

    }

    private boolean requestIsValid(int address, int quantity) {
       if(address < 0){
           return false;
       }
       if(quantity < 0 || quantity > 1000){
           return false;
       }
       if(address + quantity >= 99999){
           return false;
       }
       return true;
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

    public void initializeAddressFormatChoiceBox(){
        String fiveDigit = "Five Digit";
        String sixDigit = "Six Digit";
        String fiveDigitHex = "Five Digit Hex";
        String sixDigitHex = "Six Digit Hex";

        addressFormatChoiceBox.getItems().addAll(fiveDigit, sixDigit, fiveDigitHex, sixDigitHex);
        addressFormatChoiceBox.getSelectionModel().select(modbusDataViewTabModel.getModbusAddressFormat().ordinal());

        addressFormatChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                Optional<ModbusAddressFormat> optionalModbusAddressFormat = ModbusAddressFormat.fromFormat(number2.intValue());
                if(optionalModbusAddressFormat.isPresent()){
                    modbusDataViewTabModel.setModbusAddressFormat(optionalModbusAddressFormat.get());
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

        dataFormatChoiceBox.getItems().addAll(
                binaryLabel,
                decimalLabel,
                hexLabel,
                longLabel,
                longSwappedLabel,
                floatLabel,
                floatSwappedLabel
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
        });
    }

}

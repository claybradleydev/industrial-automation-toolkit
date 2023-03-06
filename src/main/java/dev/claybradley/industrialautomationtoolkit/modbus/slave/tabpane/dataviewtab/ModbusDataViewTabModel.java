package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.dataviewtab;

import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class ModbusDataViewTabModel {

    private ModbusSlave modbusSlave;
    private int address;
    private int quantity;
    private int unitId;
    private int functionCode;

    private ObservableList<StringProperty> dataValues;

    public ModbusDataViewTabModel(ModbusSlave modbusSlave) {
        this.address = 0;
        this.quantity = 100;
        this.unitId = 0;
        this.functionCode = 3;
        this.modbusSlave = modbusSlave;
        this.dataValues = FXCollections.observableArrayList();

    }

    public void startPolling(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                pollSlave();
            }
        }, 0, 500);
    }

    public void pollSlave(){
        ObservableList<StringProperty> values = FXCollections.observableArrayList();

        if (modbusSlave == null){
            return;
        }

        switch(functionCode) {
            case 1:
                boolean[] coils = modbusSlave.getRequestHandler().getModbusSlaveMemory().getCoils(address, quantity);
                for (int i = 0; i < quantity; ++i) {
                    StringProperty value = new SimpleStringProperty((address + i) + " <" + coils[i] + ">");
                    values.add(value);
                }
                break;
            case 2:
                boolean[] discreteInputs = modbusSlave.getRequestHandler().getModbusSlaveMemory().getDiscreteInputs(address, quantity);
                for (int i = 0; i < quantity; ++i) {
                    StringProperty value = new SimpleStringProperty((address + i) + " <" + discreteInputs[i] + ">");
                    values.add(value);
                }
                break;
            case 3:
                int[] holdingRegisters = modbusSlave.getRequestHandler().getModbusSlaveMemory().getHoldingRegisters(address, quantity);
                for (int i = 0; i < quantity; ++i) {
                    StringProperty value = new SimpleStringProperty((address + i) + " <" + holdingRegisters[i] + ">");
                    values.add(value);
                }
                break;
            case 4:
                int[] inputRegisters = modbusSlave.getRequestHandler().getModbusSlaveMemory().getInputRegisters(address, quantity);
                for (int i = 0; i < quantity; ++i) {
                    StringProperty value = new SimpleStringProperty((address + i) + "     <" + inputRegisters[i] + ">");
                    values.add(value);
                }
                break;
        }

        dataValues.clear();
        dataValues.setAll(values);
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(int functionCode) {
        this.functionCode = functionCode;
    }

    public ObservableList getDataValues() {
        return dataValues;
    }
}

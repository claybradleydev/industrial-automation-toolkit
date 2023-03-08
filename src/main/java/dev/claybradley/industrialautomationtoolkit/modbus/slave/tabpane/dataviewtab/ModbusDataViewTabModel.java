package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.dataviewtab;

import com.digitalpetri.modbus.FunctionCode;
import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMemoryArea;
import dev.claybradley.industrialautomationtoolkit.modbus.displayformat.ModbusDataFormat;
import dev.claybradley.industrialautomationtoolkit.modbus.displayformat.ModbusDataLabelMaker;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class ModbusDataViewTabModel {

    private ModbusSlave modbusSlave;
    private ModbusDataFormat modbusDataFormat;
    private int address;
    private int quantity;
    private int unitId;
    private ModbusMemoryArea modbusMemoryArea;

    public ModbusDataViewTabModel(ModbusSlave modbusSlave) {
        this.address = 0;
        this.quantity = 100;
        this.unitId = 0;
        this.modbusMemoryArea = ModbusMemoryArea.HOLDING_REGISTER;
        this.modbusDataFormat = ModbusDataFormat.DECIMAL;
        this.modbusSlave = modbusSlave;
    }

    public ArrayList<HBox> pollSlave(){

       ArrayList<HBox> dataValues = new ArrayList<>();

        if (modbusSlave == null){
            return dataValues;
        }

        switch(modbusMemoryArea) {
            case COIL:
                boolean[] coils = modbusSlave.getRequestHandler().getModbusSlaveMemory().getCoils(address, quantity);
                ArrayList<HBox> formattedCoils = ModbusDataLabelMaker.getBooleanLabels(coils, address);
                dataValues.addAll(formattedCoils);
                break;
            case DISCRETE_INPUT:
                boolean[] discreteInputs = modbusSlave.getRequestHandler().getModbusSlaveMemory().getDiscreteInputs(address, quantity);
                ArrayList<HBox> formattedDiscreteInputs = ModbusDataLabelMaker.getBooleanLabels(discreteInputs, address);
                dataValues.addAll(formattedDiscreteInputs);
                break;
            case HOLDING_REGISTER:
                int[] holdingRegisters = modbusSlave.getRequestHandler().getModbusSlaveMemory().getHoldingRegisters(address, quantity);
                ArrayList<HBox> formattedRegisters = ModbusDataLabelMaker.getRegisterLabels(holdingRegisters, address, modbusDataFormat, ModbusMemoryArea.HOLDING_REGISTER);
                dataValues.addAll(formattedRegisters);
                break;
            case INPUT_REGISTER:
                int[] inputRegisters = modbusSlave.getRequestHandler().getModbusSlaveMemory().getInputRegisters(address, quantity);
                ArrayList<HBox> formattedInputRegisters = ModbusDataLabelMaker.getRegisterLabels(inputRegisters, address, modbusDataFormat, ModbusMemoryArea.INPUT_REGISTER);
                dataValues.addAll(formattedInputRegisters);
                break;
        }

        return dataValues;
    }
    public ModbusDataFormat getModbusDataFormat() {
        return modbusDataFormat;
    }

    public void setModbusDataFormat(ModbusDataFormat modbusDataFormat) {
        this.modbusDataFormat = modbusDataFormat;
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

    public ModbusMemoryArea getModbusMemoryArea() {
        return modbusMemoryArea;
    }

    public void setModbusMemoryArea(ModbusMemoryArea modbusMemoryArea) {
        this.modbusMemoryArea = modbusMemoryArea;
    }
}

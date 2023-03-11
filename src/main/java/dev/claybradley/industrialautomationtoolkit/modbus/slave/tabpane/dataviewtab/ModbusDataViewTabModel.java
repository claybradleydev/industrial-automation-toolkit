package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.dataviewtab;

import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMemoryArea;
import dev.claybradley.industrialautomationtoolkit.modbus.displayformat.ModbusAddressFormat;
import dev.claybradley.industrialautomationtoolkit.modbus.displayformat.ModbusDataFormat;
import dev.claybradley.industrialautomationtoolkit.modbus.displayformat.ModbusLabelMaker;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class ModbusDataViewTabModel {

    private ModbusSlave modbusSlave;
    private ModbusLabelMaker modbusLabelMaker;
    private ModbusDataFormat modbusDataFormat;
    private ModbusAddressFormat modbusAddressFormat;
    private int address;
    private int quantity;
    private ModbusMemoryArea modbusMemoryArea;

    public ModbusDataViewTabModel(ModbusSlave modbusSlave) {
        this.address = 0;
        this.quantity = 100;
        this.modbusMemoryArea = ModbusMemoryArea.HOLDING_REGISTER;
        this.modbusDataFormat = ModbusDataFormat.DECIMAL;
        this.modbusAddressFormat = ModbusAddressFormat.FIVE_DIGIT;
        this.modbusLabelMaker = new ModbusLabelMaker();
        this.modbusSlave = modbusSlave;
    }

    public ArrayList<HBox> pollSlave(){

       ArrayList<HBox> dataValues = new ArrayList<>();

        if (modbusSlave == null){
            return dataValues;
        }

        switch (modbusMemoryArea){
            case COIL: {
                boolean[] data = modbusSlave.getRequestHandler().getModbusSlaveMemory().getCoils(address, quantity);
                dataValues = modbusLabelMaker.getHBoxes(data, address, modbusMemoryArea, modbusDataFormat, modbusAddressFormat);
                break;
            }
            case DISCRETE_INPUT: {
                boolean[] data = modbusSlave.getRequestHandler().getModbusSlaveMemory().getDiscreteInputs(address, quantity);
                dataValues = modbusLabelMaker.getHBoxes(data, address, modbusMemoryArea, modbusDataFormat, modbusAddressFormat);
                break;
            }
            case HOLDING_REGISTER: {
                short[] data = modbusSlave.getRequestHandler().getModbusSlaveMemory().getHoldingRegisters(address, quantity);
                short[] validatedData = validateNumberOfRegisters(data, modbusDataFormat);
                dataValues = modbusLabelMaker.getHBoxes(validatedData, address, modbusMemoryArea, modbusDataFormat, modbusAddressFormat);
                break;
            }
            case INPUT_REGISTER: {
                short[] data = modbusSlave.getRequestHandler().getModbusSlaveMemory().getInputRegisters(address, quantity);
                short[] validatedData = validateNumberOfRegisters(data, modbusDataFormat);
                dataValues = modbusLabelMaker.getHBoxes(validatedData, address, modbusMemoryArea, modbusDataFormat, modbusAddressFormat);
                break;
            }
        }

        return dataValues;
    }

    public short[] validateNumberOfRegisters(short[] data, ModbusDataFormat modbusDataFormat) {
        short[] newData;
        ArrayList<HBox> dataList = new ArrayList<>();

        if(modbusDataFormat == ModbusDataFormat.LONG || modbusDataFormat == ModbusDataFormat.LONG_SWAPPED || modbusDataFormat == ModbusDataFormat.FLOAT || modbusDataFormat == ModbusDataFormat.FLOAT_SWAPPED){
            if(data.length <= 1){
                newData = new short[0];
            } else if(data.length % 2 != 0){
                newData = new short[data.length - 1];
                for(int i = 0; i < newData.length; ++i){
                    newData[i] = data[i];
                }
            } else{
                newData = data;
            }
        } else{
            return data;
        }
        return newData;

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

    public ModbusMemoryArea getModbusMemoryArea() {
        return modbusMemoryArea;
    }

    public void setModbusMemoryArea(ModbusMemoryArea modbusMemoryArea) {
        this.modbusMemoryArea = modbusMemoryArea;
    }

    public ModbusAddressFormat getModbusAddressFormat() {
        return modbusAddressFormat;
    }

    public void setModbusAddressFormat(ModbusAddressFormat modbusAddressFormat) {
        this.modbusAddressFormat = modbusAddressFormat;
    }
}

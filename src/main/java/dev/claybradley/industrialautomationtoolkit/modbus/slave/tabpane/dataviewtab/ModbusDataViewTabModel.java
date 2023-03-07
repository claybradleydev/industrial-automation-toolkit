package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.dataviewtab;

import dev.claybradley.industrialautomationtoolkit.modbus.displayformat.ModbusDataFormat;
import dev.claybradley.industrialautomationtoolkit.modbus.displayformat.ModbusDataFormatter;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;

import java.awt.*;
import java.util.ArrayList;

public class ModbusDataViewTabModel {

    private ModbusSlave modbusSlave;
    private ModbusDataFormat modbusDataFormat;
    private int address;
    private int quantity;
    private int unitId;
    private int functionCode;

    public ModbusDataViewTabModel(ModbusSlave modbusSlave) {
        this.address = 0;
        this.quantity = 100;
        this.unitId = 0;
        this.functionCode = 3;
        this.modbusDataFormat = ModbusDataFormat.HEX;
        this.modbusSlave = modbusSlave;
    }

    public ArrayList<String> pollSlave(){

       ArrayList<String> dataValues = new ArrayList<>();

        if (modbusSlave == null){
            return dataValues;
        }

        switch(functionCode) {
            case 1:
                boolean[] coils = modbusSlave.getRequestHandler().getModbusSlaveMemory().getCoils(address, quantity);
                for (int i = 0; i < quantity; ++i) {
                    String value = (address + i) + " <" + coils[i] + ">";
                    dataValues.add(value);
                }
                break;
            case 2:
                boolean[] discreteInputs = modbusSlave.getRequestHandler().getModbusSlaveMemory().getDiscreteInputs(address, quantity);
                for (int i = 0; i < quantity; ++i) {
                    String value = (address + i) + " <" + discreteInputs[i] + ">";
                    dataValues.add(value);
                }
                break;
            case 3:
                int[] holdingRegisters = modbusSlave.getRequestHandler().getModbusSlaveMemory().getHoldingRegisters(address, quantity);
                ArrayList<String> formattedRegisters = ModbusDataFormatter.formatData(holdingRegisters, modbusDataFormat);
                dataValues.addAll(formattedRegisters);
                break;
            case 4:
                int[] inputRegisters = modbusSlave.getRequestHandler().getModbusSlaveMemory().getInputRegisters(address, quantity);
                ArrayList<String> formattedInputRegisters = ModbusDataFormatter.formatData(inputRegisters, modbusDataFormat);
                dataValues.addAll(formattedInputRegisters);
                break;
        }

        return dataValues;
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

}

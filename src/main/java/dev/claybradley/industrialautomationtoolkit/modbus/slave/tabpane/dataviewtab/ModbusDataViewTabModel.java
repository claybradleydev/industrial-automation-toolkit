package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.dataviewtab;

import com.digitalpetri.modbus.FunctionCode;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;

import java.util.ArrayList;

public class ModbusDataViewTabModel {
    private int address;
    private int quantity;
    private int unitId;
    private int functionCode;
    private final ModbusSlave modbusSlave;

    public ModbusDataViewTabModel(ModbusSlave modbusSlave) {
        this.modbusSlave = modbusSlave;
        this.address = 0;
        this.quantity = 100;
        this.unitId = 0;
        this.functionCode = 3;
    }
    public ArrayList<String> pollSlave(){
        ArrayList<String> values = new ArrayList<>();

        if (modbusSlave == null){
            return null;
        }

        int [] holdingRegisters = modbusSlave.getRequestHandler().getModbusSlaveMemory().getHoldingRegisters();

        for(int i = 0; i <  quantity; ++i){
            String value ="Address: " + (address + i)  + " Value: " + holdingRegisters[address + i];
            values.add(value);
        }

        return values;
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

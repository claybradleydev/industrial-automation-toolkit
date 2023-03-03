package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.pollingtab;

import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import javafx.scene.control.Label;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ModbusPollingTabModel {
    private int address;
    private int quantity;
    private int unitId;

    private boolean polling;

    private int pollingRate;

    private final ModbusSlave modbusSlave;

    public ModbusPollingTabModel(ModbusSlave modbusSlave) {
        this.modbusSlave = modbusSlave;
        this.address = 0;
        this.quantity = 10;
        this.unitId = 0;
        this.pollingRate = 1000;
        this.polling = false;
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

    public boolean isPolling() {
        return polling;
    }

    public void setPolling(boolean polling) {
        this.polling = polling;
    }

    public int getPollingRate() {
        return pollingRate;
    }

    public void setPollingRate(int pollingRate) {
        this.pollingRate = pollingRate;
    }
}

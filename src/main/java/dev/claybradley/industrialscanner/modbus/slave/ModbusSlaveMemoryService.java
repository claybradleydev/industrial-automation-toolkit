package dev.claybradley.industrialscanner.modbus.slave;

import org.springframework.stereotype.Service;

@Service
public class ModbusSlaveMemoryService {

    private final boolean [] coils = new boolean[65535];
    private final boolean [] discreteInputs = new boolean[65535];
    private final int [] holdingRegisters = new int[65535];
    private final int [] inputRegisters = new int[65535];

    public boolean[] getCoils() {
        return coils;
    }
    public boolean[] getDiscreteInputs() {
        return discreteInputs;
    }
    public int[] getHoldingRegisters() {
        return holdingRegisters;
    }
    public int[] getInputRegisters() {
        return inputRegisters;
    }
    public void setHoldingRegister(int address, int value){
        holdingRegisters[address] = value;
    }
}

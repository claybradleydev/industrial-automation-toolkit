package dev.claybradley.industrialautomationtoolkit.modbus.slave;


public class ModbusSlaveMemory {
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

    public int[] getHoldingRegisters(int address, int quantity){
        int[] requestedRegisters = new int[quantity];
        for(int i = address; i < i + quantity; ++i){
            requestedRegisters[i - address] = holdingRegisters[i];
        }
        return requestedRegisters;
    }
    public int[] getInputRegisters() {
        return inputRegisters;
    }
    public void setHoldingRegister(int address, int value){
        holdingRegisters[address] = value;
    }
}

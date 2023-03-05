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
        for(int i = 0; i < quantity; ++i){
            requestedRegisters[i] = holdingRegisters[i + address];
        }
        return requestedRegisters;
    }
    public int[] getInputRegisters(int address, int quantity) {
        int[] requestedRegisters = new int[quantity];
        for(int i = 0; i < quantity; ++i){
            requestedRegisters[i] = inputRegisters[i + address];
        }
        return requestedRegisters;
    }

    public boolean[] getCoils(int address, int quantity) {
        boolean[] requestedCoils = new boolean[quantity];
        for(int i = 0; i < quantity; ++i){
            requestedCoils[i] = coils[i + address];
        }
        return requestedCoils;
    }

    public boolean[] getDiscreteInputs(int address, int quantity) {
        boolean[] requestedDiscreteInputs = new boolean[quantity];
        for(int i = 0; i < quantity; ++i){
            requestedDiscreteInputs[i] = discreteInputs[i + address];
        }
        return requestedDiscreteInputs;
    }

    public void setHoldingRegister(int address, int value){
        holdingRegisters[address] = value;
    }


}

package dev.claybradley.industrialautomationtoolkit.modbus.slave;


public class ModbusSlaveMemory {

    private final boolean [] coils = new boolean[65535];
    private final boolean [] discreteInputs = new boolean[65535];
    private final short [] holdingRegisters = new short[65535];
    private final short [] inputRegisters = new short[65535];

    public short[] getHoldingRegisters(int address, int quantity){
        short[] requestedRegisters = new short[quantity];
        for(int i = 0; i < quantity; ++i){
            requestedRegisters[i] = holdingRegisters[i + address];
        }
        return requestedRegisters;
    }

    public short[] getInputRegisters(int address, int quantity) {
        short[] requestedRegisters = new short[quantity];
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

    public void setHoldingRegister(int address, short value){
        holdingRegisters[address] = value;
    }

    public void setHoldingRegisters(int address, short[] values){
        for(int i = 0; i < values.length; ++i){
            holdingRegisters[i + address] = values[i];
        }
    }

    public boolean[] getCoils() {
        return coils;
    }

    public boolean[] getDiscreteInputs() {
        return discreteInputs;
    }
    public short[] getHoldingRegisters() {
        return holdingRegisters;
    }

}

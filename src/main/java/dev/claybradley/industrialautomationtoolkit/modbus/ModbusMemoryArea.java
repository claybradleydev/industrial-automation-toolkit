package dev.claybradley.industrialautomationtoolkit.modbus;

import java.util.Optional;

public enum ModbusMemoryArea {
    COIL(0),
    DISCRETE_INPUT(1),
    HOLDING_REGISTER(2),
    INPUT_REGISTER(3);

    private final int area;

    ModbusMemoryArea(int area){
        this.area = area;
    }

    public int getArea(){
        return area;
    }

    public static Optional<ModbusMemoryArea> fromArea(int area) {
        switch (area) {
            case 0: return Optional.of(COIL);
            case 1: return Optional.of(DISCRETE_INPUT);
            case 2: return Optional.of(HOLDING_REGISTER);
            case 3: return Optional.of(INPUT_REGISTER);
        }

        return Optional.empty();
    }
}

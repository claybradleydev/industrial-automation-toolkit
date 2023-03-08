package dev.claybradley.industrialautomationtoolkit.modbus.displayformat;

import java.util.Optional;

public enum ModbusDataFormat {
    BINARY(0),
    DECIMAL(1),
    HEX(2),
    LONG(3),
    LONG_SWAPPED(4),
    FLOAT(5),
    FLOAT_SWAPPED(6),
    FLOAT_64BIT(7),
    SWAPPED_64BIT(8);

    private final int format;

    ModbusDataFormat(int format){
        this.format = format;
    }

    public int getFormat(){
        return format;
    }

    public static Optional<ModbusDataFormat> fromFormat(int format) {
        switch (format) {
            case 0: return Optional.of(BINARY);
            case 1: return Optional.of(DECIMAL);
            case 2: return Optional.of(HEX);
            case 3: return Optional.of(LONG);
            case 4: return Optional.of(LONG_SWAPPED);
            case 5: return Optional.of(FLOAT);
            case 6: return Optional.of(FLOAT_SWAPPED);
            case 7: return Optional.of(FLOAT_64BIT);
            case 8: return Optional.of(SWAPPED_64BIT);
        }

        return Optional.empty();
    }
}

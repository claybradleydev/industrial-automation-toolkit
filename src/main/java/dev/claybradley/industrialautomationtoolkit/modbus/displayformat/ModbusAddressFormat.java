package dev.claybradley.industrialautomationtoolkit.modbus.displayformat;

import java.util.Optional;

public enum ModbusAddressFormat {
    FIVE_DIGIT(0),
    SIX_DIGIT(1),
    FIVE_DIGIT_HEX(2),
    SIX_DIGIT_HEX(3);
    private final int format;

    ModbusAddressFormat(int format){
        this.format = format;
    }

    public int getFormat(){
        return format;
    }

    public static Optional<ModbusAddressFormat> fromFormat(int format) {
        switch (format) {
            case 0: return Optional.of(FIVE_DIGIT);
            case 1: return Optional.of(SIX_DIGIT);
            case 2: return Optional.of(FIVE_DIGIT_HEX);
            case 3: return Optional.of(SIX_DIGIT_HEX);
        }
        return Optional.empty();
    }
}

package dev.claybradley.industrialscanner.modsim;

import dev.claybradley.industrialscanner.modbus.slave.ModbusSlaveService;
import org.springframework.stereotype.Component;

@Component
public class TestModbusSlave {
    private final ModbusSlaveService modbusSlaveService;

    public TestModbusSlave(ModbusSlaveService modbusSlaveService){
        this.modbusSlaveService = modbusSlaveService;
        modbusSlaveService.start("192.168.1.16", 5020);
    }
}

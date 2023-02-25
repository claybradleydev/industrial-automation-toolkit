package dev.claybradley.industrialscanner.modbus.slave;

import com.digitalpetri.modbus.slave.ModbusTcpSlave;
import com.digitalpetri.modbus.slave.ModbusTcpSlaveConfig;
import java.util.ArrayList;

public class ModbusSlaveService {
    public ArrayList<ModbusSlave> getSlaves() {
        return slaves;
    }

    private final ArrayList<ModbusSlave> slaves;

    public ModbusSlaveService(){
        slaves = new ArrayList<>();
    }

    public ModbusSlave addSlave(String ipAddress, int port){
        ModbusTcpSlaveConfig config = new ModbusTcpSlaveConfig.Builder().build();
        ModbusTcpSlave modbusTcpSlave = new ModbusTcpSlave(config);
        ModbusSlaveMemory modbusSlaveMemory = new ModbusSlaveMemory();
        ServiceRequestHandlerIml serviceRequestHandlerIml = new ServiceRequestHandlerIml(modbusSlaveMemory);
        ModbusSlave modbusSlave = new ModbusSlave(modbusTcpSlave, serviceRequestHandlerIml, ipAddress, port);
        slaves.add(modbusSlave);
        return modbusSlave;
    }

    public ModbusSlave getSlave(int port){
        for(ModbusSlave slave : slaves){
            if (slave.getPort() == port){
                return slave;
            }
        }
        return null;
    }

}

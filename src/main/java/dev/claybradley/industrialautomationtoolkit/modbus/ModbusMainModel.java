package dev.claybradley.industrialautomationtoolkit.modbus;

import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ServiceRequestHandlerIml;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
@Component
public class ModbusMainModel {

    private final ArrayList<ModbusSlave> slaves;
    private ModbusSlave selectedSlave;

    public ModbusMainModel(){
        slaves = new ArrayList<>();
    }

    public ModbusSlave addSlave(String ipAddress, int port){
        ServiceRequestHandlerIml serviceRequestHandlerIml = new ServiceRequestHandlerIml();
        ModbusSlave modbusSlave = new ModbusSlave(serviceRequestHandlerIml, ipAddress, port);
        slaves.add(modbusSlave);
        selectedSlave = modbusSlave;
        return modbusSlave;
    }

    public void removeSlave(int port){
        ModbusSlave modbusSlave = getSlave(port);
        modbusSlave.stop();
        slaves.remove(modbusSlave);
        selectedSlave = null;
    }

    public ModbusSlave getSlave(int port){
        for(ModbusSlave slave : slaves){
            if (slave.getPort() == port){
                return slave;
            }
        }
        return null;
    }

    public ArrayList<ModbusSlave> getSlaves() {
        return slaves;
    }

}

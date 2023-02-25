package dev.claybradley.industrialscanner.modbus.slave;

import org.springframework.stereotype.Service;
import com.digitalpetri.modbus.slave.ModbusTcpSlave;
import com.digitalpetri.modbus.slave.ModbusTcpSlaveConfig;

@Service
public class ModbusSlaveService {
    private final ModbusTcpSlaveConfig config = new ModbusTcpSlaveConfig.Builder().build();
    private final ModbusTcpSlave slave = new ModbusTcpSlave(config);
    private final ServiceRequestHandlerIml serviceRequestHandlerIml;

    public ModbusSlaveService(ServiceRequestHandlerIml serviceRequestHandlerIml){
        this.serviceRequestHandlerIml = serviceRequestHandlerIml;
    }
    public void start(String address, int port){
        slave.setRequestHandler(serviceRequestHandlerIml);
        slave.bind(address, port);
    }

    public void stop(){
        slave.shutdown();
    }

}

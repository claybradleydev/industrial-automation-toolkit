package dev.claybradley.industrialautomationtoolkit.modbus.slave;

import com.digitalpetri.modbus.slave.ModbusTcpSlave;
import com.digitalpetri.modbus.slave.ModbusTcpSlaveConfig;

import java.util.concurrent.ExecutionException;

public class ModbusSlave {
    private final ModbusTcpSlave modbusTcpSlave;
    private final ServiceRequestHandlerIml requestHandler;
    private final String ipAddress;
    private final int port;

    public ModbusSlave(ServiceRequestHandlerIml requestHandler, String ipAddress, int port) {
        ModbusTcpSlaveConfig config = new ModbusTcpSlaveConfig.Builder().build();
        this.modbusTcpSlave = new ModbusTcpSlave(config);
        this.requestHandler = requestHandler;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public ModbusTcpSlave getModbusTcpSlave() {
        return modbusTcpSlave;
    }

    public ServiceRequestHandlerIml getRequestHandler() {
        return requestHandler;
    }

    public void start() throws ExecutionException, InterruptedException {
        modbusTcpSlave.setRequestHandler(requestHandler);
        modbusTcpSlave.bind(ipAddress, port).get();
    }

    public void stop(){
        modbusTcpSlave.shutdown();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }
}

package dev.claybradley.industrialscanner.modbus.slave;

import com.digitalpetri.modbus.slave.ModbusTcpSlave;

import java.util.concurrent.ExecutionException;

public class ModbusSlave {
    private final ModbusTcpSlave modbusTcpSlave;
    private final ServiceRequestHandlerIml requestHandlerIml;
    private final String ipAddress;
    private final int port;

    public ModbusSlave(ModbusTcpSlave modbusTcpSlave, ServiceRequestHandlerIml requestHandlerIml, String ipAddress, int port) {
        this.modbusTcpSlave = modbusTcpSlave;
        this.requestHandlerIml = requestHandlerIml;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public ModbusTcpSlave getModbusTcpSlave() {
        return modbusTcpSlave;
    }

    public ServiceRequestHandlerIml getRequestHandlerIml() {
        return requestHandlerIml;
    }

    public void start() throws ExecutionException, InterruptedException {
        modbusTcpSlave.setRequestHandler(requestHandlerIml);
        modbusTcpSlave.bind(ipAddress, port).get();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }



}

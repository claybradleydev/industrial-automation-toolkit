package dev.claybradley.industrialautomationtoolkit.modbus.slave;

import com.digitalpetri.modbus.slave.ModbusTcpSlave;
import com.digitalpetri.modbus.slave.ModbusTcpSlaveConfig;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.ModbusSlaveTabPaneModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.net.*;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;

public class ModbusSlave {
    private final ModbusTcpSlave modbusTcpSlave;
    private final ServiceRequestHandlerIml requestHandler;
    private final ModbusSlaveTabPaneModel modbusSlaveTabPaneModel;
    private String ipAddress;
    private final int port;
    private BooleanProperty running;


    public ModbusSlave(int port) throws UnknownHostException {
        this.modbusSlaveTabPaneModel = new ModbusSlaveTabPaneModel(this);
        this.requestHandler = new ServiceRequestHandlerIml();
        ModbusTcpSlaveConfig config = new ModbusTcpSlaveConfig.Builder().build();
        this.modbusTcpSlave = new ModbusTcpSlave(config);
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while( networkInterfaceEnumeration.hasMoreElements()){
                for ( InterfaceAddress interfaceAddress : networkInterfaceEnumeration.nextElement().getInterfaceAddresses())
                    if ( interfaceAddress.getAddress().isSiteLocalAddress())
                        this.ipAddress = interfaceAddress.getAddress().getHostAddress();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        this.port = port;
        this.running = new SimpleBooleanProperty(false);
    }

    public void start() throws ExecutionException, InterruptedException {
        modbusTcpSlave.setRequestHandler(requestHandler);
        modbusTcpSlave.bind(ipAddress, port).get();
        running.setValue(true);
    }

    public void stop(){
        modbusTcpSlave.shutdown();
        running.setValue(false);
    }


    public String getIpAddress() {
        return ipAddress;
    }
    public int getPort() {
        return port;
    }
    public BooleanProperty isRunning() {
        return running;
    }
    public ServiceRequestHandlerIml getRequestHandler() {
        return requestHandler;
    }
    public ModbusSlaveTabPaneModel getModbusSlaveTabPaneModel() {
        return modbusSlaveTabPaneModel;
    }
}
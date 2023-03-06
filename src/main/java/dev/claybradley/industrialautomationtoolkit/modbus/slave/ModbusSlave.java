package dev.claybradley.industrialautomationtoolkit.modbus.slave;

import com.digitalpetri.modbus.slave.ModbusTcpSlave;
import com.digitalpetri.modbus.slave.ModbusTcpSlaveConfig;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.ModbusSlaveTabPaneModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.concurrent.ExecutionException;

public class ModbusSlave {
    private final ModbusTcpSlave modbusTcpSlave;
    private final ServiceRequestHandlerIml requestHandler;
    private final ModbusSlaveTabPaneModel modbusSlaveTabPaneModel;
    private final String ipAddress;
    private final int port;
    private BooleanProperty running;


    public ModbusSlave(String ipAddress, int port) {
        this.modbusSlaveTabPaneModel = new ModbusSlaveTabPaneModel(this);
        this.requestHandler = new ServiceRequestHandlerIml();
        ModbusTcpSlaveConfig config = new ModbusTcpSlaveConfig.Builder().build();
        this.modbusTcpSlave = new ModbusTcpSlave(config);
        this.ipAddress = ipAddress;
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

    public void setRunning(boolean running) {

        this.running.setValue(running);
    }

    public ModbusTcpSlave getModbusTcpSlave() {
        return modbusTcpSlave;
    }
    public ServiceRequestHandlerIml getRequestHandler() {
        return requestHandler;
    }
    public ModbusSlaveTabPaneModel getModbusSlaveTabPaneModel() {
        return modbusSlaveTabPaneModel;
    }
}
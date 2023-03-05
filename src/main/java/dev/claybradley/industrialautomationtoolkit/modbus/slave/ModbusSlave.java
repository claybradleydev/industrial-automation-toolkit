package dev.claybradley.industrialautomationtoolkit.modbus.slave;

import com.digitalpetri.modbus.slave.ModbusTcpSlave;
import com.digitalpetri.modbus.slave.ModbusTcpSlaveConfig;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;

import java.util.concurrent.ExecutionException;

public class ModbusSlave {
    private final ModbusTcpSlave modbusTcpSlave;
    private final ServiceRequestHandlerIml requestHandler;
    private final String ipAddress;
    private final int port;
    private BooleanProperty running;
    private StringProperty runningAsString;

    public ModbusSlave(ServiceRequestHandlerIml requestHandler, String ipAddress, int port) {
        ModbusTcpSlaveConfig config = new ModbusTcpSlaveConfig.Builder().build();
        this.modbusTcpSlave = new ModbusTcpSlave(config);
        this.requestHandler = requestHandler;
        this.ipAddress = ipAddress;
        this.port = port;
        this.running = new SimpleBooleanProperty();
        this.runningAsString = new SimpleStringProperty("Not Connected");
    }

    public void start() throws ExecutionException, InterruptedException {
        modbusTcpSlave.setRequestHandler(requestHandler);
        modbusTcpSlave.bind(ipAddress, port).get();
        running.setValue(true);
        runningAsString.setValue("Connected");
    }

    public void stop(){
        modbusTcpSlave.shutdown();
        running.setValue(false);
        runningAsString.setValue("Not Connected");
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
        this.runningAsString.setValue("Connected");
    }

    public StringProperty runningAsStringProperty() {
        return runningAsString;
    }


    public ModbusTcpSlave getModbusTcpSlave() {
        return modbusTcpSlave;
    }

    public ServiceRequestHandlerIml getRequestHandler() {
        return requestHandler;
    }}
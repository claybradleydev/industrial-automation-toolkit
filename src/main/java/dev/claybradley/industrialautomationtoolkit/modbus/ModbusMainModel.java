package dev.claybradley.industrialautomationtoolkit.modbus;

import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ServiceRequestHandlerIml;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.ModbusSlaveTabPaneModel;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Component
public class ModbusMainModel {

    private final ObservableList<ModbusSlave> slaves;

    private ModbusSlave selectedSlave;


    public ModbusMainModel() throws ExecutionException, InterruptedException, UnknownHostException {

        this.slaves = FXCollections.observableArrayList(p -> new Observable[]{p.isRunning()});

        ModbusSlave newModbusSlave = addSlave(5020);
        newModbusSlave.start();
        newModbusSlave.getRequestHandler().getModbusSlaveMemory().setHoldingRegister(0, (short) 199);
    }

    public ModbusSlave addSlave(int port) throws UnknownHostException {
        ModbusSlave modbusSlave = new ModbusSlave(port);
        slaves.add(modbusSlave);
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

    public ObservableList<ModbusSlave> getSlaves() {
        return slaves;
    }

    public ModbusMainModel(ObservableList<ModbusSlave> slaves) {
        this.slaves = slaves;
    }

    public void setSelectedSlave(ModbusSlave selectedSlave) {

        this.selectedSlave = selectedSlave;

    }
    public ModbusSlave getSelectedSlave() {
        return selectedSlave;
    }

}

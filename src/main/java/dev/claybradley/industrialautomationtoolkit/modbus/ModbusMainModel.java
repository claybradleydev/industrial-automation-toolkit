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

import java.util.ArrayList;
@Component
public class ModbusMainModel {

    private final ObservableList<ModbusSlave> slaves;

    private ModbusSlave selectedSlave;


    public ModbusMainModel(){

        this.slaves = FXCollections.observableArrayList(p -> new Observable[]{p.isRunning()});

        ModbusSlave newModbusSlave = addSlave("192.168.1.16", 5020);

    }

    public ModbusSlave addSlave(String ipAddress, int port){
        ModbusSlave modbusSlave = new ModbusSlave(ipAddress, port);
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

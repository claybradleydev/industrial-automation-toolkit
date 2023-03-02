package dev.claybradley.industrialautomationtoolkit.modbus;

import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ServiceRequestHandlerIml;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.ModbusSlaveTabPaneModel;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.pollingtab.ModbusPollingTabModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
@Component
public class ModbusMainModel {

    private final ArrayList<ModbusSlave> slaves;


    private final ArrayList<ModbusSlaveTabPaneModel> modbusSlaveTabPaneModels;
    private ModbusSlave selectedSlave;

    public ModbusMainModel(){

        slaves = new ArrayList<>();

        this.modbusSlaveTabPaneModels = new ArrayList<>();
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

    public void setSelectedSlave(ModbusSlave selectedSlave) {
        this.selectedSlave = selectedSlave;
    }

    public ModbusSlave getSelectedSlave() {
        return selectedSlave;
    }

    public ArrayList<ModbusSlaveTabPaneModel> getModbusSlaveTabPaneModels() {
        return modbusSlaveTabPaneModels;
    }

    public ModbusSlaveTabPaneModel getModbusSlaveTabPaneModel(int port){
        for(ModbusSlaveTabPaneModel modbusSlaveTabPaneModel: modbusSlaveTabPaneModels){
            if(modbusSlaveTabPaneModel.getPort() == port){
                return modbusSlaveTabPaneModel;
            }
        }
        return null;
    }

    public void removeModbusSlaveTabPaneModel(ModbusSlaveTabPaneModel modbusSlaveTabPaneModel){

        modbusSlaveTabPaneModels.remove(modbusSlaveTabPaneModel);
    }

    public void addModbusSlaveTabPaneModel(ModbusSlaveTabPaneModel modbusSlaveTabPaneModel){

        modbusSlaveTabPaneModels.add(modbusSlaveTabPaneModel);
    }
}

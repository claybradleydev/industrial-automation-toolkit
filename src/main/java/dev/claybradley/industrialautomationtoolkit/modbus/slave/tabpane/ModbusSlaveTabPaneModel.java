package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane;

import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.dataviewtab.ModbusDataViewTabModel;

public class ModbusSlaveTabPaneModel {

    private final ModbusDataViewTabModel modbusDataViewTabModel;
    private final ModbusSlave modbusSlave;
    private int selectedTab;


    public ModbusSlaveTabPaneModel(ModbusSlave modbusSlave) {
        this.modbusSlave = modbusSlave;
        modbusDataViewTabModel = new ModbusDataViewTabModel(modbusSlave);
        selectedTab = 0;
    }

    public int getSelectedTab() {
        return selectedTab;
    }
    public void setSelectedTab(int selectedTab) {
        this.selectedTab = selectedTab;
    }

    public int getPort() {
        return modbusSlave.getPort();
    }

    public ModbusDataViewTabModel getModbusDataViewTabModel() {
        return modbusDataViewTabModel;
    }
}

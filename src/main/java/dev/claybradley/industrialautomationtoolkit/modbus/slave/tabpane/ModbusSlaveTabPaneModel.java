package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane;

import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.dataviewtab.ModbusDataViewTabModel;

public class ModbusSlaveTabPaneModel {
    private ModbusSlave modbusSlave;
    private ModbusDataViewTabModel modbusDataViewTabModel;
    private int selectedTab;

    public ModbusSlaveTabPaneModel(ModbusSlave modbusSlave) {
        this.modbusSlave = modbusSlave;
        this.modbusDataViewTabModel = new ModbusDataViewTabModel(modbusSlave);
    }

    public int getSelectedTab() {
        return selectedTab;
    }
    public void setSelectedTab(int selectedTab) {
        this.selectedTab = selectedTab;
    }

    public ModbusDataViewTabModel getModbusDataViewTabModel() {
        return modbusDataViewTabModel;
    }


}

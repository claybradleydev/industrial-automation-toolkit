package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane;

import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.pollingtab.ModbusPollingTabModel;

public class ModbusSlaveTabPaneModel {
    private final ModbusPollingTabModel modbusPollingTabModel;
    private int selectedTab;
    private int port;

    private final ModbusSlave modbusSlave;

    public ModbusSlaveTabPaneModel(int port, ModbusSlave modbusSlave) {
        this.port = port;
        this.modbusSlave = modbusSlave;
        modbusPollingTabModel = new ModbusPollingTabModel(modbusSlave);
        selectedTab = 0;
    }

    public int getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(int selectedTab) {
        this.selectedTab = selectedTab;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ModbusPollingTabModel getModbusPollingTabModel() {
        return modbusPollingTabModel;
    }
}

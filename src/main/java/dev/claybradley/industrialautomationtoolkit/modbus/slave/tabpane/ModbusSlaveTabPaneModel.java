package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane;

import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.pollingtab.ModbusPollingTabModel;

import java.util.ArrayList;

public class ModbusSlaveTabPaneModel {
    private final ModbusPollingTabModel modbusPollingTabModel;
    private int selectedTab;
    private int port;

    public ModbusSlaveTabPaneModel(int port) {
        this.port = port;
        modbusPollingTabModel = new ModbusPollingTabModel();
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

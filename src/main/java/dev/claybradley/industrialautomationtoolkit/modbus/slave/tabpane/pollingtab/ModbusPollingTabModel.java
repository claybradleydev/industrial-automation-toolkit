package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.pollingtab;

public class ModbusPollingTabModel {
    private int address;
    private int quantity;
    private int unitId;

    public ModbusPollingTabModel(){
        this.address = 0;
        this.quantity = 10;
        this.unitId = 0;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }
}

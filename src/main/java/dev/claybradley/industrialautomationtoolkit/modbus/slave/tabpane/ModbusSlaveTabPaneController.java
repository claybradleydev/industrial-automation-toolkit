package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane;

import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMainModel;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class ModbusSlaveTabPaneController implements Initializable {

    public AnchorPane ModbusConfigTab;
    public VBox ModbusPollingTab;
    public HBox ModbusSlaveTabPane;
    public TabPane ModbusSlaveTabPanePane;
    @Autowired
    ModbusMainModel modbusMainModel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ModbusSlave modbusSlave = modbusMainModel.getSelectedSlave();
        int tab = modbusMainModel.getModbusSlaveTabPaneModel(modbusSlave.getPort()).getSelectedTab();
        ModbusSlaveTabPanePane.getSelectionModel().select(tab);
    }
    @FXML
    private void switchTabClick(MouseEvent mouseEvent) {
        int port = modbusMainModel.getSelectedSlave().getPort();
        modbusMainModel.getModbusSlaveTabPaneModel(port).setSelectedTab(ModbusSlaveTabPanePane.getSelectionModel().getSelectedIndex());
    }
}

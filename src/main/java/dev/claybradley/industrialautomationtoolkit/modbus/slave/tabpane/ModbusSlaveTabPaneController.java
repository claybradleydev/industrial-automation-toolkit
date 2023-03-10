package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane;

import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMainController;
import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMainModel;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.ModbusSlave;
import dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.configtab.ConfigTabController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class ModbusSlaveTabPaneController implements Initializable {

    @Autowired
    ModbusMainModel modbusMainModel;
    @Autowired
    ApplicationContext applicationContext;

    @FXML
    private TabPane ModbusSlaveTabPanePane;
    @FXML
    private Tab ModbusConfigTabTab;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/modbus/ModbusConfigTab.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        Parent root = null;
        try {
            root = fxmlLoader.load();
            ModbusConfigTabTab.setContent(root);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        ModbusSlave modbusSlave = modbusMainModel.getSelectedSlave();

        int tab = modbusMainModel.getSelectedSlave().getModbusSlaveTabPaneModel().getSelectedTab();
        ModbusSlaveTabPanePane.getSelectionModel().select(tab);
    }

    @FXML
    private void switchTabClick(MouseEvent mouseEvent) {
        int tab = ModbusSlaveTabPanePane.getSelectionModel().getSelectedIndex();
        modbusMainModel.getSelectedSlave().getModbusSlaveTabPaneModel().setSelectedTab(tab);
    }

}

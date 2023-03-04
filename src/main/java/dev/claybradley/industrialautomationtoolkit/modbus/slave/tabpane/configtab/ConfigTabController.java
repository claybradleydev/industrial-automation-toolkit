package dev.claybradley.industrialautomationtoolkit.modbus.slave.tabpane.configtab;

import javafx.scene.layout.AnchorPane;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ConfigTabController {
    public AnchorPane ConfigTab;
    public AnchorPane ModbusConfigTab;
}

package dev.claybradley.industrialautomationtoolkit.modbus.displayformat;

import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMemoryArea;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class ModbusLabelMaker {

    ModbusDataLabelMaker modbusDataLabelMaker;
    ModbusAddressLabelMaker modbusAddressLabelMaker;

    public ModbusLabelMaker(){
        modbusAddressLabelMaker = new ModbusAddressLabelMaker();
        modbusDataLabelMaker = new ModbusDataLabelMaker();
    }


    public ArrayList<HBox> getHBoxes(boolean[] data, int address, ModbusMemoryArea modbusMemoryArea, ModbusDataFormat modbusDataFormat, ModbusAddressFormat modbusAddressFormat){
        ArrayList<HBox> hBoxes = new ArrayList<>();
        ArrayList<VBox> addressLabels = modbusAddressLabelMaker.format(address, data.length, modbusMemoryArea, modbusDataFormat, modbusAddressFormat);
        ArrayList<Label> dataLabels = modbusDataLabelMaker.format(data);
        for(int i = 0; i < addressLabels.size(); ++i){
            HBox hBox = getHBox();
            hBox.getChildren().addAll(addressLabels.get(i), dataLabels.get(i));
            hBoxes.add(hBox);
        }
        return hBoxes;
    }

    public ArrayList<HBox> getHBoxes(short[] data, int address, ModbusMemoryArea modbusMemoryArea, ModbusDataFormat modbusDataFormat, ModbusAddressFormat modbusAddressFormat){
        ArrayList<HBox> hBoxes = new ArrayList<>();
        ArrayList<VBox> addressLabels = modbusAddressLabelMaker.format(address, data.length, modbusMemoryArea, modbusDataFormat, modbusAddressFormat);
        ArrayList<Label> dataLabels = modbusDataLabelMaker.format(data, modbusDataFormat);
        for(int i = 0; i < addressLabels.size(); ++i){
            HBox hBox = getHBox();
            hBox.getChildren().addAll(addressLabels.get(i), dataLabels.get(i));
            hBoxes.add(hBox);
        }
        return hBoxes;
    }

    private HBox getHBox(){
        HBox hBox = new HBox();
        hBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        hBox.setMinWidth(Region.USE_COMPUTED_SIZE);
        hBox.setMaxWidth(Region.USE_COMPUTED_SIZE);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

}

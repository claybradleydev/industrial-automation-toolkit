package dev.claybradley.industrialautomationtoolkit.modbus.displayformat;

import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMemoryArea;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class ModbusDataLabelMaker {

    public static ArrayList<HBox> getRegisterLabels(int[] data, int address, ModbusDataFormat modbusDataFormat, ModbusMemoryArea modbusMemoryArea){
        switch (modbusDataFormat){
            case BINARY:
                return formatBinary(data, address);
            case DECIMAL:
                return formatDecimal(data, address);
            case HEX:
                return formatHex(data, address, modbusMemoryArea);
        }
        return null;
    }

    public static ArrayList<HBox> getBooleanLabels(boolean[] data, int address){
        ArrayList<HBox> dataList = new ArrayList<>();
        for(boolean value: data){
            HBox hBox = new HBox();
            hBox.getChildren().add(new Label(String.valueOf(value)));
            dataList.add(hBox);
        }
        return dataList;
    }

    private static ArrayList<HBox>  formatBinary(int[] data, int address){
        ArrayList<HBox> dataList = new ArrayList<>();
        for(int value: data){
            HBox hBox = new HBox();
            hBox.getChildren().add(new Label(Integer.toBinaryString(value)));
            dataList.add(hBox);
        }
        return dataList;
    }

    private static ArrayList<HBox>  formatDecimal(int[] data, int address){
        ArrayList<HBox> dataList = new ArrayList<>();
        for(int value: data){
            HBox hBox = new HBox();
            hBox.getChildren().add(new Label(String.valueOf(value)));
            dataList.add(hBox);
        }
        return dataList;
    }

    private static ArrayList<HBox> formatHex(int[] data, int address, ModbusMemoryArea modbusMemoryArea){
        ArrayList<HBox> dataList = new ArrayList<>();
        int addressValue = 0;
        int dataValue = 0;
        for(int i = 0; i < data.length; ++i) {
            if (modbusMemoryArea == ModbusMemoryArea.HOLDING_REGISTER) {
                addressValue = 40000 + address + i;
            } else{
                addressValue = 30000 + address + i;
            }
            dataValue = data[i];
            Label addressLabel = new Label(String.valueOf(addressValue));
            Label valueLabel = new Label(String.valueOf(dataValue));
            HBox hBox = new HBox();
            addressLabel.setPrefWidth(75);
            valueLabel.setPrefWidth(75);
            hBox.getChildren().addAll(addressLabel, valueLabel);
            dataList.add(hBox);
        }
        return dataList;
    }


}

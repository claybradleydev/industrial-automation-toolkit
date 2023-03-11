package dev.claybradley.industrialautomationtoolkit.modbus.displayformat;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import java.util.ArrayList;


public class ModbusDataLabelMaker {

    public ArrayList<Label> format(short[] data, ModbusDataFormat modbusDataFormat){
        switch (modbusDataFormat) {
            case BINARY:
                return formatBinary(data);
            case DECIMAL:
                return  formatDecimal(data);
            case HEX:
                return formatHex(data);
            case LONG:
                return formatLong(data);
            case LONG_SWAPPED:
                return formatLongSwapped(data);
            case FLOAT:
                return formatFloat(data);
            case FLOAT_SWAPPED:
                return formatFloatSwapped(data);
        }
        return null;
    }

    public ArrayList<Label>  format(boolean[] data){
        ArrayList<Label> dataList = new ArrayList<>();
        boolean dataValue = false;
        for(int i = 0; i < data.length; ++i) {

            dataValue = data[i];
            Label label = getValueLabel(String.valueOf(dataValue));

            dataList.add(label);
        }
        return dataList;
    }

    public ArrayList<Label>  formatBinary(short[] data){
        ArrayList<Label> dataList = new ArrayList<>();
        int dataValue = 0;
        for(int i = 0; i < data.length; ++i) {

            dataValue = data[i];
            Label label = getValueLabel(Integer.toBinaryString(dataValue));

            dataList.add(label);
        }
        return dataList;
    }

    public ArrayList<Label>  formatDecimal(short[] data){
        ArrayList<Label> dataList = new ArrayList<>();
        int dataValue = 0;
        for(int i = 0; i < data.length; ++i) {

            dataValue = data[i];
            Label label = getValueLabel(String.valueOf(dataValue));

            dataList.add(label);
        }
        return dataList;
    }

    public ArrayList<Label> formatHex(short[] data){
        ArrayList<Label> dataList = new ArrayList<>();
        int dataValue = 0;
        for(int i = 0; i < data.length; ++i) {

            dataValue = data[i];
            Label label = getValueLabel(Integer.toHexString(dataValue));

            dataList.add(label);
        }
        return dataList;
    }

    public ArrayList<Label> formatLong(short[] data){
        ArrayList<Label> dataList = new ArrayList<>();

        for(int i = 0; i < data.length; i = i + 2) {

            short dataValue1 = data[i];
            short dataValue2 = data[i + 1];
            int resultDataValue = (int) (dataValue2 << 16) | (dataValue1 & 0xFFFF);
            Label label = getValueLabel(String.valueOf(resultDataValue));

            dataList.add(label);
        }
        return dataList;
    }

    public ArrayList<Label> formatLongSwapped(short[] data){
        ArrayList<Label> dataList = new ArrayList<>();
        for(int i = 0; i < data.length; i = i + 2) {
            short dataValue1 = data[i];
            short dataValue2 = data[i + 1];
            int resultDataValue = (int) (dataValue1 << 16) | (dataValue2 & 0xFFFF);
            Label label = getValueLabel(String.valueOf(resultDataValue));
            dataList.add(label);
        }
        return dataList;
    }

    public ArrayList<Label> formatFloat(short[] data){
        ArrayList<Label> dataList = new ArrayList<>();

        for(int i = 0; i < data.length; i = i + 2) {

            short dataValue1 = data[i];
            short dataValue2 = data[i + 1];

            int combinedValue = (int) (dataValue2 << 16) | dataValue1;
            float resultDataValue = Float.intBitsToFloat(combinedValue);

            Label label = getValueLabel(String.valueOf(resultDataValue));

            dataList.add(label);
        }
        return dataList;
    }

    public ArrayList<Label> formatFloatSwapped(short[] data){
        ArrayList<Label> dataList = new ArrayList<>();

        for(int i = 0; i < data.length; i = i + 2) {

            short dataValue1 = data[i];
            short dataValue2 = data[i + 1];
            int combinedValue = (int) (dataValue1 << 16) | dataValue2;
            float resultDataValue = Float.intBitsToFloat(combinedValue);

            Label valueLabel = getValueLabel(String.valueOf(resultDataValue));

            dataList.add(valueLabel);
        }
        return dataList;
    }

    public Label getValueLabel(String value){
        Label label = new Label(value);
        label.setMaxWidth(9999);
        label.setMinWidth(Region.USE_COMPUTED_SIZE);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-font-family: monospace; -fx-font-size: 14; -fx-text-fill: white");
        HBox.setHgrow(label, Priority.ALWAYS);
        return label;
    }

}

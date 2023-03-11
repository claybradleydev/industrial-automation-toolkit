package dev.claybradley.industrialautomationtoolkit.modbus.displayformat;

import dev.claybradley.industrialautomationtoolkit.modbus.ModbusMemoryArea;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class ModbusAddressLabelMaker {

    public ArrayList<VBox> format(int address, int quantity, ModbusMemoryArea modbusMemoryArea, ModbusDataFormat modbusDataFormat, ModbusAddressFormat modbusAddressFormat){
        ArrayList<VBox> vBoxes = new ArrayList<>();
       if(modbusMemoryArea == ModbusMemoryArea.COIL || modbusMemoryArea == ModbusMemoryArea.DISCRETE_INPUT || modbusDataFormat == ModbusDataFormat.BINARY || modbusDataFormat == ModbusDataFormat.DECIMAL || modbusDataFormat == ModbusDataFormat.HEX){
           vBoxes = format16BitLabels(address, quantity, modbusMemoryArea, modbusAddressFormat);
       } else if(modbusDataFormat == ModbusDataFormat.LONG || modbusDataFormat == ModbusDataFormat.LONG_SWAPPED || modbusDataFormat == ModbusDataFormat.FLOAT || modbusDataFormat == ModbusDataFormat.FLOAT_SWAPPED){
           vBoxes = format32BitLabels(address, quantity, modbusMemoryArea, modbusAddressFormat);
       }
        return vBoxes;
    }

    private ArrayList<VBox> format16BitLabels(int address, int quantity, ModbusMemoryArea modbusMemoryArea, ModbusAddressFormat modbusAddressFormat){
        ArrayList<VBox> dataList = new ArrayList<>();
        for(int i = address; i < quantity + address; ++i) {
            String addressString = getFormattedAddressValue(i, modbusMemoryArea, modbusAddressFormat);
            Label label = getAddressLabel(addressString);
            VBox vBox = getVBox();
            vBox.getChildren().add(label);
            dataList.add(vBox);
        }
        return dataList;
    }

    private ArrayList<VBox> format32BitLabels(int address, int quantity, ModbusMemoryArea modbusMemoryArea, ModbusAddressFormat modbusAddressFormat){
        ArrayList<VBox> dataList = new ArrayList<>();

        for(int i = address; i < quantity + address; i += 2) {
            String addressString1 = getFormattedAddressValue(i, modbusMemoryArea, modbusAddressFormat);
            String addressString2 = getFormattedAddressValue(i + 1, modbusMemoryArea, modbusAddressFormat);
            Label label1 = getAddressLabel(addressString1);
            Label label2 = getAddressLabel(addressString2);
            VBox vBox = getVBox();
            vBox.getChildren().addAll(label1, label2);
            dataList.add(vBox);
        }
        return dataList;
    }

    private String getFormattedAddressValue(int address, ModbusMemoryArea modbusMemoryArea, ModbusAddressFormat modbusAddressFormat){
        int addressValue = address + getModbusMemoryAreaAdder(modbusMemoryArea, modbusAddressFormat);
        String formattedAddress = "";
        if(modbusAddressFormat == ModbusAddressFormat.FIVE_DIGIT || modbusAddressFormat == ModbusAddressFormat.SIX_DIGIT){
            String addressString = String.valueOf(addressValue);
            formattedAddress = addressString;
        } else if (modbusAddressFormat == ModbusAddressFormat.FIVE_DIGIT_HEX || modbusAddressFormat == ModbusAddressFormat.SIX_DIGIT_HEX){
            String addressString = Integer.toHexString(addressValue);
            formattedAddress = addressString;
        }
        if(modbusAddressFormat == ModbusAddressFormat.FIVE_DIGIT || modbusAddressFormat == ModbusAddressFormat.FIVE_DIGIT_HEX){
            formattedAddress = StringUtils.leftPad(formattedAddress, 5, "0");
        }
        if(modbusAddressFormat == ModbusAddressFormat.SIX_DIGIT || modbusAddressFormat == ModbusAddressFormat.SIX_DIGIT_HEX){
            formattedAddress = StringUtils.leftPad(formattedAddress, 6, "0");
        }
        return formattedAddress;
    }

    private int getModbusMemoryAreaAdder(ModbusMemoryArea modbusMemoryArea, ModbusAddressFormat modbusAddressFormat){
        int result = 0;
        switch(modbusMemoryArea){
            case COIL: {
                result =  0;
            }
            break;

            case DISCRETE_INPUT: {
                if(modbusAddressFormat == ModbusAddressFormat.FIVE_DIGIT || modbusAddressFormat == ModbusAddressFormat.FIVE_DIGIT_HEX){
                    result = 10000;
                } else if(modbusAddressFormat == ModbusAddressFormat.SIX_DIGIT || modbusAddressFormat == ModbusAddressFormat.SIX_DIGIT_HEX) {
                    result = 100000;
                }
            }
            break;

            case HOLDING_REGISTER: {
                if(modbusAddressFormat == ModbusAddressFormat.FIVE_DIGIT || modbusAddressFormat == ModbusAddressFormat.FIVE_DIGIT_HEX){
                    result = 40000;
                } else if(modbusAddressFormat == ModbusAddressFormat.SIX_DIGIT || modbusAddressFormat == ModbusAddressFormat.SIX_DIGIT_HEX) {
                    result = 400000;
                }
            }
            break;

            case INPUT_REGISTER:  {
                if(modbusAddressFormat == ModbusAddressFormat.FIVE_DIGIT || modbusAddressFormat == ModbusAddressFormat.FIVE_DIGIT_HEX){
                    result = 30000;
                } else if(modbusAddressFormat == ModbusAddressFormat.SIX_DIGIT || modbusAddressFormat == ModbusAddressFormat.SIX_DIGIT_HEX) {
                    result = 300000;
                }
            }
            break;
        }
        return result;
    }

    private Label getAddressLabel(String addressValue){
        Label label = new Label(addressValue + ":");
        label.setPrefWidth(75);
        label.setAlignment(Pos.BASELINE_RIGHT);
        return label;
    }

    private VBox getVBox(){
        VBox vBox = new VBox();
        vBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        vBox.setMinWidth(Region.USE_COMPUTED_SIZE);
        vBox.setMaxWidth(Region.USE_COMPUTED_SIZE);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(0);
        return vBox;
    }

}

package dev.claybradley.industrialautomationtoolkit.modbus.displayformat;

import java.util.ArrayList;

public class ModbusDataFormatter {

    public static ArrayList<String> formatData(int[] data, ModbusDataFormat modbusDataFormat){
        switch (modbusDataFormat){
            case BINARY:
                return formatBinary(data);
            case DECIMAL:
                return formatDecimal(data);
            case HEX:
                return formatHex(data);

        }
        return null;
    }

    private static ArrayList<String>  formatBinary(int[] data){
        ArrayList<String> dataList = new ArrayList<>();
        for(int value: data){
            dataList.add(Integer.toBinaryString(value));
        }
        return dataList;
    }

    private static ArrayList<String>  formatDecimal(int[] data){
        ArrayList<String> dataList = new ArrayList<>();
        for(int value: data){
            dataList.add(String.valueOf(value));
        }
        return dataList;
    }

    private static ArrayList<String> formatHex(int[] data){
        ArrayList<String> dataList = new ArrayList<>();
        for(int value: data){
            dataList.add(Integer.toHexString(value));
        }
        return dataList;
    }



}

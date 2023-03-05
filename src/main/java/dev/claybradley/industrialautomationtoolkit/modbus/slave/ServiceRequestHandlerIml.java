package dev.claybradley.industrialautomationtoolkit.modbus.slave;

import com.digitalpetri.modbus.requests.*;
import com.digitalpetri.modbus.responses.*;
import com.digitalpetri.modbus.slave.ServiceRequestHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.ReferenceCountUtil;

public class ServiceRequestHandlerIml implements ServiceRequestHandler {
    private final ModbusSlaveMemory modbusSlaveMemory;
    public ServiceRequestHandlerIml(){
        this.modbusSlaveMemory = new ModbusSlaveMemory();
    }

    @Override
    public void onReadHoldingRegisters(ServiceRequest<ReadHoldingRegistersRequest, ReadHoldingRegistersResponse> service) {
        String clientRemoteAddress = service.getChannel().remoteAddress().toString();
        String clientIp = clientRemoteAddress.replaceAll(".*/(.*):.*", "$1");
        String clientPort = clientRemoteAddress.replaceAll(".*:(.*)", "$1");
        ReadHoldingRegistersRequest request = service.getRequest();

        int address = request.getAddress();
        int quantity = request.getQuantity();

        ByteBuf registers = PooledByteBufAllocator.DEFAULT.buffer(quantity);

        int [] holdingRegisters = modbusSlaveMemory.getHoldingRegisters(address, quantity);

        for (int i = 0; i < quantity; ++i) {
            registers.writeShort(holdingRegisters[i]);
        }

        service.sendResponse(new ReadHoldingRegistersResponse(registers));

        ReferenceCountUtil.release(request);
    }

    @Override
    public void onReadInputRegisters(ServiceRequest<ReadInputRegistersRequest, ReadInputRegistersResponse> service) {
        String clientRemoteAddress = service.getChannel().remoteAddress().toString();
        String clientIp = clientRemoteAddress.replaceAll(".*/(.*):.*", "$1");
        String clientPort = clientRemoteAddress.replaceAll(".*:(.*)", "$1");
        ReadInputRegistersRequest request = service.getRequest();

        int address = request.getAddress();
        int quantity = request.getQuantity();

        ByteBuf registers = PooledByteBufAllocator.DEFAULT.buffer(quantity);

        int [] inputRegisters = modbusSlaveMemory.getInputRegisters(address, quantity);

        for (int i = address; i < quantity; i++) {
            registers.writeShort(inputRegisters[i]);
        }

        service.sendResponse(new ReadInputRegistersResponse(registers));

        ReferenceCountUtil.release(request);
    }

    @Override
    public void onReadCoils(ServiceRequest<ReadCoilsRequest, ReadCoilsResponse> service) {
        String clientRemoteAddress = service.getChannel().remoteAddress().toString();
        String clientIp = clientRemoteAddress.replaceAll(".*/(.*):.*", "$1");
        String clientPort = clientRemoteAddress.replaceAll(".*:(.*)", "$1");
        ReadCoilsRequest request = service.getRequest();

        int address = request.getAddress();
        int quantity = request.getQuantity();

        ByteBuf coils = PooledByteBufAllocator.DEFAULT.buffer(quantity);

        boolean [] discreteCoils= modbusSlaveMemory.getCoils(address, quantity);

        for (int i = address; i < quantity; i++) {
            coils.writeBoolean(discreteCoils[i]);
        }

        service.sendResponse(new ReadCoilsResponse(coils));

        ReferenceCountUtil.release(request);
    }
    @Override
    public void onReadDiscreteInputs(ServiceRequest<ReadDiscreteInputsRequest, ReadDiscreteInputsResponse> service) {
        String clientRemoteAddress = service.getChannel().remoteAddress().toString();
        String clientIp = clientRemoteAddress.replaceAll(".*/(.*):.*", "$1");
        String clientPort = clientRemoteAddress.replaceAll(".*:(.*)", "$1");
        ReadDiscreteInputsRequest request = service.getRequest();

        int address = request.getAddress();
        int quantity = request.getQuantity();

        ByteBuf inputs = PooledByteBufAllocator.DEFAULT.buffer(quantity);

        boolean [] discreteInputs= modbusSlaveMemory.getDiscreteInputs(address, quantity);

        for (int i = address; i < quantity; i++) {
            inputs.writeBoolean(discreteInputs[i]);
        }

        service.sendResponse(new ReadDiscreteInputsResponse(inputs));

        ReferenceCountUtil.release(request);
    }
    @Override
    public void onWriteSingleRegister(ServiceRequest<WriteSingleRegisterRequest, WriteSingleRegisterResponse> service) {
        String clientRemoteAddress = service.getChannel().remoteAddress().toString();
        String clientIp = clientRemoteAddress.replaceAll(".*/(.*):.*", "$1");
        String clientPort = clientRemoteAddress.replaceAll(".*:(.*)", "$1");
        WriteSingleRegisterRequest request = service.getRequest();

        int address = request.getAddress();
        int value = request.getValue();

        modbusSlaveMemory.setHoldingRegister(address, value);
        service.sendResponse(new WriteSingleRegisterResponse(address, value));

        ReferenceCountUtil.release(request);
    }

    public ModbusSlaveMemory getModbusSlaveMemory() {
        return modbusSlaveMemory;
    }


}

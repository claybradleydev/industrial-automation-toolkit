package dev.claybradley.industrialscanner.modbus.slave;

import com.digitalpetri.modbus.requests.ReadHoldingRegistersRequest;
import com.digitalpetri.modbus.requests.WriteSingleRegisterRequest;
import com.digitalpetri.modbus.responses.ReadHoldingRegistersResponse;
import com.digitalpetri.modbus.responses.WriteSingleRegisterResponse;
import com.digitalpetri.modbus.slave.ServiceRequestHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.ReferenceCountUtil;
import org.springframework.stereotype.Component;

@Component
public class ServiceRequestHandlerIml implements ServiceRequestHandler {

    private final ModbusSlaveMemoryService modbusSlaveMemoryService;

    public ServiceRequestHandlerIml(ModbusSlaveMemoryService modbusSlaveMemoryService){
        this.modbusSlaveMemoryService = modbusSlaveMemoryService;
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

        int [] holdingRegisters = modbusSlaveMemoryService.getHoldingRegisters();

        for (int i = address; i < quantity; i++) {
            registers.writeShort(holdingRegisters[i]);
        }

        service.sendResponse(new ReadHoldingRegistersResponse(registers));

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

        modbusSlaveMemoryService.setHoldingRegister(address, value);
        service.sendResponse(new WriteSingleRegisterResponse(address, value));

        ReferenceCountUtil.release(request);
    }
}
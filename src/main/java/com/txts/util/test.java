package com.txts.util;

import org.springframework.beans.factory.annotation.Autowired;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.WriteRegisterRequest;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;

/**
 * @description 
 * 
 * @author lfy
 * @time 2018年9月26日-下午2:18:37
 */
public class test {
	@Autowired
	public static void main(String[] args)   {
		try {
			System.out.println("init");
			IpParameters params = new IpParameters();
			params.setHost("10.0.0.1");
			params.setPort(502);
			ModbusFactory modbusFactory = new ModbusFactory();
			// modbusFactory.createRtuMaster(wrapper); //RTU 协议
			// modbusFactory.createUdpMaster(params);//UDP 协议
			// modbusFactory.createAsciiMaster(wrapper);//ASCII 协议
			ModbusMaster master = modbusFactory.createTcpMaster(params, true);// TCP 协议
			master.init();
			BaseLocator<Number> locator = BaseLocator.holdingRegister(1, 11, DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED);
			master.setValue(locator, 1);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}

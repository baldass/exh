package com.txts.util;
/**
 * modbus4读取通讯工具类
 * 
 * @author 40857
 * @dependencies modbus4j-3.0.3.jar
 * @website https://github.com/infiniteautomation/modbus4j
 *
 */

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;

public class Modbus4jReadUtils {
	/**
	 * 生产工厂
	 */
	public static ModbusFactory modbusFactory;
	static {
		if (modbusFactory == null) {
			modbusFactory = new ModbusFactory();
		}
	}

	/**
	 * 获取master
	 * 
	 * @return
	 * @throws ModbusInitException
	 */
	public static ModbusMaster getMaster() throws ModbusInitException {
		IpParameters params = new IpParameters();
//		10.0.0.1
		params.setHost("localhost");
		params.setPort(502);

		// modbusFactory.createRtuMaster(wrapper); //RTU 协议
		// modbusFactory.createUdpMaster(params);//UDP 协议
		// modbusFactory.createAsciiMaster(wrapper);//ASCII 协议
		ModbusMaster master = modbusFactory.createTcpMaster(params, false);// TCP 协议
		master.init();
		return master;
	}
	/**
	 * 设置TCP Master的ip与端口号
	 * @param ip
	 * @param port
	 * @return
	 */
	public static ModbusMaster getMaster(String ip, int port) {
		// 🚐设备Modbus TCP的ip与端口,如果端口不设置则默认为502
		IpParameters params = new IpParameters();
		params.setHost(ip);
		if (502 != port)
			params.setPort(port);
		// 参数1:IP和端口信息 参数二:保持连接激活
		ModbusMaster tcpMaster = modbusFactory.createTcpMaster(params, true);
		try {
			tcpMaster.init();
			System.out.println("TCP Master 初始化成功!");
			return tcpMaster;
		} catch (ModbusInitException e) {
			System.out.println("TCP Master 初始化失败!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取[01 Coil Status 0x]类型 开关数据
	 * 
	 * @param slaveId
	 * @param offset
	 * @return 读取值
	 * @throws ModbusTransportException
	 * @throws ErrorResponseException
	 * @throws ModbusInitException
	 */
	public static Boolean readCoilStatus(int slaveId, int offset)
			throws ModbusTransportException, ErrorResponseException, ModbusInitException {
		// 01 Coil Status
		BaseLocator<Boolean> loc = BaseLocator.coilStatus(slaveId, offset);
		Boolean value = getMaster().getValue(loc);
		return value;
	}

	/**
	 * 读取[02 Input Status 1x]类型 开关数据
	 * 
	 * @param slaveId
	 * @param offset
	 * @return
	 * @throws ModbusTransportException
	 * @throws ErrorResponseException
	 * @throws ModbusInitException
	 */
	public static Boolean readInputStatus(int slaveId, int offset)
			throws ModbusTransportException, ErrorResponseException, ModbusInitException {
		// 02 Input Status
		BaseLocator<Boolean> loc = BaseLocator.inputStatus(slaveId, offset);
		Boolean value = getMaster().getValue(loc);
		return value;
	}

	/**
	 * 读取[03 Holding Register类型 2x]模拟量数据
	 * 
	 * @param slaveId
	 * @param offset
	 * @param dataType
	 * @return
	 * @throws ModbusTransportException
	 * @throws ErrorResponseException
	 * @throws ModbusInitException
	 */
	public static Number readHoldingRegister(int slaveId, int offset, int dataType)
			throws ModbusTransportException, ErrorResponseException, ModbusInitException {
		// 03 Holding Register类型数据读取
		BaseLocator<Number> loc = BaseLocator.holdingRegister(slaveId, offset, dataType);
		Number value = getMaster().getValue(loc);
		return value;
	}

	/**
	 * 读取[04 Input Registers 3x]类型 模拟量数据
	 * 
	 * @param slaveId
	 * @param offset
	 * @param dataType
	 * @return
	 * @throws ModbusTransportException
	 * @throws ErrorResponseException
	 * @throws ModbusInitException
	 */
	public static Number readInputRegisters(int slaveId, int offset, int dataType)
			throws ModbusTransportException, ErrorResponseException, ModbusInitException {
		// 04 Input Registers类型数据读取
		BaseLocator<Number> loc = BaseLocator.inputRegister(slaveId, offset, dataType);
		Number value = getMaster().getValue(loc);
		return value;
	}

	/**
	 * 批量读取
	 * 
	 * @throws ModbusTransportException
	 * @throws ErrorResponseException
	 * @throws ModbusInitException
	 
	public static void batchRead() throws ModbusTransportException, ErrorResponseException, ModbusInitException {
		BatchRead<Integer> batch = new BatchRead<Integer>();

		batch.addLocator(0, BaseLocator.holdingRegister(1, 1, DataType.FOUR_BYTE_FLOAT));
		batch.addLocator(1, BaseLocator.inputStatus(1, 0));

		ModbusMaster master = getMaster();

		batch.setContiguousRequests(false);
		BatchResults<Integer> results = master.send(batch);
		System.out.println(results.getValue(0));
		System.out.println(results.getValue(1));
	}

	public static void main(String[] args) {
		try {
			// 01测试 读取ModbusSlave中 Slave Definition中Function选项为01 CoilStatus的数据
//			Boolean v011 = readCoilStatus(1, 0);//读第0行
//			Boolean v012 = readCoilStatus(1, 1);//读第1行
//			Boolean v013 = readCoilStatus(1, 6);//读第6行
//			System.out.println("v011:" + v011);
//			System.out.println("v012:" + v012);
//			System.out.println("v013:" + v013);
			// 02测试
//			 Boolean v021 = readInputStatus(1, 0);
//			 Boolean v022 = readInputStatus(1, 1);
//			 Boolean v023 = readInputStatus(1, 2);
//			 System.out.println("v021:" + v021);
//			 System.out.println("v022:" + v022);
//			 System.out.println("v023:" + v023);			
			 // 03测试
//			 Number v031 = readHoldingRegister(1, 0, DataType.FOUR_BYTE_FLOAT);// 注意,float
//			 Number v032 = readHoldingRegister(1, 2, DataType.FOUR_BYTE_FLOAT);// 同上
//			 System.out.println("v031:" + v031);
//			 System.out.println("v032:" + v032);

//			 DataType.FOUR_BYTE_INT_SIGNED_SWAPPED :Signed Unsigned 			 
			 // 04测试 DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED
			 Number v041 = readInputRegisters(1, 1, DataType.FOUR_BYTE_FLOAT);//
//			 Number v042 = readInputRegisters(1, 3, DataType.FOUR_BYTE_FLOAT);//
			 System.out.println("v041:" + v041);
//			 System.out.println("v042:" + v042);
//			for (int i = 0; i < 100; i++) {
//				Number result = readInputRegisters(1, i, DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED);
//				System.out.println(result);
//			}
			
			 // 批量读取
//			 batchRead();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	*/
}

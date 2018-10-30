package com.txts.util.modbus;
/**
 * modbus控制类
 * 实现获取期望值后可以继续获取的问题
 * @author 40857
 * @time 2018.10.25
 *
 */

import java.net.InetAddress;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.net.TCPMasterConnection;

@Component
public class ModbusUtils {
	private static final Logger logger = Logger.getLogger(ModbusUtils.class);

	private static String ip = "10.0.0.1";// IP地址
	private static int port = 502;// 端口号
	private static int slaveId = 1;// 从站地址
	private static long interval = 1000L;// 间隔时间

	private static int maxLoop = 120;// 最大循环数

	/**
	 * 4x地址的写入操作 将数据写入到modbus
	 * 
	 * @param name
	 * @param address
	 * @param value
	 * @return
	 */
	public static boolean modbus4xWrite(String name, int address, int value) {
		logger.info("开始启动" + name + ".");
		TCPMasterConnection connection = null;
		try {
			InetAddress inet = InetAddress.getByName(ip);
			connection = new TCPMasterConnection(inet);
			connection.setPort(port);
			connection.connect();

			ModbusTCPTransaction trans = new ModbusTCPTransaction(connection);
			UnityRegister register = new UnityRegister(value);
			WriteSingleRegisterRequest request = new WriteSingleRegisterRequest(address, register);
			request.setUnitID(slaveId);
			trans.setRequest(request);
			System.out.println("ModbusSlave: FC" + request.getFunctionCode() + " ref=" + request.getReference()
					+ " value=" + register.getValue());
			trans.execute();
			logger.info("启动" + name + "完成,未出现异常.");
			return true;
		} catch (Exception e) {
			logger.info("启动" + name + "未成功,出现异常.");
			e.printStackTrace();
			return false;
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	/**
	 * 4x地址的读取操作
	 * 
	 * @param name
	 * @param address
	 * @param value
	 * @return 返回读取到的值
	 */
	public static boolean modbus4xWait(String name, int address, int value) {
		try {
			logger.info("开始轮询" + name + "完成操作.");
			boolean flag = true;
			int counter = 0;
			while (flag) {
				if (counter >= maxLoop) {
					throw new Exception(name + "读取等待时间过久.");
				}
				counter++;
				int reslut = modbus4xWait(address);
				logger.info("正在读取" + name + "信息,第" + counter + "次读取,读取值为:" + reslut + ".,期望值为:" + value + ".");

				if (reslut == -1) {
					// modbus4xWait(addr)方法报异常
					logger.info("di" + counter + "次轮询出现错误,继续轮询.");
				} else if (value == reslut) {
					// 获取到期望值
					logger.info(name + "完成操作,结束轮询.");
					return true;
				}
				Thread.sleep(interval);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static int modbus4xWait(int address) {
		TCPMasterConnection connection = null;
		try {
			InetAddress inet = InetAddress.getByName(ip);
			connection = new TCPMasterConnection(inet);
			connection.setPort(port);
			connection.connect();

			ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(address, 1);
			request.setUnitID(slaveId);

			ModbusTCPTransaction trans = new ModbusTCPTransaction(request);
			trans.setRequest(request);
			trans.execute();
			ReadMultipleRegistersResponse result = (ReadMultipleRegistersResponse) trans.getResponse();

			return result.getRegisterValue(0);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	public static void main(String[] args) {
		modbus4xWrite("机械手4", 31, 1);

		//modbus4xWait("机械手4", 17, 1);
	}
}

package com.txts.util.device;

import java.util.HashMap;
import java.util.Map;

import com.txts.task.CollectUtil;
import com.txts.util.modbus.ModbusUtil;

/**
 * @description
 * 
 * @author lfy
 * @time 2018年10月11日-下午10:31:58
 */
public class DeviceCollectRunnable implements Runnable {

	// private static final Logger logger =
	// Logger.getLogger(DeviceCollect.class);
	private static String ip = "127.0.0.1";// IP地址
	private static int port = 502;// 端口 默认502
	private static int slave = 1;// 从站
	private static Long waitMillis = 1000L;// 默认等待时间

	private CollectUtil collectUtil;
	private String deviceName;// 设备
	private int deviceStartAddress;// 启动地址
	private int deviceStartValue;// 启动值
	private int deviceStopAddress;// 关闭地址
	private int deviceStopValue;// 关闭值
	private int deviceErrAddress;// 异常地址
	private int deviceErrValue;// 异常值
	private Map<String, Object> stateMap;

	public DeviceCollectRunnable(CollectUtil collectUtil, String deviceName, int deviceStartAddress,
			int deviceStartValue, int deviceStopAddress, int deviceStopValue, Map<String, Object> stateMap) {
		super();
		this.collectUtil = collectUtil;
		this.deviceName = deviceName;
		this.deviceStartAddress = deviceStartAddress;
		this.deviceStartValue = deviceStartValue;
		this.deviceStopAddress = deviceStopAddress;
		this.deviceStopValue = deviceStopValue;
		this.stateMap = stateMap;
		stateMap.put("device_name", deviceName);
		
	}

	public void run() {
		Long stmp = 0L;
		while (true) {
			// 监听启动
			while (true) {
				try {
					Thread.sleep(waitMillis);
					if(isRrr()){
						//异常的时候
						continue;
					}
					int read = ModbusUtil.readRegister(ip, port, slave, deviceStartAddress);
					if (read == deviceStartValue) {
						// 监听到设备启动的时候
						// 记录时间 跳出启动监听 进入关闭监听
						stmp = System.currentTimeMillis();
						stateMap.put("device_status","1");
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// 循环判断是否关闭
			while (true) {
				try {
					Thread.sleep(waitMillis);
					if(isRrr()){
						//异常的时候
						continue;
					}
					int read = ModbusUtil.readRegister(ip, port, slave, deviceStopAddress);
					if (read == deviceStopValue) {
						// 监听到设备停止的时候 记录下 当前执行时间 单位秒
						double time = (System.currentTimeMillis() - stmp) / 1000.0;
						// 插入数据库
						Map<String, Object> m = new HashMap<>();
						m.put("eb_device_name", deviceName);// 执行设备
						m.put("eb_device_time", time);// 执行时间
						stateMap.put("device_status","2");
						collectUtil.insertBeat(m);
						// 关闭的时候 修改数据库的值 然后从头循环 继续监听启动
						continue;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
	/**
	 * 是否正常
	 * 
	 * @author lfy
	 * @time 2018年10月11日-下午11:44:00
	 * @return
	 */
	private boolean isRrr() {
		int read = ModbusUtil.readRegister(ip, port, slave, deviceErrAddress);
		if (read == deviceErrValue) {
			//异常的时候
			stateMap.put("device_status","0");
			return true;
		}
		return false;

	}
}

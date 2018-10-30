package com.txts.util.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.txts.task.HardwareHelper;
import com.txts.util.modbus.ModbusUtils;

/**
 * 机械手4 控制类 控制流程 抓料->向AGV小车放料->向料盘储位放料盘->结束
 * 
 * @author 40857
 *
 */
@Component
public class Extraman4  {
	private static final String TAG = "4号机械手";
	public static final int ON_START = 0;// 准备开始(停止)
	public static final int FEED_A = 1;// 向料盘A放料
	public static final int FEED_B = 2;// 向料盘B放料
	public static final int END_AGV = 3;// 完成向agv放料
	public static final int END_TRAY = 4;// 完成向料盘储位放料

	private static int state;
	private static int fireAddress;// 机械手运行 modbus地址
	private static int waitAddress;// 监听机械手结束modbus地址
	private static int aValue;// 执行向料盘储位A放料的modbus期望值
	private static int bValue;// 执行向料盘储位B放料的modbus期望值
	private static int endValue;// 机械手动作结束时的modbus期望值
	private static int clearValue;// 重置modbus值
	@Autowired
	private static ModbusUtils mh;

	/**
	 * 设备4初始化
	 * 
	 * @param h
	 * @return
	 */
	public static boolean init() {
		try {
			fireAddress = 35;
			waitAddress = 17;
			aValue = 2;
			bValue = 1;
			clearValue = 0;
			
			mh.modbus4xWrite("机械手4等待点重置", 17, 0);// 机械手2 完成点判断 归零
			state = ON_START;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void feedA() throws InterruptedException {
		state = FEED_A;
		mh.modbus4xWrite(TAG, fireAddress, aValue);
		Thread.sleep(2000);
		mh.modbus4xWrite(TAG, fireAddress, clearValue);
		state = END_AGV;
		mh.modbus4xWait(TAG, waitAddress, endValue);
		state = END_TRAY;
		mh.modbus4xWait(TAG, waitAddress, endValue);
		state = ON_START;
	}

	public void feedB() throws InterruptedException {
		state = FEED_A;
		mh.modbus4xWrite(TAG, fireAddress, bValue);
		Thread.sleep(2000);
		mh.modbus4xWrite(TAG, fireAddress, clearValue);
		state = END_AGV;
		mh.modbus4xWait(TAG, waitAddress, endValue);
		state = END_TRAY;
		mh.modbus4xWait(TAG, waitAddress, endValue);
		state = ON_START;
	}
	/**
	 * 获取当前机械手状态
	 * @return
	 */
	public static int getState() {
		return state;
	}
	
}

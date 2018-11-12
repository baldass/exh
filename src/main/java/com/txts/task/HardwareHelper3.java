package com.txts.task;

import org.jboss.logging.Logger;

/**
 * 连续取料操作类
 * 添加一个总的管控类,动态更新各个线程状态
 * @purpose 解决一个线程报错整个项目必须重启问题
 * @way 每个线程独立出一个类,并且使用属性表示状态
 * @purpose 添加各个设备的控制接口
 * @way 
 * @author 40857
 *
 */
public class HardwareHelper3 {
	public static String currentOrder = "";// 当前订单
	public static boolean isRun = false;// 是否正在运行
	private static final Logger logger = Logger.getLogger(HardwareHelper.class);
	
}

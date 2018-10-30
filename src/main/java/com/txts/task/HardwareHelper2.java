package com.txts.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.internal.metadata.facets.Validatable;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.txts.util.agv.AgvUtil;
import com.txts.util.code.CodeUtil;
import com.txts.util.device.DeviceCollectUtil;
import com.txts.util.modbus.ModbusUtil;
import com.txts.util.rfid.RfidLoopRead;
import com.txts.util.rfid.RfidUtil;
import com.txts.util.visio.VisioUtil;

/**
 * 连续取料操作类 先查询出计划数量,按计划数量循环取原料 增加料盘储位静态变量
 * 
 * @author 40857
 *
 */
public class HardwareHelper2 {
	public static String currentOrder = "";// 当前订单
	public static boolean isRun = false;// 是否正在运行
	private static final Logger logger = Logger.getLogger(HardwareHelper.class);
	private static String ip = "10.0.0.1";// IP地址
	private static int port = 502;// 端口 默认502
	private static int slave = 1;// 从站
	private static Long waitMillis = 1000L;// 默认等待时间

	private static int maxWait = 120;// 最大等待2分钟
	private static int currentSl = 0;// 计划数量
	private static int endsl = 0;// 已完成

	private static int trueSl;// 从立体库实际取出的数量
	private static boolean warehouseCur = true;// 立体库是否可以去原料库取料 true:可以取料 false:不可取
	private static boolean warehouseStorage = false;// 立体库出口的料盘储位是否有原料
	private static boolean[] lp = { false, false };// 包装料盘储位上是否有料盘

	private static boolean whState;// 立体库状态 true:运行状态 false:等待状态
	private static boolean rbt4State;// 机械手4运行状态 true:运行状态 false:等待状态
	/**
	 * agv小车运行指示器 {@value}0:在取料处等待取料完成 {@value}1:在取料处取料完成可以想上料处走
	 * {@value}2:在上料处等待上料完成 {@value}3:在上料处可以向取料处走
	 */
	private static int agvDirect;
	private static int agvState;// agv小车状态 0:在取料处 1:在上料处 2:运行中

	@Autowired
	private WarehouseHelper wh;
	@Autowired
	private RfidUtil ru;
	@Autowired
	private CollectUtil collectUtil;
	@Autowired
	DeviceCollectUtil devUtil;
	static int i = 0;
	@Autowired
	AgvUtil agv;// agv 串口
	@Autowired
	VisioUtil vu;// 视觉接收
	@Autowired
	CodeUtil c;// 条码 串口
	@Autowired
	static RfidLoopRead rfid = new RfidLoopRead();

	public static void main(String[] args) throws Exception {
		HardwareHelper2 h=new HardwareHelper2();
		h.modbus4xWrite("机械手1-指令1", 31, 1);// 启动机械手1 抓料
		Thread.sleep(1000);// 等待5s
		h.modbus4xWrite("机械手1-指令1", 31, 0);// 启动机械手1 抓料
		h.modbus4xWait("机械手1-指令1", 11, 1);// 等待机械手从agv抓料完成
		h.modbus4xWrite("机械手1-指令1", 11, 0);
		Thread.sleep(7000);// 等待7秒再去串口读取
		System.out.println(">>>>>>>>>>>>>>>>>>>第二次读取");
		h.modbus4xWait("机械手1-指令1", 11, 1);// 等待机械手1完成 转圈完成
		h.modbus4xWrite("机械手1-指令1", 11, 0);// 启动机械手1关闭信号重置
		
	}

	public boolean start(int sl, String order) throws Exception {

		HardwareHelper2 h = this;
		isRun = true;
		currentOrder = order;
		endsl = 0;
		currentSl = sl;
		trueSl = 0;

		whState = false;
		rbt4State = true;
		agvDirect = 2;
		agvState = 0;

		if (wh == null)
			wh = new WarehouseHelper();

		// 循环取原料直到取完计划的数量
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (trueSl < currentSl) {
					// 立体库出库
					try {
						if (warehouseCur) {// 判断是否可以取料
							Thread.sleep(2000L);// 不可取的话等待2s重新验证
							continue;
						}
						if (warehouseStorage) {
							// 等待立体库出口的料盘储位空出来
							Thread.sleep(2000L);
							continue;
						}
						if (whState) {
							// 立体题库机械手是否正在运行中
							Thread.sleep(2000L);
							continue;
						}
						Integer outValue = getOneOut();// 出库位置指令
						whState = true;
						wh.modbus4xWait("等待立体库可出库", 37, 1);
						// 立体库出库
						wh.modbus4xWrite("立体库可出库", 38, outValue);
						Thread.sleep(5000);
						// 设置立体库出库为原料占据状态
						warehouseStorage = true;
						// 实际取出数量+1
						trueSl++;
						// 立体库出库
						wh.modbus4xWait("等待立体库出库完成", 37, 1);
						whState = false;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
		// 控制机械手4抓取物料至agv小车
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					h.modbus4xWrite("机械手4等待点重置", 17, 0);// 机械手2 完成点判断 归零
					while (endsl < currentSl) {
						// 当立体库出口有料时运行 抓取值agv,并移动料盘
						while (warehouseStorage) {
							// 查询包装的料盘储位状态
							int value = 2;
							if (!lp[0]) {// 如果料盘储位1为空
								value = 2;
							} else if (!lp[0]) {// 如果料盘储位2为空
								value = 1;
							} else {// 料盘储位均不为空
								// 停止立体库取料,并等待
								warehouseCur = true;
								Thread.sleep(1000L);
								continue;
							}
							// 如果agv小车不在取料处或者 处于不可以向上料处走的状态 则等待2s重新运行
							if (agvState != 0 || agvDirect != 0) {
								Thread.sleep(1000L);
								continue;
							}
							// 机械手开始抓取原料
							h.modbus4xWrite("机械手4", 35, value);
							Thread.sleep(5000L);
							h.modbus4xWrite("机械手4", 35, 0);
							// 机械手4抓取至agv小车
							h.modbus4xWait("机械手4", 17, 1);
							// 设置立体库可以抓料
							warehouseCur = false;
							// 允许agv小车从取料处向上料处运行
							agvDirect = 1;
							Thread.sleep(2000L);
							h.modbus4xWait("机械手4", 17, 1);
							warehouseStorage = false;
						}
						// 等待2s
						Thread.sleep(2000L);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
		// agv从取料处向上料处运原料
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (endsl < currentSl) {

						if (agvState == 0 && agvDirect == 1) {
							agvState = 2;
							agv.start1();
							h.increaceMoDetail("emd_out", null);// 出库一个
							agv.waitend2();
							agvState = 1;// agv到达上料处
							agvDirect = 2;// 等待状态 可以进行上料
						}else if(agvState)
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		// 机械手1进行抓取动作
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (endsl < currentSl) {
						if (agvState != 1 || agvDirect != 2) {
							// 如果agv小车不在取料处则等待
							Thread.sleep(1000L);
							continue;
						}
						h.modbus4xWrite("机械手1等待点重置", 11, 0);// 机械手1 完成点判断 归零
						long start = h.startDevice("上料工位");// 上料工位启动
						// 启动
						h.modbus4xWrite("机械手1-指令1", 31, 1);// 启动机械手1 抓料
						Thread.sleep(5000);// 等待5s
						h.modbus4xWrite("机械手1-指令1", 31, 0);// 清除机械手1 抓料写入
						h.modbus4xWait("机械手1-指令1", 11, 1);// 等待机械手从agv抓料完成
						agvDirect=3;
						Thread.sleep(1000L);
						h.modbus4xWrite("机械手1-指令1", 11, 0);// 清除写入
						// 上料工位结束
						h.stepExecOver("上料工位", "emd_process1", null, System.currentTimeMillis() - start);
						Thread.sleep(7000);// 等待7秒再去串口读取
						String readCode = c.waitread();// 等待条码扫描
						// boolean isok = true;
						System.out.println("条码机器扫描出:" + readCode);
						h.modbus4xWait("机械手1-指令1", 11, 1);//等待机械1完成扫码操作
						h.modbus4xWrite("机械手1-指令1", 11, 0);//机械手信号重置
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		return true;
	}

	public int getOneOut() {
		try {
			while (true) {
				if (collectUtil != null) {
					List<Map> list = collectUtil.top1out(null);
					if (list.size() >= 0) {
						String id = list.get(0).get("id").toString();
						Integer location = Integer.valueOf((list.get(0).get("location").toString()));
						Map map = new HashMap<>();
						map.put("id", id);
						map.put("state", 0);
						map.put("name", "");
						map.put("rfid", "");
						// 修改状态为库里没有东西
						collectUtil.updateWarehouse(map);
						return location;
					}
				}
				Thread.sleep(1000);// 直到数据库有东西结束
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean modbus4xWrite(String machineName, int address, int value) {
		try {
			logger.info("开始启动" + machineName + ".");
			ModbusUtil.writeRegister(ip, port, slave, address, value);
			logger.info("启动" + machineName + "完成,未出现异常.");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("启动" + machineName + "失败,出现异常.", e);
			return false;
		}
	}

	public static boolean modbus4xWait(String machineName, int address, int value) throws Exception {
		try {
			logger.info("开始等待" + machineName + "完成操作.");

			Integer read = ModbusUtil.readRegister(ip, port, address, slave);
			int i = 1;
			logger.info("正在读取" + machineName + "信息,第" + i + "次读取,读取值为:" + read + ".,期望值为:" + value + ".");
			while (read != value) {
				if (i >= maxWait)
					throw new Exception(machineName + "读取等待时间过久.");
				Thread.sleep(waitMillis);
				i++;
				// 这个方法用于区分3x和4x
				read = ModbusUtil.readRegister(ip, port, address, slave);
				logger.info("正在读取" + machineName + "信息,第" + i + "次读取,读取值为:" + read + ".,期望值为:" + value + ".");

			}
			logger.info(machineName + "完成操作,结束等待.");
			return true;
		} catch (Exception e) {
			logger.info(machineName + "等待出现异常.", e);
			e.printStackTrace();
			throw new Exception(machineName + "读取出现异常");
		}
	}

	/**
	 * 累加
	 * 
	 * @author lfy
	 * @time 2018年10月19日-下午4:56:14
	 * @param stepName
	 * @param stepName2
	 */
	public void increaceMoDetail(String stepName, String stepName2) {
		Map map = new HashMap<>();
		map.put(stepName, 1);
		map.put(stepName2, 1);
		map.put("emd_order", currentOrder);
		// 订单数累加
		if (collectUtil != null) {
			try {
				collectUtil.updateMoDetailIncreace(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 记录启动时间 并且将状态修改为启动
	 * 
	 * @author lfy
	 * @return
	 * @time 2018年10月18日-下午5:18:52
	 */
	public long startDevice(String devName) {
		if (devUtil != null)
			devUtil.setStatus(devName, 1);
		return System.currentTimeMillis();
	}

	/**
	 * 记录每个工位的结束时间
	 * 
	 * @author lfy
	 * @time 2018年10月18日-下午4:57:53
	 */
	public void stepExecOver(String devName, String stepName, String stepName2, Long times) {
		// TODO
		if (devUtil != null) {
			devUtil.setStatus(devName, 2);// 工位状态改为关闭
		}
		Map dm = new HashMap<>();
		dm.put("eb_device_name", devName);// 节拍名称
		dm.put("eb_device_time", (times / 1000.0));// 节拍时间
		if (collectUtil != null) {
			try {
				collectUtil.insertBeat(dm);// 记录节拍
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Map map = new HashMap<>();
		map.put(stepName, 1);
		map.put(stepName2, 1);
		map.put("emd_order", currentOrder);
		// 订单数累加
		if (collectUtil != null) {
			try {
				collectUtil.updateMoDetailIncreace(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


}

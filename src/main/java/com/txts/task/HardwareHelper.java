package com.txts.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.txts.util.HttpRequestUtil;
import com.txts.util.agv.AgvUtil;
import com.txts.util.code.CodeUtil;
import com.txts.util.device.DeviceCollectUtil;
import com.txts.util.modbus.ModbusUtil;
import com.txts.util.rfid.RfidLoopRead;
import com.txts.util.rfid.RfidUtil;
import com.txts.util.visio.VisioUtil;

/**
 * 硬件操作的汇总类
 * 
 * @description
 * 
 * @author lfy
 * @time 2018年9月28日-下午1:24:42
 */
@SuppressWarnings("all")
@Component
public class HardwareHelper {
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
	private static boolean[] lp = { false, false };// 料盘储位上是否又料盘

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
		HardwareHelper h = new HardwareHelper();

		// h.modbus4xWrite("4号机械手-模拟包装", 31, 1);
		// h.modbus4xWrite("4号机械手-模拟包装", 31, 1);
		// h.modbus4xWrite("4号机械手-模拟包装", 31, 1);
		// Thread.sleep(3000);
		h.modbus4xWrite("4号机械手", 35, 1);
		Thread.sleep(5000);
		h.modbus4xWrite("4号机械手", 35, 0);
		h.modbus4xWait("4号机械手抓取结束", 17, 1);
		Thread.sleep(3000);
		h.modbus4xWait("4号机械手抓取结束2", 17, 1);

		// if(i==0){
		// i++;
		//
		// h.vu=new VisioUtil();
		// h.vu.init();
		// h.agv = new AgvUtil();
		// h.agv.init("COM6");//初识化agv
		// h.c = new CodeUtil();
		// h.c.init("COM5");//初始化条码
		// }
		//
		// i++;
		/*
		 * if(i>=3) return;
		 */
		///// 初始化部分

		// h.runOne();
		//////////////////

	}

	/**
	 * 需要完成的数量
	 * 
	 * @author lfy
	 * @time 2018年10月19日-下午1:00:11
	 * @param sl
	 * @throws Exception
	 */
	public boolean start(int sl, String order) throws Exception {
		new Thread(new Runnable() {
			public void run() {
				isRun = true;
				currentOrder = order;
				endsl = 0;
				currentSl = sl;
				while (true) {
					boolean one = false;
					try {
						one = runOne();
					} catch (Exception e) {
						e.printStackTrace();
						one = false;
					}
					if (one == true) {
						// 成功的时候 完成数量+1
						endsl++;
						// 如果完成数量大于或者等于完工数量 则结束
						if (endsl >= sl) {
							// 修改订单状态
							changeStatus("完工");
							isRun = false;
							currentOrder = null;
							endsl = 0;
							currentSl = 0;
							return;
						}
					}
				}
			}
		}).start();
		return true;
	}

	/**
	 * 执行一次流水线 ng返回false ok返回true
	 * 
	 * @author lfy
	 * @time 2018年10月19日-下午5:01:22
	 * @return
	 * @throws Exception
	 */
	public boolean runOne() throws Exception {
		Long start = 0L;

		if (wh == null)
			wh = new WarehouseHelper();
		HardwareHelper h = this;// modbus
		Integer outValue = h.getOneOut();// 出库位置指令

		// 立体库出库
		wh.modbus4xWait("等待立体库可出库", 37, 1);
		// 立体库出库
		wh.modbus4xWrite("立体库可出库", 38, outValue);
		Thread.sleep(5000);
		// 立体库出库
		wh.modbus4xWait("等待立体库出库完成", 37, 1);
		// 启动立体库抓料上agv
		h.modbus4xWrite("4号机械手", 35, 1);// 1 和2 对应2个位置
		Thread.sleep(5000);
		h.modbus4xWrite("4号机械手", 35, 0);
		h.modbus4xWait("4号机械手抓取结束", 17, 1);

		agv.start1();
		System.out.println("wait agv end ...");
		h.increaceMoDetail("emd_out", null);// 出库一个
		agv.waitend2();
		System.out.println("step agv end ..");

		//////////////////
		///// 第1部分的机械手 抓料 条码扫描 判断ng还是ok
		//////////////////
		///// 初始化部分
		//////////////////
		h.modbus4xWrite("机械手1等待点重置", 11, 0);// 机械手1 完成点判断 归零
		h.modbus4xWrite("机械手2等待点重置", 16, 0);// 机械手2 完成点判断 归零
		h.modbus4xWrite("机械手3等待点重置", 15, 0);// 机械手2 完成点判断 归零
		h.modbus4xWrite("机械手4等待点重置", 17, 0);// 机械手2 完成点判断 归零
		h.modbus4xWrite("打钉机等待点重置", 13, 0);// 机械手2 完成点判断 归零
		start = h.startDevice("上料工位");// 上料工位启动

		// 打钉机 需要手动回原点 不需要代码
		// 启动
		h.modbus4xWrite("机械手1-指令1", 31, 1);// 启动机械手1 抓料
		Thread.sleep(5000);// 等待5s
		h.modbus4xWrite("机械手1-指令1", 31, 0);// 启动机械手1 抓料
		h.modbus4xWait("机械手1-指令1", 11, 1);// 等待机械手从agv抓料完成
		h.modbus4xWrite("机械手1-指令1", 11, 0);// 等待机械手从agv抓料完成 回写
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("agv returning ...");
					agv.start2(); //
					agv.waitend2();//
					System.out.println("agv return end ");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		// 上料工位结束
		h.stepExecOver("上料工位", "emd_process1", null, System.currentTimeMillis() - start);
		Thread.sleep(7000);// 等待7秒再去串口读取
		String readCode = c.waitread();// 等待条码扫描
		// boolean isok = true;
		System.out.println("条码机器扫描出:" + readCode);
		h.modbus4xWait("机械手1-指令1", 11, 1);// 等待机械手1完成
		h.modbus4xWrite("机械手1-指令1", 11, 0);// 启动机械手1关闭信号重置
		// 记录上料结束时间
		// 读取值后的ng判断 是读取值201810011016999910001的时候 就为ok
		if (!(readCode.indexOf("201810011016999910001") >= 0)) {
			h.modbus4xWrite("机械手1-ng", 31, 2);// ng
			Thread.sleep(15000);
			h.modbus4xWrite("机械手1", 31, 0);// 重置归零
			Thread.sleep(5000);
			h.modbus4xWait("机械手1", 11, 1);// 等待读取机械手1执行结束
			h.modbus4xWrite("机械手1", 11, 0);// 启动机械关闭信号重置
			System.out.println("step1 end .. ng");
			// 启动立体库抓料上agv
			h.modbus4xWrite("4号机械手", 35, 2);// 2把料抓到废料盆
			Thread.sleep(5000);
			h.modbus4xWrite("4号机械手", 35, 0);
			h.modbus4xWait("4号机械手抓取结束", 17, 1);
			h.addNg("条码扫描未通过");
			return false;// 结束
		}

		h.modbus4xWrite("机械手1", 31, 3);// ok的时候
		Thread.sleep(5000);
		h.modbus4xWrite("机械手1", 31, 0);// 重置归零
		h.modbus4xWait("机械手1", 11, 1);// 等待读取机械手1执行结束
		h.modbus4xWrite("机械手1", 11, 0);// 启动机械关闭信号重置
		System.out.println("step1 end .. ok");

		//
		Thread.sleep(5000);
		start = h.startDevice("机床工位");
		////////////////////////
		/// 第二部分的机械手抓料 进机床、打钉、上传送带
		///////////////////////
		// 机械手2指令 抓料进机床
		// 加工
		h.modbus4xWrite("机械手2-指令1", 32, 1);// 启动机械手2
		Thread.sleep(5000);
		h.modbus4xWrite("机械手2-指令1", 32, 0);// 关闭机械手
		h.modbus4xWait("机械手2-指令1", 16, 1);// 等待机械手2完成
		h.modbus4xWrite("机械手2-指令1", 16, 0);// 机械手2完成后回写
		// 机床工位结束
		h.stepExecOver("机床工位", "", null, System.currentTimeMillis() - start);
		start = h.startDevice("打钉工位");
		// 启动打钉机
		h.modbus4xWrite("打钉机", 33, 1);// 启动打钉机是1
		Thread.sleep(5000);
		h.modbus4xWrite("打钉机", 33, 0);// 关闭打钉机是
		h.modbus4xWait("打钉机", 13, 1);// 打钉等待
		h.modbus4xWrite("打钉机", 13, 0);// 打钉等待回写
		h.modbus4xWait("打钉机", 33, 0);// 打钉等待
		// 机械手2指令 抓料进机床 然后放入打钉机
		h.modbus4xWrite("机械手2-指令2", 32, 2);// 启动机械手2
		Thread.sleep(5000);
		h.modbus4xWrite("机械手2-指令1", 32, 0);// 关闭机械手2
		h.modbus4xWait("机械手2-指令2", 16, 1);// 等待机械手2指令完成
		h.modbus4xWrite("机械手2-指令2", 16, 0);// 机械手指令2回写

		h.stepExecOver("打钉工位", "emd_process2", null, System.currentTimeMillis() - start);
		start = h.startDevice("视觉工位");
		// 机械手2指令 抓料进机床
		// 启动1号流水线
		h.modbus4xWrite("流水线1-指令1", 25, 1);// 启动流水线1
		Thread.sleep(5000);// 2秒后判断传送带是否启动
		h.modbus4xWait("流水线1-指令1", 4, 1);// 流水线1 等待流水线启动
		// 传感器判断 读取到值的时候 代表分拣位有料了
		h.modbus4xWait("分拣位是否有料判断", 21, 1);// 等待分拣位有料
		h.modbus4xWrite("流水线1-指令1", 25, 0);// 关闭流水线1
		// 启动视觉 先写0 然后写1
		h.modbus4xWrite("视觉启动", 3, 0);
		Thread.sleep(3000);
		h.modbus4xWrite("视觉启动", 3, 1);
		Thread.sleep(3000);
		h.modbus4xWrite("视觉启动", 3, 0);
		// 等待触发视觉扫描
		// 关闭1号流水线
		// Integer read=2;
		Integer read = vu.waitVal();
		System.out.println("读取到的钉子数量为:" + read);
		h.stepExecOver("视觉工位", "", null, System.currentTimeMillis() - start);

		// 钉子的数量不足3 NG的时候
		if (!read.equals(3)) {
			// 启动分拣
			h.modbus4xWrite("视觉分拣机器人", 2, 1);
			// 3# 4# 分拣 转弯 4个流水线是一起的 所以一起启动
			h.modbus4xWrite("启动分拣流水线", 27, 1);// 启动分拣流水线
			h.modbus4xWrite("启动转弯流水线", 28, 1);// 启动转弯流水线
			h.modbus4xWrite("启动3号流水线", 29, 1);// 启动3#流水线
			h.modbus4xWrite("启动4号流水线", 30, 1);// 启动4#流水线
			// 等待分拣结束 分拣结束的时候 地址的值会变成2
			h.modbus4xWait("视觉分拣机器人", 2, 2);

			// 判断分拣位的料是否被拿走
			h.modbus4xWait("分拣流水线", 21, 0);
			// 等待分拣流水线
			h.modbus4xWait("分拣流水线", 6, 1);
			// 等待转弯流水线
			h.modbus4xWait("转弯流水线", 9, 1);
			// 等待3#流水线
			h.modbus4xWait("3号流水线", 7, 1);
			// 等待4#流水线 启
			h.modbus4xWait("4号流水线", 8, 1);

			h.modbus4xWrite("4号机械手", 35, 2);// 2把盆抓到废料盆
			Thread.sleep(5000);
			h.modbus4xWrite("4号机械手", 35, 0);
			h.modbus4xWait("4号机械手抓取结束", 17, 1);
			// 等待分拣位有料
			h.modbus4xWait("传送带NG分拣位有料", 24, 1);
			h.modbus4xWrite("关闭分拣流水线", 27, 0);// 关闭分拣流水线
			h.modbus4xWrite("关闭转弯流水线", 28, 0);// 关闭转弯流水线
			h.modbus4xWrite("关闭3号流水线", 29, 0);// 关闭3#流水线
			h.modbus4xWrite("关闭4号流水线", 30, 0);// 关闭4#流水线

			// 启动5号机器人
			h.modbus4xWrite("5号机械手-分拣", 36, 1);
			Thread.sleep(5000);
			h.modbus4xWrite("5号机械手-分拣", 36, 0);
			// 设备问题 地址未确认
			// h.modbus4xWait("5号机器人-分拣", 19, 1);
			h.addNg("视觉检测未通过");
			Thread.sleep(15000);
			System.out.println("visio ng step end.");
			return false;
		}
		start = h.startDevice("包装工位");
		// ok的时候 收回气缸 启动2号流水线
		// 开启流水线1
		h.modbus4xWrite("流水线1-指令1", 25, 1);

		h.modbus4xWrite("视觉扫描-气缸", 37, 1);
		// 启动2号流水线
		h.modbus4xWrite("2号流水线", 26, 1);
		// 判断2号流水线是否正常启动
		h.modbus4xWait("2号流水线", 5, 1);//

		h.modbus4xWrite("视觉扫描-气缸", 37, 0);
		// 等待3#机械臂位置有料
		h.modbus4xWait("3#机械臂位置有料", 22, 1);
		// 弹出气缸
		h.modbus4xWrite("视觉扫描-气缸", 37, 0);
		System.out.println("关闭1号、2号流水线");
		// 关闭流水线1
		h.modbus4xWrite("流水线1-指令1", 25, 0);
		// 关闭2号流水线
		h.modbus4xWrite("关闭2号流水线", 26, 0);
		System.out.println("关闭1号、2号流水线结束");
		// 启动3#机械臂
		// 设备问题无法启动 地址未确认
		h.modbus4xWrite("3号机械手-模拟包装", 34, 1);// 指a令1 指令2 对应左右2个位置 未确认
		Thread.sleep(5000);
		h.modbus4xWrite("3号机械手-模拟包装", 34, 0);// 关闭机械手指令
		// 包装
		h.stepExecOver("包装工位", "emd_process3", null, System.currentTimeMillis() - start);
		// 设备3#机械臂完成地址未确认
		h.modbus4xWait("3号机械手-模拟包装", 15, 1);
		// 启动2号流水线
		h.modbus4xWrite("2号流水线", 26, 1);

		// 物料到达传送带末端
		h.modbus4xWait("等待物料到达传送带末端", 23, 1);
		Thread.sleep(5000);
		// 关闭2号流水线
		h.modbus4xWrite("2号流水线", 26, 0);
		Map inMap = h.getOneIn();
		int inValue = Integer.valueOf(inMap.get("location").toString());// 入库位置指令

		// 立体库入库
		wh.modbus4xWait("等待立体库可入库", 37, 1);
		// 立体库入库
		wh.modbus4xWrite("立体库可入库", 38, inValue);
		h.dealIn(inMap);// 处理入库的数据库相关操作
		Thread.sleep(5000);
		// 立体库入库
		wh.modbus4xWait("等待立体库入库完成", 37, 1);
		h.increaceMoDetail("emd_in", "emd_endsl");// 入库和完成各增加一个
		System.out.println("传送带 ok step end.");

		return true;
	}

	// ModbusUtil的方法
	// readInputRegister 读取3x的方法 3x只能读 不可写
	// readRegister 读取4x的方法
	// writeRegister 写入4x的方法
	public void test() throws Exception {

	}

	public Map getOneIn() throws InterruptedException {
		while (true) {
			try {
				if (collectUtil != null) {
					List<Map> list = collectUtil.top1in(null);
					if (list.size() > 0) {
						String id = list.get(0).get("id").toString();
						Integer location = Integer.valueOf((list.get(0).get("location").toString()));
						return list.get(0);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(1000);// 一秒读一次 直到有东西
		}
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

	/**
	 * 处理入库数据
	 * 
	 * @author lfy
	 * @time 2018年10月20日-上午8:59:49
	 * @param m
	 * @return
	 */
	public int dealIn(Map m) {
		try {
			Integer id = Integer.valueOf(m.get("id").toString());
			Map map = new HashMap<>();
			map.put("id", id);
			map.put("state", 1);
			map.put("name", "装配件");
			map.put("rfid", getRfid());
			// 修改状态为库里有东西
			return collectUtil.updateWarehouse(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String getRfid() {
		// Map params = new HashMap();
		// params.put("a", 1);
		// String r=HttpRequestUtil.get("http://10.0.0.102:8080/rfid/wait/read", params,
		// 6000, 6000, "utf-8");
		// return r;
		return ru.getLastValue();
	}

	/**
	 * 
	 * 
	 * @author lfy
	 * @time 2018年10月19日-下午4:56:55
	 */
	public void changeStatus(String status) {
		if (collectUtil != null) {
			Map map = new HashMap<>();
			map.put("emd_state", status);
			map.put("emd_order", currentOrder);
			try {
				logger.info("订单状态修改,单号:" + currentOrder + ",状态:" + status);
				collectUtil.changeMoDetail(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 新增质量表 ng原因
	 * 
	 * @author lfy
	 * @time 2018年10月19日-下午4:53:23
	 * @param reason
	 */
	public void addNg(String reason) {
		if (collectUtil != null) {
			Map map = new HashMap();
			map.put("eq_name", reason);
			try {
				collectUtil.insertQuality(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
	////////////////////////
	////// 通用操作代码
	/////////////////////////

	/**
	 * 阻塞线程 等待机完成读取操作(modbus 3x类型)
	 * 
	 * @author lfy
	 * @time 2018年9月28日-下午2:18:59
	 * @param machineName
	 *            机器名(记录日志用)
	 * @param address
	 *            modbus地址
	 * @param value
	 *            期望值 即读取值会变成这个的时候 完成操作
	 * @return
	 * @throws Exception
	 */
	public static boolean modbus3xWait(String machineName, int address, int value) throws Exception {
		try {
			logger.info("开始等待" + machineName + "完成操作.");
			int read = ModbusUtil.readRegister(ip, port, slave, address);
			int i = 1;
			while (read != value) {
				logger.info("正在读取" + machineName + "信息,当前第" + i + "次读取,读取值为:" + value + ".");
				i++;
				Thread.sleep(waitMillis);
				// 这个方法用于区分3x和4x
				read = ModbusUtil.readRegister(ip, port, slave, address);
			}
			logger.info(machineName + "完成操作,结束等待.");
			return true;
		} catch (Exception e) {
			logger.info(machineName + "等待出现异常.", e);
			e.printStackTrace();
			throw new Exception(machineName + "写入出现异常");
		}
	}

	/**
	 * 阻塞线程 等待机完成读取操作(modbus 4x类型)
	 * 
	 * @author lfy
	 * @time 2018年9月28日-下午2:18:59
	 * @param machineName
	 *            机器名(记录日志用)
	 * @param address
	 *            modbus地址
	 * @param value
	 *            期望值 即读取值会变成这个的时候 完成操作
	 * @return
	 * @throws Exception
	 */
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
	 * 写入操作 将数值写入到modbus
	 * 
	 * @author lfy
	 * @time 2018年9月28日-下午2:22:35
	 * @param machineName
	 *            机器名(记录日志用)
	 * @param address
	 *            modbus地址
	 * @param value
	 *            期望值 即读取值会变成这个的时候 完成操作
	 * @return
	 */
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
}

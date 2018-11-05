package com.txts.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.internal.metadata.facets.Validatable;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component
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
	/**
	 * 包装处料盘储位的状态 false 空
	 */
	private static volatile boolean[] pStates = { false, false };
	/**
	 * 出库模块 0 1:运行
	 */
	private static volatile int outModel;
	private static volatile boolean whState = false;// 立体库出口料盘状态 false 空
	private static volatile int whDirect;
	private static volatile int outSl;// 出库数量
	/**
	 * agv状态 0运行状态 1:取料口,准备状态 2:取料口,完成状态 3:上料口,准备状态 4.上料口完成状态
	 */
	private static volatile int agvState;
	/**
	 * rbt2 状态 0:初始位 1:取原料 2:放只传送带
	 */
	private static volatile int rbt2;
	/**
	 * 机械手3状态 true 运行 false 停止
	 */
	private static volatile boolean rbt3State;
	/**
	 * 机械手3 包装
	 */
	private static volatile boolean rbt3package;
	/**
	 * 机械手3 卸料
	 */
	private static volatile boolean rbt3unload;
	private static volatile boolean rbt3unload2;
	/**
	 * 传送带1 0: 1:1启动
	 */
	private static volatile int belt;
	/**
	 * 视觉 0: 1:启动
	 */
	private static volatile int vision;
	/**
	 * 二号流水线
	 */
	private static volatile int belt2;
	private static volatile int belt2State;
	/**
	 * 入库模块动作 0 1:运行中
	 */
	private static volatile int inModel;

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
		modbus4xWrite("关闭分拣流水线", 27, 0);// 关闭分拣流水线
		modbus4xWrite("关闭转弯流水线", 28, 0);// 关闭转弯流水线
		modbus4xWrite("关闭3号流水线", 29, 0);// 关闭3#流水线
		modbus4xWrite("关闭4号流水线", 30, 0);// 关闭4#流水线
		// modbus4xWrite("1号机械手>>抓料", 31, 2);// 启动机械手1 抓料
		// Thread.sleep(5000L);
		// // 等待5s
		// modbus4xWrite("1号机械手>>动作地址清零", 31, 0);// 启动机械手1 抓料

		// modbus4xWrite("3号机械手>>模拟包装", 34, 4);
		// Thread.sleep(3000L);
		// modbus4xWrite("3号机械手>>动作地址清零", 34, 0);// 关闭机械手指令
		// modbus4xWait("3号机械手>>模拟包装完成", 15, 1);
		// Thread.sleep(3000L);
		// modbus4xWrite("3号机械手>>监听地址清零", 15, 0);

		// modbus4xWrite("4号机械手", 17, 0);
		// modbus4xWrite("4号机械手", 35, 2);// 1 和2 对应2个位置
		// Thread.sleep(3000);
		// modbus4xWrite("4号机械手", 35, 0);
		// modbus4xWait("4号机械手抓取结束", 17, 1);
		// modbus4xWrite("4号机械手", 17, 0);
		// Thread.sleep(3000);
		// modbus4xWait("4号机械手抓取结束", 17, 1);
		// modbus4xWrite("4号机械手", 17, 0);

		// modbus4xWrite("机械手2-指令2", 32, 0);
		// Thread.sleep(3000L);
		// modbus4xWrite("机械手2-指令2", 32, 1);
		// Thread.sleep(3000L);
		// modbus4xWrite("机械手2-指令2", 32, 0);
		//
		// modbus4xWrite("4号机械手-取料1", 35, 1);
		// Thread.sleep(2000L);
		// modbus4xWrite("4号机械手-取料1", 35, 0);

		// modbus4xWrite("2号机械手-模拟包装", 16, 0);
		// modbus4xWrite("2号机械手-取料2", 32, 2);
		// Thread.sleep(1000L);
		// modbus4xWrite("2号机械手-取料2", 32,0);
		// modbus4xWrite("流水线1-指令1", 25, 0);// 关闭流水线1
		// modbus4xWrite("关闭2号流水线", 26, 0);
		// WarehouseHelper wh = new WarehouseHelper();
		// wh.modbus4xWrite("立体库可入库", 38,1);
	}

	public void start(int sl, String order) throws Exception {
		outModel = 0;
		isRun = true;
		whDirect = 0;
		currentOrder = order;
		endsl = 0;
		currentSl = sl;
		outSl = 0;
		agvState = 1;
		rbt2 = 0;
		rbt3package = false;
		rbt3unload = false;
		rbt3unload2 = false;
		rbt3State = false;
		belt = 0;
		belt2 = 0;
		belt2State = 0;
		vision = 0;
		inModel = 0;
		if (wh == null)
			wh = new WarehouseHelper();

		// 出库模块线程
		new Thread(new Runnable() {

			@Override
			public void run() {

				while (outSl < currentSl) {
					try {
						if (!whState && inModel == 0) {// 如果出库位没有被占据 且 没有运行入库模块
							Integer outValue = getOneOut();
							if (outValue == null || outValue == 0) {
								break;
							}
							System.out.println("将要出库地址为< " + outValue + ">的原料");
							outModel = 1;// 开始出库
							// 出库位置指令
							wh.modbus4xWait("立体库-等待出库", 37, 1);
							// 立体库出库
							wh.modbus4xWrite("立体库出库:" + outValue, 38, outValue);
							Thread.sleep(3000);
							// 立体库出库
							wh.modbus4xWait("等待立体库出库完成", 37, 1);
							// wh.modbus4xWrite("等待立体库出库", 37, 0);
							outModel = 0;// 出库完成
							whState = true;
							outEnd(outValue);
							outSl++;
							System.out.println(">>>>>>>取出数量" + outSl);
						} else {
							Thread.sleep(1000L);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}).start();
		// 机械手4逻辑
		new Thread(new Runnable() {

			@Override
			public void run() {

				while (true) {
					try {
						if (!whState) {// 出库位为空则等待
							Thread.sleep(1000L);
							continue;
						}
						if (agvState != 1) {// agv不在取料位置则等待
							Thread.sleep(1000L);
							continue;
						}
						if (rbt3State) {// 机械手3处于运行状态时不运行
							Thread.sleep(1000L);
							continue;
						}
						int value = 2;
						if (!pStates[0]) {
							value = 1;
						} else if (!pStates[1]) {
							value = 2;
						} else {// 料盘君不为空则等待
							Thread.sleep(1000L);
							continue;
						}
						// 启动立体库抓料上agv;
						modbus4xWrite("4号机械手>>监听地址清零 ", 17, 0);
						modbus4xWrite("4号机械手>>动作地址清零", 35, 0);
						modbus4xWrite("4号机械手>>取原料 ", 35, value);// 1 和2 对应2个位置
						Thread.sleep(3000);
						modbus4xWrite("4号机械手>>动作地址清零 ", 35, 0);
						modbus4xWait("4号机械手>>等待取原料结束 ", 17, 1);
						modbus4xWrite("4号机械手>>监听地址清零 ", 17, 0);
						Thread.sleep(2000);
						agvState = 2;// agv完成状态
						System.out.println("agv 可运动至上料处");
						modbus4xWait("4号机械手>>放置料盘 ", 17, 1);
						modbus4xWrite("4号机械手>>监听地址清零 ", 17, 0);
						pStates[value - 1] = true;
						whState = false;

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();

		// Agv控制
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						switch (agvState) {
						case 2:// 如果agv在取料口 完成状态 则向上料口运行
							agvState = 0;
							System.out.println("agv 运行中...\n目的地: 上料口");
							agv.start1();
							increaceMoDetail("emd_out", null);// 出库一个
							agv.waitend2();
							System.out.println(" agv 到达上料口 开始等待");
							agvState = 3;
							break;
						case 4:
							agvState = 0;
							System.out.println("agv 运行中... \n目的地: 取料口");
							agv.start2();
							agv.waitend2();
							System.out.println("agv 到达取料口 开始等待");
							agvState = 1;
							break;
						default:
							Thread.sleep(1000L);
							break;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
		// 机械手1
		new Thread(new Runnable() {

			@Override
			public void run() {

				while (true) {
					try {
						if (agvState == 3) {
							modbus4xWrite("1号机械手>>监听地址清零", 11, 0);
							modbus4xWrite("1号机械手>>运动地址清零", 31, 0);
							// 启动
							Long start = startDevice("上料工位");// 上料工位启动
							modbus4xWrite("1号机械手>>抓料", 31, 1);// 启动机械手1 抓料
							Thread.sleep(5000L);
							// 等待5s
							modbus4xWrite("1号机械手>>动作地址清零", 31, 0);// 启动机械手1 抓料
							modbus4xWait("1号机械手>>完成抓料", 11, 1);// 等待机械手从agv抓料完成
							modbus4xWrite("1号机械手>>监听地址清零", 11, 0);// 等待机械手从agv抓料完成 回写
							agvState = 4;// agv上料处 完成状态
							// 上料工位结束
							stepExecOver("上料工位", "emd_process1", null, System.currentTimeMillis() - start);
							Thread.sleep(7000L);// 等待7秒再去串口读取
							System.out.println("条码扫描....");
							String readCode = c.waitread();// 等待条码扫描
							System.out.println("条码机器扫描出:" + readCode);
							modbus4xWait("1号机械手>>等待条码扫描动作结束", 11, 1);
							Thread.sleep(2000L);
							modbus4xWrite("1号机械手>>监听地址清零", 11, 0);
							if (!(readCode.indexOf("201810011016999910001") >= 0)) {
								modbus4xWrite("1号机械手>>ng动作", 31, 2);// ng
								Thread.sleep(15000L);
								modbus4xWrite("1号机械手>>动作地址清零", 31, 0);// 重置归零
								Thread.sleep(5000);
								modbus4xWait("1号机械手>>ng动作完成", 11, 1);// 等待读取机械手1执行结束
								modbus4xWrite("1号机械手>>监听地址清零", 11, 0);// 启动机械关闭信号重置
								rbt3unload = true;// 机械手3卸料
								addNg("条码扫描未通过");
							} else {
								modbus4xWrite("1号机械手>>ok动作", 31, 3);
								Thread.sleep(5000);
								modbus4xWrite("1号机械手>>动作地址清零", 31, 0);// 重置归零
								modbus4xWait("1号机械手>>ok动作完成", 11, 1);// 等待读取机械手1执行结束
								modbus4xWrite("1号机械手>>监听地址清零", 11, 0);// 启动机械关闭信号重置
								rbt2 = 1;
							}
						} else {
							Thread.sleep(1000L);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}).start();
		// 机械手2 控制
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						if (rbt2 == 1) {
							Long start = startDevice("机床工位");
							rbt2 = 0;
							modbus4xWrite("2号机械手>>监听地址清零", 16, 0);
							modbus4xWrite("2号机械手>>动作地址清零", 32, 0);
							modbus4xWrite("打钉机      >>动作地址清零", 33, 0);// 关闭打钉机
							modbus4xWrite("打钉机      >>监听地址清零", 13, 0);// 打钉等待回写
							modbus4xWrite("2号机械手>>加工动作", 32, 1);// 启动机械手2
							Thread.sleep(5000);
							modbus4xWrite("2号机械手>>动作地址清零", 32, 0);// 关闭机械手
							modbus4xWait("2号机械手>>加工动作完成", 16, 1);// 等待机械手2完成
							modbus4xWrite("2号机械手>>监听地址清零", 16, 0);// 机械手2完成后回写
							// 机床工位结束
							stepExecOver("机床工位", "", null, System.currentTimeMillis() - start);
							start = startDevice("打钉工位");
							Thread.sleep(2000);
							// 启动打钉机
							modbus4xWrite("打钉机      >>打钉", 33, 1);// 启动打钉机是1
							Thread.sleep(5000);
							modbus4xWrite("打钉机      >>动作工位清零", 33, 0);// 关闭打钉机
							modbus4xWait("打钉机      >>打钉完成", 13, 1);// 打钉等待
							Thread.sleep(2000);
							modbus4xWrite("打钉机      >>监听地址清零", 13, 0);// 打钉等待回写
							modbus4xWait("打钉机", 33, 0);// 打钉等待
							// 机械手2指令 抓料进机床 然后放入打钉机
							modbus4xWrite("2号机械手>>运送到传送带", 32, 2);// 启动机械手2
							Thread.sleep(5000);
							modbus4xWrite("2号机械手>>动作地址清零", 32, 0);// 关闭机械手2
							modbus4xWait("2号机械手>>完成运送", 16, 1);// 等待机械手2指令完成
							modbus4xWrite("2号机械手>>监听地址清零", 16, 0);// 机械手指令2回写
							stepExecOver("打钉工位", "emd_process2", null, System.currentTimeMillis() - start);
							belt = 1;
						} else {
							Thread.sleep(1000L);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
		// 传送带 控制
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						if (belt == 1) {
							belt = 0;
							modbus4xWrite("流水线1>>动作地址清零", 25, 0);
							modbus4xWait("分拣位>>动作地址清零", 21, 0);
							modbus4xWrite("流水线1>>监听地址清零", 4, 0);
							modbus4xWrite("流水线1>>启动", 25, 1);// 启动流水线1
							Thread.sleep(5000);// 2秒后判断传送带是否启动
							modbus4xWait("流水线1>>流水线已启动", 4, 1);// 流水线1 等待流水线启动
							// 传感器判断 读取到值的时候 代表分拣位有料了
							modbus4xWait("分拣位>>物品到达", 21, 1);// 等待分拣位有料
							modbus4xWrite("流水线1>>动作地址清零", 25, 0);// 关闭流水线1
							vision = 1;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		// 视觉 控制
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						if (vision == 1) {
							modbus4xWrite("视觉分拣机器人>>动作地址清零", 2, 0);
							modbus4xWrite("分拣流水线>>动作地址清零", 27, 0);
							modbus4xWrite("转弯流水线>>动作地址清零", 28, 0);
							modbus4xWrite("3号流水线>>动作地址清零", 29, 0);
							modbus4xWrite("4号流水线>>动作地址清零", 30, 0);
							modbus4xWrite("5号机械手>>动作地址清零", 36, 0);

							long start = startDevice("视觉工位");
							// 启动视觉 先写0 然后写1
							modbus4xWrite("视觉启动", 3, 0);
							Thread.sleep(3000);
							modbus4xWrite("视觉启动", 3, 1);
							Thread.sleep(3000);
							modbus4xWrite("视觉启动", 3, 0);
							// 等待触发视觉扫描
							// 关闭1号流水线
							// Integer read=2;
							Integer read = vu.waitVal();
							System.out.println("读取到的钉子数量为:" + read);
							stepExecOver("视觉工位", "", null, System.currentTimeMillis() - start);
							vision = 0;
							// 钉子的数量不足3 NG的时候
							if (!read.equals(3)) {
								// 启动分拣
								modbus4xWrite("视觉分拣机器人", 2, 1);
								// 3# 4# 分拣 转弯 4个流水线是一起的 所以一起启动
								modbus4xWrite("启动分拣流水线", 27, 1);// 启动分拣流水线
								modbus4xWrite("启动转弯流水线", 28, 1);// 启动转弯流水线
								modbus4xWrite("启动3号流水线", 29, 1);// 启动3#流水线
								modbus4xWrite("启动4号流水线", 30, 1);// 启动4#流水线
								// 等待分拣结束 分拣结束的时候 地址的值会变成2
								modbus4xWait("视觉分拣机器人>>结束", 2, 2);
								// 判断分拣位的料是否被拿走
								modbus4xWait("分拣流水线>>结束", 21, 0);
								// 等待分拣流水线
								modbus4xWait("分拣流水线>>启动完成", 6, 1);
								// 等待转弯流水线
								modbus4xWait("转弯流水线>>启动完成", 9, 1);
								// 等待3#流水线
								modbus4xWait("3号流水线>>启动完成", 7, 1);
								// 等待4#流水线 启
								modbus4xWait("4号流水线>>启动完成", 8, 1);

								// 3号机器人卸料
								rbt3unload2 = true;
								// 等待分拣位有料
								modbus4xWait("传送带NG分拣位有料", 24, 1);
								modbus4xWrite("分拣流水线>>关闭", 27, 0);// 关闭分拣流水线
								modbus4xWrite("转弯流水线>>关闭", 28, 0);// 关闭转弯流水线
								modbus4xWrite("3号流水线>>关闭", 29, 0);// 关闭3#流水线
								modbus4xWrite("4号流水线>>关闭", 30, 0);// 关闭4#流水线
								// 启动5号机器人
								modbus4xWrite("5号机械手>>分拣", 36, 1);
								Thread.sleep(5000);
								modbus4xWrite("5号机械手>>动作地址清零", 36, 0);
								addNg("视觉检测未通过");
								Thread.sleep(15000);
							} else {
								// ok的时候 收回气缸 启动2号流水线
								modbus4xWrite("视觉扫描-气缸>>弹出", 37, 1);
								// 开启流水线1
								modbus4xWrite("流水线1     >>启动", 25, 1);
								// 启动2号流水线
								modbus4xWrite("流水线2     >>启动", 26, 1);
								// 判断2号流水线是否正常启动
								modbus4xWait("流水线2      >>启动完成", 5, 1);//
								modbus4xWrite("视觉扫描-气缸>>关闭", 37, 0);
								// 等待3#机械臂位置有料
								modbus4xWait("包装处发现物料", 22, 1);
								// 弹出气缸
								modbus4xWrite("视觉扫描-气缸>>关闭", 37, 0);
								// 关闭流水线1
								modbus4xWrite("流水线1     >>关闭", 25, 0);
								// 关闭2号流水线
								modbus4xWrite("流水线2     >>关闭", 26, 0);
								// 启动3号机械手上料
								rbt3package = true;

							}
						} else {
							Thread.sleep(1000L);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		// 机械手3 控制
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						boolean flag = true;
						if (pStates[0]) {
							flag = true;
						} else if (pStates[1]) {
							flag = false;
						} else {
							Thread.sleep(1000L);
							continue;
						}
						if (rbt3package) {// 包装
							modbus4xWrite("3号机械手>>动作地址清零", 34, 0);
							modbus4xWrite("3号机械手>>监听地址清零", 15, 0);
							rbt3State = true;
							long start = startDevice("包装工位");
							if (flag) {
								modbus4xWrite("3号机械手>>模拟包装", 34, 3);
							} else {
								modbus4xWrite("3号机械手>>模拟包装", 34, 2);
							}
							Thread.sleep(3000L);
							modbus4xWrite("3号机械手>>动作地址清零", 34, 0);// 关闭机械手指令
							stepExecOver("包装工位", "emd_process3", null, System.currentTimeMillis() - start);
							// 设备3#机械臂完成地址未确认
							modbus4xWait("3号机械手>>模拟包装完成", 15, 1);
							rbt3package = false;
							rbt3State = false;
							belt2 = 1;
							if (flag) {
								pStates[0] = false;
							} else {
								pStates[1] = false;
							}
							Thread.sleep(3000L);
							modbus4xWrite("3号机械手>>监听地址清零", 15, 0);
							continue;
						}
						if (rbt3unload) {
							rbt3State = true;
							modbus4xWrite("3号机械手>>动作地址清零", 34, 0);
							modbus4xWrite("3号机械手>>监听地址清零", 15, 0);
							if (flag) {
								modbus4xWrite("3号机械手>>卸料", 34, 4);
							} else {
								modbus4xWrite("3号机械手>>卸料", 34, 1);
							}
							Thread.sleep(3000L);
							modbus4xWrite("3号机械手>>动作地址清零", 34, 0);// 关闭机械手指令
							modbus4xWait("3号机械手>>完成卸料", 15, 1);
							if (flag) {
								pStates[0] = false;
							} else {
								pStates[1] = false;
							}
							rbt3State = false;
							rbt3unload = false;
							Thread.sleep(3000L);
							modbus4xWrite("3号机械手>>监听地址清零", 15, 0);
							continue;
						}
						if (rbt3unload2) {
							rbt3State = true;
							modbus4xWrite("3号机械手>>动作地址清零", 34, 0);
							modbus4xWrite("3号机械手>>监听地址清零", 15, 0);
							if (flag) {
								modbus4xWrite("3号机械手>>卸料", 34, 4);
							} else {
								modbus4xWrite("3号机械手>>卸料", 34, 1);
							}
							Thread.sleep(3000L);
							modbus4xWrite("3号机械手>>动作地址清零", 34, 0);// 关闭机械手指令
							modbus4xWait("3号机械手>>完成卸料", 15, 1);
							if (flag) {
								pStates[0] = false;
							} else {
								pStates[1] = false;
							}
							rbt3State = false;
							rbt3unload2 = false;
							Thread.sleep(3000L);
							modbus4xWrite("3号机械手>>监听地址清零", 15, 0);
							continue;
						}
						/*
						 * switch (rbt3) { case 1:// 上料 modbus4xWrite("3号机械手>>动作地址清零", 34, 0);
						 * modbus4xWrite("3号机械手>>监听地址清零", 15, 0); rbt3State = true; long start =
						 * startDevice("包装工位"); if (flag) { modbus4xWrite("3号机械手>>模拟包装", 34, 3); } else
						 * { modbus4xWrite("3号机械手>>模拟包装", 34, 2); } Thread.sleep(3000L);
						 * modbus4xWrite("3号机械手>>动作地址清零", 34, 0);// 关闭机械手指令 stepExecOver("包装工位",
						 * "emd_process3", null, System.currentTimeMillis() - start); // 设备3#机械臂完成地址未确认
						 * modbus4xWait("3号机械手>>模拟包装完成", 15, 1); rbt3 = 0; rbt3State = false; belt2 = 1;
						 * if (flag) { pStates[0] = false; } else { pStates[1] = false; }
						 * Thread.sleep(3000L); modbus4xWrite("3号机械手>>监听地址清零", 15, 0); break; case 2://
						 * 卸料 rbt3State = true; modbus4xWrite("3号机械手>>动作地址清零", 34, 0);
						 * modbus4xWrite("3号机械手>>监听地址清零", 15, 0); if (flag) { modbus4xWrite("3号机械手>>卸料",
						 * 34, 4); } else { modbus4xWrite("3号机械手>>卸料", 34, 1); } Thread.sleep(3000L);
						 * modbus4xWrite("3号机械手>>动作地址清零", 34, 0);// 关闭机械手指令 modbus4xWait("3号机械手>>完成卸料",
						 * 15, 1); if (flag) { pStates[0] = false; } else { pStates[1] = false; }
						 * rbt3State = false; rbt3 = 0; Thread.sleep(3000L);
						 * modbus4xWrite("3号机械手>>监听地址清零", 15, 0); break; default: Thread.sleep(1000L);
						 * break; }
						 */
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
		// 二号流水线
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						if (belt2 == 1) {
							belt2State = 1;// 2号流水线开始启动
							// 启动2号流水线
							modbus4xWrite("流水线2包装动作>>启动", 26, 1);
							// 物料到达传送带末端
							modbus4xWait("等待物品到达末端", 23, 1);
							Thread.sleep(3000L);
							// 关闭2号流水线
							modbus4xWrite("流水线2包装动作>>动作地址清零", 26, 0);
							modbus4xWrite("等待物品到达末端>>清零", 23, 0);
							belt2 = 0;
							belt2State = 0;
							whDirect = 1;
						} else {
							Thread.sleep(1000L);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		// 入库模块线程
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						if (whDirect == 1 && outModel == 0) {// 入库
							inModel = 1;// 入库开始
							whDirect = 0;
							Map inMap = getOneIn();
							int inValue = Integer.valueOf(inMap.get("location").toString());// 入库位置指令

							// 立体库入库
							WarehouseHelper.modbus4xWait("等待立体库可入库", 37, 1);
							// 立体库入库
							WarehouseHelper.modbus4xWrite("立体库可入库:" + inValue, 38, inValue);
							dealIn(inMap);// 处理入库的数据库相关操作
							Thread.sleep(5000);
							// 立体库入库
							WarehouseHelper.modbus4xWait("等待立体库入库完成", 37, 1);
							inModel = 0;// 入库结束
							increaceMoDetail("emd_in", "emd_endsl");// 入库和完成各增加一个
						} else {
							Thread.sleep(1000L);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}).start();
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
						// Map map = new HashMap<>();
						// map.put("id", id);
						// map.put("state", 0);
						// map.put("name", "");
						// map.put("rfid", "");
						// // 修改状态为库里没有东西
						// collectUtil.updateWarehouse(map);
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

	private void outEnd(Integer location) {
		Map map = new HashMap<>();
		map.put("location", location);
		map.put("state", 0);
		map.put("name", "");
		map.put("rfid", "");
		collectUtil.updateWarehouse2(map);
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

}

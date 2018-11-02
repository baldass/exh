package com.txts.exh.console.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.txts.config.BaseController;
import com.txts.exh.console.service.ConsoleService;
import com.txts.task.CollectUtil;
import com.txts.task.HardwareHelper;
import com.txts.task.HardwareHelper2;
import com.txts.util.Json;
import com.txts.util.device.DeviceCollectUtil;
import com.vo.BootstrapTable;

/**
 * @description 控制台
 * 
 * @author lfy
 * @time 2018年9月11日-上午8:52:52
 */
@SuppressWarnings("all")
@Controller
@RequestMapping("console")
public class ConsoleController extends BaseController {
	@Autowired
	private ConsoleService consoleService;
	public static Long startStamp;
	@Autowired
	private CollectUtil collectUtil;
	//@Autowired
	//private TaskRun run;
	@Autowired
	private HardwareHelper hard;
	@Autowired
	private HardwareHelper2 hard2;
	@Autowired
	private DeviceCollectUtil deviceUtil;
	
	/**
	 * 开工页面
	 * 
	 * @author lfy
	 * @time 2018年10月10日-下午4:13:36
	 * @return
	 */
	@RequestMapping("orderpicker")
	public Object orderpicker() {
		return "common/orderpicker";
	}
	@ResponseBody
	@RequestMapping("test")
	public Object test() throws Exception {
		Map map =new HashMap<>();
		map.put("emd_state", "1");
		map.put("emd_order", "1");
		try {
			collectUtil.changeMoDetail(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "true";
	}
	/**
	 * 开工数据
	 * 
	 * @author lfy
	 * @time 2018年10月10日-下午4:13:45
	 * @return
	 */
	@ResponseBody
	@RequestMapping("orderpicker/data")
	public Object orderpickerdata() {
		BootstrapTable<Map> table = consoleService.getPickerOrder(getRequestPageAndLoginMap());
		return table;
	}

	/**
	 * 开工
	 * 
	 * @author lfy
	 * @time 2018年10月10日-下午4:13:45
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("orderpicker/start")
	public Object orderpickerstart() throws Exception {
		Map requestMap = getRequestMap();
		String order = (String) requestMap.get("mo_no");
		Integer num = Integer.valueOf(requestMap.get("emd_sl").toString());
		if(!InitController.isInited){
			return new Json(false, "当前设备未初始化", null);
		}
		if (hard2.isRun) {
			return new Json(false, "当前有工单正在开工", null);
		}
		int ix = consoleService.orderStart(requestMap);
		if (ix == 0) {
			return new Json(false, "失败", null);
		}
//		hard.start(num, order);
		hard2.start(num,order);
		// TODO
		return new Json(true, "操作成功", null);
	}

	/**
	 * 任务池
	 * 
	 * @author lfy
	 * @time 2018年9月26日-上午9:18:19
	 * @return
	 */
	@RequestMapping("dispatch")
	public Object dispatch() {
		return "common/dispatch";
	}

	/**
	 * 任务池集合
	 * 
	 * @author lfy
	 * @time 2018年9月26日-上午9:18:23
	 * @return
	 */
	@ResponseBody
	@RequestMapping("dispatch/list")
	public Object dispatch_list() {
		BootstrapTable bootstrapTable = consoleService.getDispatch(getRequestMap());
		List d = bootstrapTable.getRows();
		return d;
	}

	/**
	 * 任务池详情
	 * 
	 * @author lfy
	 * @time 2018年9月26日-上午9:18:30
	 * @return
	 */
	@ResponseBody
	@RequestMapping("dispatch/detail")
	public Object dispatch_detail() {
		String s = consoleService.getDispatchDetail(getRequestMap());

		return new Json(true, null, s);
	}

	/**
	 * 更新任务池
	 * 
	 * @author lfy
	 * @time 2018年9月26日-上午9:18:42
	 * @return
	 */
	@ResponseBody
	@RequestMapping("dispatch/update")
	public Object dispatch_update() {
		Integer i = consoleService.updateDispatchDetail(getRequestMap());
		if (i > 0) {
			return new Json(true, "操作成功！", null);
		}
		return new Json(false, "操作失败", null);
	}

	/**
	 * 当前所有的设备
	 * 
	 * @author lfy
	 * @time 2018年9月26日-上午9:18:48
	 * @return
	 */
	@ResponseBody
	@RequestMapping("dispatch/device")
	public Object dispatch_device() {
		List l = new ArrayList<>();
		Map m4 = new HashMap<>();
		m4.put("device_id", "5");
		m4.put("device", "机械臂1");
		m4.put("handle", "描述4");
		m4.put("status", "1");
		m4.put("status_name", "正常");
		Map m3 = new HashMap<>();
		m3.put("device_id", "4");
		m3.put("device", "机械臂2");
		m3.put("handle", "描述3");
		m3.put("status", "1");
		m3.put("status_name", "正常");
		Map m2 = new HashMap<>();
		m2.put("device_id", "3");
		m2.put("device", "车床1");
		m2.put("handle", "描述2");
		m2.put("status", "1");
		m2.put("status_name", "正常");
		Map m1 = new HashMap<>();
		m1.put("device_id", "2");
		m1.put("device", "传送带2");
		m1.put("handle", "描述1");
		m1.put("status", "1");
		m1.put("status_name", "正常");
		Map m0 = new HashMap<>();
		m0.put("device_id", "1");
		m0.put("device", "传送带1");
		m0.put("status", "1");
		m0.put("handle", "描述0");
		m0.put("status_name", "正常");
		l.add(m0);
		l.add(m1);
		l.add(m2);
		l.add(m3);
		l.add(m4);
		return new Json(true, null, l);
	}

	/**
	 * 首页
	 * 
	 * @author lfy
	 * @time 2018年9月11日-上午8:53:41
	 * @return
	 */
	@RequestMapping("index")
	public Object index() {
		return "common/index";
	}

	/**
	 * 看板
	 * 
	 * @author lfy
	 * @time 2018年9月26日-上午9:19:03
	 * @return
	 */
	@RequestMapping("border/white")
	public Object border() {
		return "common/border";
	}

	/**
	 * 看板
	 * 
	 * @author lfy
	 * @time 2018年9月26日-上午9:19:09
	 * @return
	 */
	@RequestMapping("border/blue")
	public Object blueBorder() {
		return "common/border_blue";
	}

	public List<Map> getCompletesMap() {
		List<Map> ls = new ArrayList<Map>();
		Random rd = new Random();
		int nextInt = rd.nextInt(999);
		Map m2 = new HashMap();
		m2.put("name", "已完成");
		m2.put("value", nextInt);
		Map m = new HashMap();
		m.put("name", "未完成");
		nextInt = rd.nextInt(999);
		m.put("value", nextInt);
		ls.add(m);
		ls.add(m2);
		return ls;
	}

	public List<Map> getDeviceMap() {
		List<Map> ls = new ArrayList<Map>();
		Random rd = new Random();
		Map o1 = new HashMap<>();
		int nextInt = rd.nextInt(3);
		o1.put("device_name", "机械手1");
		o1.put("device_status", nextInt);
		Map o2 = new HashMap<>();
		o2.put("device_name", "机械手2");
		nextInt = rd.nextInt(3);
		o2.put("device_status", nextInt);
		Map o3 = new HashMap<>();
		o3.put("device_name", "机械手3");
		nextInt = rd.nextInt(3);
		o3.put("device_status", nextInt);
		ls.add(o1);
		ls.add(o2);
		ls.add(o3);
		return ls;
	}

	public Map getActMap() {
		Random rd = new Random();
		int nextInt = rd.nextInt(10);
		Map o1 = new HashMap<>();
		o1.put("device_name", "设备" + nextInt);
		nextInt = rd.nextInt(10) + 1;
		o1.put("plan_time", nextInt);
		nextInt = rd.nextInt(10);
		o1.put("real_time", nextInt);
		nextInt = rd.nextInt(51) + 50;
		o1.put("ok_rate", nextInt + "%");
		o1.put("effic", nextInt);
		// o1.put("oee", nextInt);
		return o1;
	}

	public List<Map> getAsMap() {
		List<Map> ls = new ArrayList<Map>();
		ls.add(getActMap());
		ls.add(getActMap());
		ls.add(getActMap());
		return ls;
	}

	/**
	 * 数据 看板
	 * 
	 * @author lfy
	 * @time 2018年9月26日-上午9:19:09
	 * @return
	 */
	@ResponseBody
	@RequestMapping("border/blue/data")
	public Object blueBorderDate() {
		// Map m = getTestData();
		Map m = new HashMap<>(); // 订单进度数据
		m.put("order", collectUtil.selectMoDetail(null));
		// 设备数据
		m.put("device", deviceUtil.getStatus());// 实际数据 
		//m.put("device", getDeviceMap()); // 设备数据
		m.put("quality", collectUtil.selectQuality(null)); // 稼动率
		m.put("activation", collectUtil.selectOpera(null)); // 节拍
		m.put("beat", collectUtil.selectBeat(null));
		
		return new Json(true, "", m);
	}

	private int count = 0;
	private int orderCount = 0;

	private Map getTestData() {
		String[] names = { "数控设备", "加工机床", "视觉检验", "包装设备" };
		String[] reasons = { "洞口数量少", "未打螺丝", "螺丝数量少", "未包装" };
		count++;
		Map result = new HashMap<>();
		List<Map<String, Object>> order = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 4; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			if (i == 0) {
				int base = orderCount / 5;
				int remainder = orderCount % 5;
				orderCount++;
				item.put("emd_id", 1);
				item.put("emd_date", "2018-10-13 9:00:00");
				item.put("emd_order", "test-order-001");
				item.put("emd_desc", "装配件");
				item.put("emd_sl", 100);
				item.put("emd_endsl", base);
				item.put("emd_process1", (base + remainder > 0 ? 0 : 1));
				item.put("emd_process2", (base + remainder > 1 ? 0 : 1));
				item.put("emd_process3", (base + remainder > 2 ? 0 : 1));
				item.put("emd_in", (base + remainder > 3 ? 0 : 1));
				item.put("emd_out", (base + remainder == 0 ? 0 : 1));
				item.put("emd_ng", 0);
				item.put("emd_state", "在产");

			} else {
				item.put("emd_id", 2);
				item.put("emd_date", "2018-10-13 9:00:00");
				item.put("emd_order", "test-order-001");
				item.put("emd_desc", "装配件");
				item.put("emd_sl", 100);
				item.put("emd_endsl", 0);
				item.put("emd_process1", 0);
				item.put("emd_process2", 0);
				item.put("emd_process3", 0);
				item.put("emd_in", 0);
				item.put("emd_out", 0);
				item.put("emd_ng", 0);
				item.put("emd_state", "计划");
			}
			order.add(item);
		}
		result.put("order", order);
		List<Map<String, Object>> device = new ArrayList<Map<String, Object>>();
		Random rd = new Random();
		for (int i = 0; i < 4; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("device_name", names[i]);
			int nextInt = rd.nextInt(2);
			item.put("device_status", nextInt);
			device.add(item);
		}
		result.put("device", device);
		List<Map<String, Object>> quality = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 4; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("eq_name", reasons[i]);
			item.put("sum", i + count);
			quality.add(item);
		}
		result.put("quality", quality);
		List<Map<String, Object>> activation = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 4; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("device_name", names[i]);
			item.put("real_time", (int) (Math.random() * 6) + 1);
			item.put("plan_time", 8);
			item.put("ok_rate", Math.random());
			activation.add(item);
		}
		result.put("activation", activation);
		List<Map<String, Object>> beat = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < 4; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("beat_name", names[i]);
			item.put("beat_num", new BigDecimal(Math.random() * 3).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
			beat.add(item);
		}
		result.put("beat", beat);
		return result;
	}

	/**
	 * 看板
	 * 
	 * @author lfy
	 * @time 2018年9月26日-上午9:19:09
	 * @return
	 */
	@RequestMapping("border/device")
	public Object deviceBorder() {
		return "common/border_device";
	}

	/**
	 * 看板
	 * 
	 * @author lfy
	 * @time 2018年9月26日-上午9:19:09
	 * @return
	 */
	@RequestMapping("border/device1")
	public Object deviceBorder1() {
		return "common/border_device1";
	}

	/**
	 * 看板
	 * 
	 * @author lfy
	 * @time 2018年9月26日-上午9:19:09
	 * @return
	 */
	@RequestMapping("border/device2")
	public Object deviceBorder2() {
		return "common/border_device2";
	}

	/**
	 * 看板
	 * 
	 * @author lfy
	 * @time 2018年9月26日-上午9:19:09
	 * @return
	 */
	@RequestMapping("border/device3")
	public Object deviceBorder3() {
		return "common/border_device3";
	}

	/**
	 * 看板
	 * 
	 * @author lfy
	 * @time 2018年9月26日-上午9:19:09
	 * @return
	 */
	@RequestMapping("border/device4")
	public Object deviceBorder4() {
		return "common/border_device4";
	}

	/**
	 * 看板
	 * 
	 * @author lfy
	 * @time 2018年9月26日-上午9:19:09
	 * @return
	 */
	@RequestMapping("border/device5")
	public Object deviceBorder5() {
		return "common/border_device5";
	}

	/**
	 * 数据 设备分屏
	 * 
	 * @author lfy
	 * @time 2018年9月26日-上午9:19:09
	 * @return
	 */
	@ResponseBody
	@RequestMapping("border/blue/device/data")
	public Object blueBorderDevice() {
		String device_name = (String) getRequestMap().get("device_name");
		Map m = new HashMap<>();
		// 订单进度数据
		Map map = new HashMap();
		map.put("emd_state", "在产");
		List<Map> moDetail = collectUtil.selectMoDetail(map);
		try {
			Map md = new HashMap();
			for (Map map2 : moDetail) {
				if( "在产".equals(map2.get("emd_state"))){
					md = map2;
				}
			}
			moDetail=new ArrayList();
			moDetail.add(md);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 订单进度数据
		m.put("moDetail", moDetail);
		m.put("deviceStatus", deviceUtil.getDeviceStatus(device_name));
		m.put("startStamp", startStamp);//启动时间
		m.put("quality", collectUtil.selectQuality(null)); // 稼动率
		return new Json(true, "", m);
	}
	
	
	
	// 当前订单
	@ResponseBody
	@RequestMapping("order/list")
	public Object order_list() {
		// 虚拟数据
		Map m = new HashMap<>();
		m.put("mo_no", "test-001-001");
		m.put("MC_Name", "测试产品");
		m.put("mo_sl", "1600");
		m.put("mo_end_sl", "600");
		m.put("mo_pass_sl", "600");
		m.put("mo_NG_sl", "100");
		m.put("plan_start", "2016-07-05");
		m.put("plan_end", "2016-07-15");
		List l = new ArrayList<>();
		l.add(m);
		BootstrapTable t = new BootstrapTable();
		t.setTotal(1);
		t.setRows(l);
		return t;
	}

	// 当前物料
	@ResponseBody
	@RequestMapping("material/list")
	public Object material_list() {
		// 虚拟数据
		Map m = new HashMap<>();
		m.put("material_name", "material_test_1");
		m.put("material_in_producing", "1000");
		m.put("material_left", "6910");
		Map m2 = new HashMap<>();
		m2.put("material_name", "material_test_2");
		m2.put("material_in_producing", "800");
		m2.put("material_left", "9171");
		List l = new ArrayList<>();
		l.add(m);
		// l.add(m2);
		BootstrapTable t = new BootstrapTable();
		t.setTotal(2);
		t.setRows(l);
		return t;
	}

	// 当前设备质量管理
	@ResponseBody
	@RequestMapping("ins/list")
	public Object ins_list() {
		Map m = new HashMap<>();
		m.put("ins_pass", "1120");
		m.put("ins_NG", "10");
		m.put("ins_reason", "test test test");
		List l = new ArrayList<>();
		l.add(m);
		BootstrapTable t = new BootstrapTable();
		t.setTotal(2);
		t.setRows(l);
		return t;
	}

	// 当前设备管理
	@ResponseBody
	@RequestMapping("device/list")
	public Object device_list() {
		Map m4 = new HashMap<>();
		m4.put("device_name", "传送带01");
		m4.put("img_path", "/img/device/it2.png");
		m4.put("status", "正常");
		m4.put("is_start", "启动");
		Map m3 = new HashMap<>();
		m3.put("device_name", "机械臂01");
		m3.put("img_path", "/img/device/it1.jpg");
		m3.put("status", "正常");
		m3.put("is_start", "启动");
		Map m2 = new HashMap<>();
		m2.put("device_name", "机械臂02");
		m2.put("img_path", "/img/device/it1.jpg");
		m2.put("status", "正常");
		m2.put("is_start", "启动");
		Map m = new HashMap<>();
		m.put("device_name", "机械臂03");
		m.put("img_path", "/img/device/it1.jpg");
		m.put("status", "正常");
		m.put("is_start", "启动");

		List l = new ArrayList<>();
		l.add(m4);
		l.add(m3);
		l.add(m2);
		l.add(m);
		Map hm = new HashMap();
		hm.put("data", l);
		hm.put("result", 1);
		return hm;
	}

	@RequestMapping("device/simulatedata")
	@ResponseBody
	public Object devicesimulatedata() {
		HashMap m = new HashMap();

		return new Json(true, null, m);
	}

}

//package com.txts.task;
//
//import java.util.List;
//import java.util.Map;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.txts.exh.console.service.ConsoleService;
//
///**
// * 执行的线程
// * @description 
// * 
// * @author lfy
// * @time 2018年9月29日-上午11:12:36
// */
//public class LineRunable implements Runnable{
//	private ConsoleService service;
//	@Override
//	public void run() {
//		List<Map<String,Object>> list = service.getDispatchList(null);
//		Map<String, Object> map = list.get(0);
//		JSONArray arr = JSON.parseArray((String) map.get("detail"));
//		for (int i = 0; i < arr.size(); i++) {
//			JSONObject item = arr.getJSONObject(i);
//			//设备ID
//			String dev_id = item.getString("device_id");
//			switch (dev_id) {
//				case "1"://启动agv的时候
//					//开启
//					HardwareHelper.startAgv();
//					//等待结束
//					HardwareHelper.waitAgv();
//				break;
//				case "2"://启动机械臂的时候
//					//开启
//					HardwareHelper.startMachineArm1();
//					//等待结束
//					HardwareHelper.waitMachineArm1();
//				break;
//				case "3"://启动车床的时候
//					//启动
//					HardwareHelper.startMachineLathe1();
//					//等待结束
//					HardwareHelper.waitMachineLathe1();
//					break;
//			default:
//				break;
//			}
//		}
//		
//		
//		
//		
//	}
//	
//	
//}

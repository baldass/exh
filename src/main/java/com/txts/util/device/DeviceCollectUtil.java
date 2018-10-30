package com.txts.util.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.txts.task.CollectUtil;

/**
 * @description 
 * 
 * @author lfy
 * @time 2018年10月11日-下午10:19:38
 */
@Component
public class DeviceCollectUtil {
	
	@Autowired
	private CollectUtil collectUtil;
	//设备状态 添加volatile修饰符 修改即时生效
	private volatile List<Map<String,Object>> deviceStatus=new ArrayList<>();
	//device_name device_status
	/**
	 * 启动设备节拍监听
	 * 
	 * @author lfy
	 * @time 2018年10月11日-下午10:54:23
	 */
	public void startDeviceCollect(){
		HashMap hm = new HashMap();
		DeviceCollectRunnable run = new DeviceCollectRunnable(
				collectUtil, "测试设备", 1, 1, 1, 1,hm);
		
	}
	public  DeviceCollectUtil(){
		HashMap hm1 = new HashMap();
		hm1.put("device_name", "上料工位");
		hm1.put("device_status", "2");
		HashMap hm2 = new HashMap();
		hm2.put("device_name", "机床工位");
		hm2.put("device_status", "2");
		HashMap hm3 = new HashMap();
		hm3.put("device_name", "打钉工位");
		hm3.put("device_status", "2");
		HashMap hm4 = new HashMap();
		hm4.put("device_name", "视觉检测工位");
		hm4.put("device_status", "2");
		HashMap hm5 = new HashMap();
		hm5.put("device_name", "包装工位");
		hm5.put("device_status", "2");
		deviceStatus.add(hm5);
		deviceStatus.add(hm4);
		deviceStatus.add(hm3);
		deviceStatus.add(hm2);
		deviceStatus.add(hm1);
	}
	public List<Map<String,Object>> getStatus(){
		return deviceStatus;
	}
	/**
	 * 获取状态
	 * @author lfy
	 * @time 2018年10月16日-下午1:43:12
	 * @param key
	 * @return
	 */
	public String getDeviceStatus(String key){
		for (Map<String, Object> map : deviceStatus) {
			if(key.equals(map.get("device_name"))){
				String s = map.get("device_status").toString();
				if(s.equals("1")){
					//运行
					return "启动";
				}else if(s.equals("2")){
					//关闭
					return "关闭";
				}else {
					//异常
					return "异常";
				}
			}
		}
		return null;
	}
	/**
	 * 设置状态
	 * @author lfy
	 * @time 2018年10月16日-下午1:43:02
	 * @param key
	 * @param value
	 */
	public void setStatus(String key ,Object value){
		for (Map<String, Object> map : deviceStatus) {
			if(key.equals(map.get("device_name"))){
				map.put("device_status", value);
			}
		}
	}
}

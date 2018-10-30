package com.txts.util;
/**
 * 生成Json格式的数据
 * @author 40857
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtils {
	private static Map<String, String> map;
	private static String[] attrs;
	static {

		attrs = new String[] { "0", "device_id", "status_name", "handle", "device", "status" };
	}

	private JsonUtils() {
	}

	/**
	 * 生成单个json数据
	 * 
	 * @param map
	 * @return
	 */
	public static String makeJsonData(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"");
		int length = attrs.length;
		for (int i = 0; i < length; i++) {
			sb.append(attrs[i]).append("\":\"").append(map.get(attrs[i])).append("\"");
			if (i != length - 1) {
				sb.append(",");
			}
		}
		sb.append("}");
		return sb.toString();
	}

	public static String makeJsonData() {
		StringBuilder sb = new StringBuilder();
		Map<String, String> testMap = new HashMap<>();
		testMap.put("0", "false");
		testMap.put("device_id", "-1");
		testMap.put("status_name", "正常");
		testMap.put("handle", "测试数据");
		testMap.put("device", "机械臂1");
		testMap.put("status", "1");
		return makeJsonData(testMap);
	}

	public static String makeJsonList(List<Map<String, String>> list) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		Iterator<Map<String, String>> itr = list.iterator();
		while (itr.hasNext()) {
			Map<String, String> item = itr.next();
			sb.append(makeJsonData(item));
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 测试
	 * 
	 * @param args
	 */
//	public static void main(String[] args) {
//		List list = new ArrayList<Map<String, String>>();
//		Map<String, String> testMap = new HashMap<>();
//		testMap.put("0", "false");
//		testMap.put("device_id", "-1");
//		testMap.put("status_name", "正常");
//		testMap.put("handle", "测试数据");
//		testMap.put("device", "机械臂1");
//		testMap.put("status", "1");
//		list.add(testMap);
//		Map<String, String> testMap2 = new HashMap<>();
//		testMap2.put("0", "false");
//		testMap2.put("device_id", "-1");
//		testMap2.put("status_name", "正常");
//		testMap2.put("handle", "测试数据2");
//		testMap2.put("device", "机械臂1");
//		testMap2.put("status", "1");
//		list.add(testMap2);
//		System.out.println(makeJsonList(list));
//	}
}

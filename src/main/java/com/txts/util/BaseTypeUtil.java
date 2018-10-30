package com.txts.util;
/** 
 * 基础类型工具类
 * @author lfy
 * @time 2018年9月5日-下午4:29:35
 */
public class BaseTypeUtil {
	/**
	 * 非空判断
	 * @time 2018年8月2日-下午2:06:15
	 * @author lfy
	 */
	public static boolean isNotNull(Object o){
		if(o==null)
			return false;
		if(o.equals(""))
			return false;
		return true;
	}
	/**
	 * 空判断
	 * @time 2018年8月2日-下午2:06:15
	 * @author lfy
	 */
	public static boolean isNull(Object o){
		if(o==null)
			return true;
		if(o.equals(""))
			return true;
		return false;
	}
	/**
	 * 转字符串
	 * 
	 * @author lfy
	 * @time 2018年8月17日-下午5:02:31
	 * @param o
	 * @return
	 */
	public static String toStr(Object o ){
		if(o==null)
			return "";
		return o.toString();
		
	}
	/**
	 * 转integer
	 * 
	 * @author lfy
	 * @time 2018年8月30日-下午2:41:40
	 * @param o
	 * @return
	 */
	public static Integer toInteger(Object o){
		if(o==null)
			return null;
		return Integer.valueOf(o.toString());
	}
}

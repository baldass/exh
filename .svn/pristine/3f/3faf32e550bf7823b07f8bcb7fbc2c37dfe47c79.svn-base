package com.txts.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;

import com.txts.util.RequestUtil;

/** 
 * @time 2018年8月1日-下午3:43:39
 * @author lfy
 */
@SuppressWarnings("all")
public class BaseController {

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;

	/**
	 * 获取url
	 * 
	 * @time 2018年8月1日-下午3:44:55
	 * @author lfy
	 */
	public static String getURLPath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
	}

	/**
	 * 用@ModelAttribute 赋值请求变量、响应变量、会话变量
	 * 
	 * @time 2018年8月1日-下午3:45:02
	 * @author lfy
	 */
	@ModelAttribute
	public void setAttribute(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.session = request.getSession();
	}
	/**
	 * 非空判断
	 * @time 2018年8月2日-下午2:06:15
	 * @author lfy
	 */
	public boolean isNotNull(Object o){
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
	public boolean isNull(Object o){
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
	public String toStr(Object o ){
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
	public Integer toInteger(Object o){
		if(o==null)
			return null;
		return Integer.valueOf(o.toString());
	}
	/**
	 * 获取参数map
	 * 
	 * @author lfy
	 * @time 2018年8月29日-下午3:10:38
	 * @return
	 */
	public Map getRequestMap(){
		return  RequestUtil.getParameterMap(request);
	}
	/**
	 * 获取登录信息map
	 * 
	 * @author lfy
	 * @time 2018年8月29日-下午3:11:11
	 * @return
	 */
	public Map getLoginUser(){
		Map m = new HashMap();
		m.put("login_user_id","-1");
		m.put("login_user_name","test_user");
		return m;
	}
	/**
	 * 获取登录信息和参数信息的map
	 * 
	 * @author lfy
	 * @time 2018年8月29日-下午3:10:48
	 * @return
	 */
	public Map getRequestAndLoginMap(){
		  Map map = RequestUtil.getParameterMap(request);
		  map.putAll(getLoginUser());
		  return map;
	}
	/**
	 * 获取登录信息和参数信息的map并且整理页码属性 可以方便直接进入sql使用
	 * 
	 * @author lfy
	 * @time 2018年8月29日-下午3:10:48
	 * @return
	 */
	public Map getRequestPageAndLoginMap(){
		  Map map = RequestUtil.getParameterMap(request);
		  map.putAll(getLoginUser());
		  Integer page=toInteger(map.get("page"));
		  Integer rows=toInteger(map.get("rows"));
		  map.put("pageOffset", (page-1)*rows);
		  map.put("pageRows", rows);
		  
		  return map;
	}
}

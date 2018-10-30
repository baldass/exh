package com.txts.exh.console.service;

import java.util.List;
import java.util.Map;

import com.vo.BootstrapTable;

/**
 * @description 控制台
 * 
 * @author lfy
 * @time 2018年9月11日-上午8:52:31
 */
public interface ConsoleService {
	
	public Object test();
	/**
	 * 获取调度任务
	 * 
	 * @author lfy
	 * @time 2018年9月19日-下午2:38:20
	 * @param m
	 * @return
	 */
	public BootstrapTable<Map<String, Object>> getDispatch(Map<String, Object> m);
	/**
	 * 获取调度任务
	 * 
	 * @author lfy
	 * @time 2018年9月19日-下午2:38:20
	 * @param m
	 * @return
	 */
	public List<Map<String,Object>> getDispatchList(Map<String, Object> m);
	/**
	 * 更新调度任务详情
	 * 
	 * @author lfy
	 * @time 2018年9月19日-下午2:38:20
	 * @param m
	 * @return
	 */
	public Integer updateDispatchDetail(Map<String, Object> m);
	/**
	 * 获取调度任务详情
	 * 
	 * @author lfy
	 * @time 2018年9月19日-下午2:38:20
	 * @param m
	 * @return
	 */
	public String getDispatchDetail(Map<String, Object> m);
	/**
	 * 获取工单（从mes）
	 * 
	 * @author lfy
	 * @time 2018年10月10日-下午2:38:20
	 * @param m
	 * @return
	 */
	public BootstrapTable<Map<String, Object>> getPickerOrder(Map<String, Object> m);
	/**
	 * 开工
	 * 
	 * @author lfy
	 * @time 2018年10月10日-下午5:54:37
	 * @return
	 */
	public int orderStart(Map m);
}

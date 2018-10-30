package com.txts.exh.console.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.txts.exh.console.beans.Mozc;
import com.txts.exh.console.service.SqlserverConnectService;

/**
 * sqlserver数据库链接
 * 
 * @author 40857
 *
 */
@Controller
@RequestMapping("sqlserver")
public class SqlserverConnectController {
	@Autowired
	private SqlserverConnectService scs;

	@ResponseBody
	@RequestMapping("test")
	public Object text() {
		return scs.test();
	}

	/**
	 * 获取所有要执行的工单
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("get_execute")
	public List<Mozc> getExecuteMo() {
		List<Mozc> list = scs.getExecuteMo();
		return list;
	}

	/**
	 * 获取本产线需要执行的工单信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("get_zc_list")
	public List<String> getMozcLists() {
		List<String> lists = scs.getMozcLists();
		return lists;
	}

	/**
	 * 通过工单号获取任务池
	 * 
	 * @param tzdh
	 *            通知单号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("get_task_pool")
	public List<Mozc> getTaskPool(String tzdh) {
		List<Mozc> list = scs.getTaskPool(tzdh);
		// 对查询出来的list进行排序
		list = sortList(list);
		return list;
	}

	/**
	 * 对任务池中的数据进行排序
	 * 
	 * @param list
	 * @return
	 */
	private List<Mozc> sortList(List<Mozc> list) {
		Iterator<Mozc> iterator = list.iterator();
		List<Mozc> cache = new ArrayList<Mozc>();
		List<Mozc> result = new ArrayList<Mozc>();
		while (iterator.hasNext()) {
			Mozc item = iterator.next();
			if (item.getUpzc() == null) {// 找到起始制程
				// 放入result
				result.add(item);
				continue;
			}
			// 非起始制程放入cache
			cache.add(item);
		}
		// 循环cache排序
		int size = cache.size();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Mozc item = result.get(result.size() - 1);
				Mozc zc = cache.get(j);
				// 如果没有转下制程则跳出
				if (item.getDownzc() == null || item.getDownzc().equals(""))
					break;
				if (item.getDownzc().equals(zc.getMo_zcnum())) {
					result.add(zc);
					// cache.remove(zc);
					break;
				}
			}
		}
		return result;
	}

}

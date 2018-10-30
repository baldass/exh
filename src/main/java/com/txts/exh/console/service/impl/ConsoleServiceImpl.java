package com.txts.exh.console.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.txts.exh.console.service.ConsoleService;
import com.txts.task.CollectUtil;
import com.txts.util.CommonDao;
import com.vo.BootstrapTable;

/**
 * @description 控制台
 * 
 * @author lfy
 * @time 2018年9月11日-上午8:54:28
 */
@SuppressWarnings("all")
@Service
public class ConsoleServiceImpl implements ConsoleService {
	
	@Autowired
	private CommonDao dao;
	@Autowired
	private CollectUtil collectUtil;
	@Override
	public Object test() {
		return dao.findList("consoleMapper.test", null);
	}
	@Override
	public BootstrapTable<Map<String, Object>> getDispatch(Map<String, Object> m) {
		Integer size = (Integer)dao.findOne("consoleMapper.getDispatchCount", null);
		List<Map<String, Object>> list = dao.findList("consoleMapper.getDispatch", m);
		BootstrapTable<Map<String, Object>> b = new BootstrapTable<Map<String, Object>>(list,size);
		return b;	
	}
	@Override
	public Integer updateDispatchDetail(Map<String, Object> m) {
		return dao.update("consoleMapper.updateDispatchDetail", m);
	}
	@Override
	public List<Map<String,Object>>  getDispatchList(Map<String, Object> m) {
		
		return dao.findList("consoleMapper.getDispatch", m);
	}
	@Override
	public String getDispatchDetail(Map<String, Object> m) {
		
		return (String) dao.findOne("consoleMapper.getDispatchDetail", m);
	}
	@Override
	public BootstrapTable<Map<String, Object>> getPickerOrder(Map<String, Object> m) {
		Integer size = (Integer)dao.findOne("collectMapper.getPickerOrderNum", m);
		List<Map<String, Object>> list = dao.findList("collectMapper.getPickerOrder", m);
		BootstrapTable<Map<String, Object>> b = new BootstrapTable<Map<String, Object>>(list,size);
		return b;	
	}
	@Override
	public int orderStart(Map m) {
		m.put("mo_state", "在产");
		collectUtil.updateMesMo(m);
		m.put("emd_order", m.get("mo_no"));
		m.put("emd_desc", m.get("mc_name"));
		m.put("emd_sl", Integer.valueOf( m.get("emd_sl").toString()));
		collectUtil.insertMoDetail(m);
		return 1;
	}

}

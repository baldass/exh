package com.txts.exh.console.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.txts.exh.console.beans.Mozc;
import com.txts.exh.console.beans.StorageItem;
import com.txts.exh.console.service.SqlserverConnectService;
import com.txts.util.CommonDao;

@Service
public class SqlserverConncetServiceImpl implements SqlserverConnectService {
	@Autowired
	private CommonDao commonDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<Mozc> test() {
		return commonDao.findList("sqlserver.getMoList", null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Mozc> getExecuteMo() {
		return commonDao.findList("sqlserver.get_execute", null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getMozcLists() {
		return commonDao.findList("sqlserver.get_mozc_lists", null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Mozc> getTaskPool(String tzdh) {
		return commonDao.findList("sqlserver.get_task_pool", tzdh);
	}

	@SuppressWarnings("unchecked")
	public List<StorageItem> getWarehousDetails() {
		return commonDao.findList("sqlserver.get_3d_warehouse", null);
	}

	@Override
	public Map<String, String> getMaterNum(String orderName) {
		// TODO Auto-generated method stub
		return (Map<String, String>) commonDao.findOne("sqlserver.get_mater_num", orderName);
	}

	@Override
	public void updateMO(String orderName) {
		// TODO Auto-generated method stub
		commonDao.update("sqlserver.update_mo", orderName);
	}

	@Override
	public int inStorage(Integer id, String qrCode) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		map.put("qrCode",qrCode);
		return commonDao.update("sqlserver.in_storage", map);
	}

	@Override
	public int renew() {
		// TODO Auto-generated method stub
		return commonDao.update("sqlserver.renew",null);
	}
	
}

package com.txts.exh.console.service;

import java.util.List;
import java.util.Map;

import com.txts.exh.console.beans.Mozc;
import com.txts.exh.console.beans.StorageItem;

public interface SqlserverConnectService {
	public Object test();

	public List<Mozc> getExecuteMo();

	public List<String> getMozcLists();

	public List<Mozc> getTaskPool(String tzdh);

	public List<StorageItem> getWarehousDetails();

	public Map<String, String> getMaterNum(String orderName);

	public void updateMO(String orderName);

	public int inStorage(Integer id, String qrCode);
}

package com.txts.exh.console.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.txts.exh.console.service.SqlserverConnectService;

/**
 * 设备控制类 不想改数据库了,直接在类里面声明
 * 
 * @author 40857
 *
 */
@Controller
@RequestMapping("/project")
public class DeviceController {
	@Autowired
	private SqlserverConnectService ss;

	@RequestMapping("/all")
	@ResponseBody
	public String getDetails() {
		return null;
	}

	@RequestMapping("/control")
	@ResponseBody
	public boolean deviceController(Integer name, Integer value) {
		return false;
	}

	@RequestMapping("/renew")
	@ResponseBody
	public boolean Renew() {
		int renew = ss.renew();
		if (renew > 0)
			return true;
		else
			return false;
	}
}

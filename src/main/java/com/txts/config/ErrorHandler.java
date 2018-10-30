package com.txts.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 异常的统一返回
 * 
 * @author lfy
 * @time 2018年8月24日-下午4:38:46
 */
//@Controller
public class ErrorHandler implements ErrorController {
	private static final String ERROR_PATH = "/error";

	
	@RequestMapping(value = ERROR_PATH)
	@ResponseBody
	public String handleError(HttpServletResponse response) {
		response.setStatus(200);//设置为正常响应
		return "<h5>服务器繁忙..<h5>";
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
}
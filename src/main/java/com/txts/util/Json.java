package com.txts.util;

import java.io.Serializable;

/**
 * @description 
 * 
 * @author lfy
 * @time 2018年8月17日-下午3:46:33
 */
public class Json implements Serializable{
	private static final long serialVersionUID = 1L;
	//结果
	private boolean result;
	//文本描述
	private String msg;
	//错误码
	private String code;
	//数据
	private Object data;
	
	public Json() {}
	
	public Json(boolean result, String msg, String code, Object data) {
		super();
		this.result = result;
		this.msg = msg;
		this.code = code;
		this.data = data;
	}
	public Json(boolean result, String msg, Object data) {
		super();
		this.result = result;
		this.msg = msg;
		this.data = data;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}

package com.vo;

import java.util.List;
import java.util.Map;

/**
 * 与bootstrapTable 所配套的返回值
 * 
 * @author lfy
 * @time 2018年8月27日-上午11:05:20
 * @param <Z>
 */
public class BootstrapTable<Z> {

	// bootstrapTable所需属性

	private Integer total;

	private List<Z> rows;

	private Map<String, Object> extData;// 自定义扩展属性

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<Z> getRows() {
		return rows;
	}

	public void setRows(List<Z> rows) {
		this.rows = rows;
	}

	public Map<String, Object> getExtData() {
		return extData;
	}

	public void setExtData(Map<String, Object> extData) {
		this.extData = extData;
	}

	public BootstrapTable() {
	}

	public BootstrapTable(List<Z> rows, Integer total) {
		super();
		this.rows = rows;
		this.total = total;
	}

	@Override
	public String toString() {
		return "BootstrapTable [total=" + total + ", rows=" + rows + ", extData=" + extData + "]";
	}

}

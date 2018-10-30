package com.txts.exh.console.beans;

import java.util.Date;

/**
 * 制令单
 * @author 40857
 *
 */
public class Mozc {
	private Integer id;
	private String mo_num;//制令单号
	private String mo_tzdh;//通知单号
	private String mo_zcnum;//工序代号
	private String mo_mcid;//产品编号
	private Integer mo_sl;//数量
	private Integer mo_end_sl;//完工数量
	private String mo_class;//生产班组 
	private Integer ng_sl;//不良数量
	private Integer mo_pass_sl;//合格数量
	private Integer state;//报工状态
	private Integer c_state;//操作状态
	private String upzc;//承上制程
	private String downzc;//转下制程
	private Integer mo_order;//工序排序
	private Date mo_date;//制令单日期
	private Integer mo_isend;//是否已完工（机器回写）
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMo_num() {
		return mo_num;
	}
	public void setMo_num(String mo_num) {
		this.mo_num = mo_num;
	}
	public String getMo_tzdh() {
		return mo_tzdh;
	}
	public void setMo_tzdh(String mo_tzdh) {
		this.mo_tzdh = mo_tzdh;
	}
	public String getMo_zcnum() {
		return mo_zcnum;
	}
	public void setMo_zcnum(String mo_zcnum) {
		this.mo_zcnum = mo_zcnum;
	}
	public String getMo_mcid() {
		return mo_mcid;
	}
	public void setMo_mcid(String mo_mcid) {
		this.mo_mcid = mo_mcid;
	}
	public Integer getMo_sl() {
		return mo_sl;
	}
	public void setMo_sl(Integer mo_sl) {
		this.mo_sl = mo_sl;
	}
	public Integer getMo_end_sl() {
		return mo_end_sl;
	}
	public void setMo_end_sl(Integer mo_end_sl) {
		this.mo_end_sl = mo_end_sl;
	}
	public String getMo_class() {
		return mo_class;
	}
	public void setMo_class(String mo_class) {
		this.mo_class = mo_class;
	}
	public Integer getNg_sl() {
		return ng_sl;
	}
	public void setNg_sl(Integer ng_sl) {
		this.ng_sl = ng_sl;
	}
	public Integer getMo_pass_sl() {
		return mo_pass_sl;
	}
	public void setMo_pass_sl(Integer mo_pass_sl) {
		this.mo_pass_sl = mo_pass_sl;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getC_state() {
		return c_state;
	}
	public void setC_state(Integer c_state) {
		this.c_state = c_state;
	}
	public String getUpzc() {
		return upzc;
	}
	public void setUpzc(String upzc) {
		this.upzc = upzc;
	}
	public String getDownzc() {
		return downzc;
	}
	public void setDownzc(String downzc) {
		this.downzc = downzc;
	}
	public Integer getMo_order() {
		return mo_order;
	}
	public void setMo_order(Integer mo_order) {
		this.mo_order = mo_order;
	}
	public Date getMo_date() {
		return mo_date;
	}
	public void setMo_date(Date mo_date) {
		this.mo_date = mo_date;
	}
	public Integer getMo_isend() {
		return mo_isend;
	}
	public void setMo_isend(Integer mo_isend) {
		this.mo_isend = mo_isend;
	}
	@Override
	public String toString() {
		return "Mozc [id=" + id + ", mo_num=" + mo_num + ", mo_tzdh=" + mo_tzdh + ", mo_zcnum=" + mo_zcnum
				+ ", mo_mcid=" + mo_mcid + ", mo_sl=" + mo_sl + ", mo_end_sl=" + mo_end_sl + ", mo_class=" + mo_class
				+ ", ng_sl=" + ng_sl + ", mo_pass_sl=" + mo_pass_sl + ", state=" + state + ", c_state=" + c_state
				+ ", upzc=" + upzc + ", downzc=" + downzc + ", mo_order=" + mo_order + ", mo_date=" + mo_date
				+ ", mo_isend=" + mo_isend + "]";
	}
		
}

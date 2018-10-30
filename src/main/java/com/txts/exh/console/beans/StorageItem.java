package com.txts.exh.console.beans;

/**
 * 立体仓库实体类
 * 
 * @author 40857
 *
 */
public class StorageItem {
	private Integer id;// 储位号码
	private String rfid;// 储存的物品编号
	private String name;
	private String qr_code;
	private boolean state;// 储存状态 true 有物品 ,false没有物品

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRfid() {
		return rfid;
	}

	public void setRfid(String rfid) {
		this.rfid = rfid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getQr_code() {
		return qr_code;
	}

	public void setQr_code(String qr_code) {
		this.qr_code = qr_code;
	}

}

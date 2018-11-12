package com.txts.exh.console.beans;

import org.springframework.stereotype.Component;

/**
 * 仓库类线程 出库和入库
 *
 */
@Component
public class OutStorage extends Thread {
	private static OutStorage instance;

	private OutStorage() {
		this.state=false;
		this.model=0;
	}
	
	public static synchronized OutStorage getInstance() {
		if (instance == null) {//单例模式
			instance = new OutStorage();
		}
		return instance;
	}

	/**
	 * 运行状态
	 */
	private volatile static boolean state;
	/**
	 * 0 1 2
	 */
	private volatile static int model;

	@Override
	public void run() {
		super.run();
		while(true) {
			try {
				
			}catch(Exception e) {
				
			}finally {
				
			}
		}
	}

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}

	public boolean isState() {
		return state;
	}
}

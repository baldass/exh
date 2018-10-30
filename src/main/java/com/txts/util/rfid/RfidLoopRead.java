package com.txts.util.rfid;

import org.springframework.stereotype.Component;

import com.jlrfid.service.*;

@Component
public class RfidLoopRead implements GetReadData{
	
	//public Long lastTime;//最后读取的时间戳
	public volatile boolean isRead;//volatile修改马上生效
	public String lastValue;//最后的值
	public String getValue() throws Exception{
		while(isRead){//当已被读取的时候 进入循环
			Thread.sleep(500);
		}
		isRead= true;
		return lastValue;
	}
	/**
	 * @param args
	 * @throws RFIDException 
	 */
	public static void main(String[] args) throws RFIDException {
		start(7);
	}
	/**
	 * 需要传入com口的数值
	 * 
	 * @author lfy
	 * @time 2018年10月10日-上午10:02:18
	 * @param port
	 * @throws RFIDException 
	 */
	public static void start(int port) throws RFIDException{
		MainHandler handler = new MainHandler();
		if(handler.dllInit("D:\\Disdll.dll")){
			if(handler.deviceInit("",port, 9600)){
				//System.out.println(handler.StopInv());
				handler.setSingleParameter((char)0x70, (char)2);
				handler.BeginInv(new RfidLoopRead());
			}
		}
	}
	public void getReadData(String data, int antNo) {
		System.out.println("rfid扫描器读取到值:"+data);
		synValue(data, 0);//设置值
	}
	public synchronized String synValue(String val,int type){
		if(type == 1){
			String temp=lastValue;
			lastValue=null;
			return temp;
		}
		else{
			lastValue =val;
			return null;
		}
	}
	public String waitVal() throws Exception{
		while(true){
			String val = synValue(null,1);
			if(val != null){
				return val;
			}
			Thread.sleep(1000);//1秒读一次
		}
		
	}
}

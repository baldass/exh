package com.txts.util.visio;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.stereotype.Component;

import sun.applet.Main;

/**
 * @description
 * 
 * @author lfy
 * @time 2018年10月11日-下午10:20:29
 */
@Component
public class VisioUtil {
	
	public volatile boolean isRead;//volatile修改马上生效
	
	public volatile String lastValue;//最后的值 volatile修改马上生效
	/**
	 * 同步读写值
	 * 
	 * @author lfy
	 * @time 2018年10月15日-下午6:09:23
	 * @param val
	 * @param type
	 * @return
	 */
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
	public Integer waitVal() throws Exception{
		while(true){
			String val = synValue(null,1);
			if(val != null){
				return Integer.valueOf(val);
			}
			Thread.sleep(1000);//1秒读一次
		}
		
	}
	
	private ServerSocket serverSocket;
	
	public void init() throws Exception {
		serverSocket = new ServerSocket();
		serverSocket.setReuseAddress(true); // 设置 ServerSocket 的选项
		serverSocket.bind(new InetSocketAddress(8082));//占用此端口
		//开启数据接收的线程
		Runnable run = new VisioAcceptThread(serverSocket, this);
		new Thread(run).start();
	}



	public static void main(String[] args) throws Exception {
		VisioUtil vu = new VisioUtil();
		vu.init();
		while (true) {
			System.out.println("read:" + vu.waitVal());
		}
	}
}

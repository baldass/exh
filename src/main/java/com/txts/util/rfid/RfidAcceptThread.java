package com.txts.util.rfid;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description
 * 
 * @author lfy
 * @time 2018年10月15日-下午5:55:22
 */
public class RfidAcceptThread implements Runnable {
	ServerSocket serverSocket;
	RfidUtil rfid;

	@Override
	public void run() {
		while (true) {
			try {
				// 等待接收新的数据
				Socket client = serverSocket.accept();
				System.out.println("有对象连接到服务器");
				new Thread(new Runnable() {
					// 每接到一个socket对象 就新建一个线程
					@Override
					public void run() {
						System.out.println("开始监听tcpip对象发送的数据");
						while (true) {
							try {
								// 判断是否已关闭
								
								
								InputStream is = client.getInputStream();
								// 此方法会阻塞线程 直到读取到数据
								byte[] data = getReqData(is);
								String res = "";
								for (byte b : data) {
									String hex = Integer.toHexString(b & 0xFF);  
								    if(hex.length() < 2){  
								        hex = "0" + hex;  
								    }  
									res+=hex;
								}
								System.out.println("rfid获得了新值:"+res);
								// 设置值
								rfid.synValue(res, 0);//设置值
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
				}).start();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public RfidAcceptThread(ServerSocket serverSocket, RfidUtil rfid) {
		super();
		this.serverSocket = serverSocket;
		this.rfid = rfid;
	}

	public byte[] getReqData(InputStream is) throws Exception {
		while (true) {
			Thread.sleep(1000);
			int available = is.available();
			if (available <= 0) {
				continue;
			}
			// Thread.sleep(500);//延迟一下在读 防止数据未完全传输
			try {
				byte[] b = new byte[available];
				is.read(b);
				return b;
			} catch (Exception e) {
			}
			return null;
		}
	}

}

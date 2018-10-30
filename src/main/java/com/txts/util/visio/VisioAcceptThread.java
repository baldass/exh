package com.txts.util.visio;

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
public class VisioAcceptThread implements Runnable {
	ServerSocket serverSocket;
	VisioUtil visio;

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
								String res = new String(data);
								System.out.println("视觉扫描 读取到值:"+res);
								// 设置值
								visio.synValue(res, 0);//设置值
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

	public VisioAcceptThread(ServerSocket serverSocket, VisioUtil visio) {
		super();
		this.serverSocket = serverSocket;
		this.visio = visio;
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

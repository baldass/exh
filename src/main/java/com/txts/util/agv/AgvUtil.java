package com.txts.util.agv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import org.springframework.stereotype.Component;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * @description
 * 
 * @author lfy
 * @time 2018年10月11日-下午5:42:24
 */
@Component
public class AgvUtil implements SerialPortEventListener {
	public static void main(String[] args) throws Exception {
		AgvUtil avg = new AgvUtil();
		avg.init("COM5");
//		avg.start1();
		System.out.println("COM5");
		avg.start2();
		//System.out.println("wait repait");
		avg.waitend2();
		System.out.println("end1..");
	
	}
	// 检测系统中可用的通讯端口类
	private CommPortIdentifier portId;
	// 枚举类型
	private Enumeration<CommPortIdentifier> portList;

	// RS232串口
	private SerialPort serialPort;

	// 输入输出流
	private InputStream inputStream;
	private OutputStream outputStream;

	// 保存串口返回信息
	private String test = "";

	// 单例创建
	private static AgvUtil uniqueInstance = new AgvUtil();

	// 初始化串口
	@SuppressWarnings("unchecked")
	public void init(String comName) {
		// 获取系统中所有的通讯端口
		portList = CommPortIdentifier.getPortIdentifiers();
		// 循环通讯端口
		while (portList.hasMoreElements()) {
			portId = portList.nextElement();
			// 判断是否是串口
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				// 比较串口名称是否是"COM1"
				if (comName.equals(portId.getName())) {
					System.out.println("找到串口"+comName);
					// 打开串口
					try {
						// open:（应用程序名【随意命名】，阻塞时等待的毫秒数）
						serialPort = (SerialPort) portId.open(Object.class.getSimpleName(), 2000);
						System.out.println("获取到串口对象,"+comName);
						// 设置串口监听
						serialPort.addEventListener(this);
						// 设置串口数据时间有效(可监听)
						serialPort.notifyOnDataAvailable(true);
						// 设置串口通讯参数
						// 波特率，数据位，停止位和校验方式
						// 波特率2400,偶校验
						serialPort.setSerialPortParams(38400, SerialPort.DATABITS_8, //
								SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
						test = "";
						outputStream = serialPort.getOutputStream();
					} catch (PortInUseException e) {
						e.printStackTrace();
					} catch (TooManyListenersException e) {
						e.printStackTrace();
					} catch (UnsupportedCommOperationException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		}
	}

	public void start1() throws Exception {
		System.out.println("发送信号");
		byte[] readBuffer = new byte[1024];
		try {
			OutputStream outStream = serialPort.getOutputStream();
			byte[]b = new byte[]{
					-29,//帧头
					1,//对接码  1/2
					1,//AGV编号
					1,//主发01 接收02
					2,//指令 2/4
					0,//备用 暂无数据
					0,//校验码 - 前面所有字节的亦或码
					3//尾码
			};
			//校验码(亦或)
			b[6]=(byte) (b[0]^b[1]^b[2]^b[3]^b[4]^b[5]);
			//发送数据
			outStream.write(b);
			outputStream.flush();
			System.out.println("发送信号完成");
			inputStream = serialPort.getInputStream();
			// 从线路上读取数据流
			int len = 0;
			while ((len = inputStream.read(readBuffer)) > 0) {
				Thread.sleep(1000);
				System.out.println("等待返回");
				//test += new String(readBuffer, 0, len).trim();
					if(readBuffer[3]==(byte)02){
						break;
					}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	

	public void start2() {
		System.out.println("发送信号");
		byte[] readBuffer = new byte[1024];
		try {
			OutputStream outStream = serialPort.getOutputStream();
			byte[]b = new byte[]{
					-29,//帧头
					2,//对接码  1/2
					1,//AGV编号
					1,//主发01 接收02
					4,//指令 2/4
					0,//备用 暂无数据
					0,//校验码 - 前面所有字节的亦或码
					3//尾码
			};
			//校验码(亦或)
			b[6]=(byte) (b[0]^b[1]^b[2]^b[3]^b[4]^b[5]);
			//发送数据
			outStream.write(b);
			outputStream.flush();
			System.out.println("发送信号完成");
			inputStream = serialPort.getInputStream();
			// 从线路上读取数据流
			int len = 0;
			while ((len = inputStream.read(readBuffer)) > 0) {
				//test += new String(readBuffer, 0, len).trim();
				if(readBuffer[3]==(byte)01){
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*public void waitend() throws Exception {
		byte[] readBuffer = new byte[1024];
		inputStream.read(readBuffer);
		Thread.sleep(10000);
		//inputStream.reset();
		try {
			inputStream = serialPort.getInputStream();
			// 从线路上读取数据流
			int len = 0;
			boolean flag = true;
			while (flag) {
				System.out.println("等待中...");
				Thread.sleep(1000);
				while ((len = inputStream.read(readBuffer)) > 0) {
					Thread.sleep(1000);
					System.out.println("等待返回");
					//test += new String(readBuffer, 0, len).trim();
					if(readBuffer[3]==(byte)01){
						break;
					}
				}
				System.out.println("等待收到数据");
				//test += new String(readBuffer, 0, len).trim();
				for (int i = 0; i < len; i++) {
					byte a = (byte) readBuffer[i];
					int ix = a;
					ix = a & 0xff;
					System.out.println(ix);
				}
				
				System.out.println("end read.");
				byte[] b = new byte[] { -29, // 帧头
						readBuffer[1], // 对接码 1/2
						1, // AGV编号
						2, // 主发01 接收02
						10, // 指令 2/10
						0, // 备用 暂无数据
						-29, // 校验码 - 前面所有字节的亦或码
						3// 尾码
				};

				// 校验码(亦或)
				b[6] = (byte) (b[0] ^ b[1] ^ b[2] ^ b[3] ^ b[4] ^ b[5]);
				outputStream.write(b);
				outputStream.flush();
				
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	@Override
	public void serialEvent(SerialPortEvent arg0) {
		
		
	}
	public void waitend2() throws Exception {
		byte[] readBuffer = new byte[1024];
		Thread.sleep(2000);
		inputStream = serialPort.getInputStream();
		if(inputStream.available()>0){
			inputStream.read(readBuffer);
		}
		Thread.sleep(10000);
		try {
			//inputStream = serialPort.getInputStream();
			// 从线路上读取数据流
			int len = 0;
			boolean flag = true;
			while (flag) {
				System.out.print(".");
				Thread.sleep(1000);
				if((len = inputStream.read(readBuffer)) <= 0){
					continue;
				}
				System.out.println("");
				System.out.println("收到数据");
				test += new String(readBuffer, 0, len).trim();
				for (int i = 0; i < len; i++) {
					byte a = (byte) readBuffer[i];
					int ix = a;
					ix = a & 0xff;
					System.out.println(ix);
				}

				System.out.println("end read.");
				byte[] b = new byte[] { -29, // 帧头
						readBuffer[1], // 对接码 1/2
						1, // AGV编号
						2, // 主发01 接收02
						10, // 指令 2/10
						0, // 备用 暂无数据
						-29, // 校验码 - 前面所有字节的亦或码
						3// 尾码
				};

				// 校验码(亦或)
				b[6] = (byte) (b[0] ^ b[1] ^ b[2] ^ b[3] ^ b[4] ^ b[5]);
				outputStream.write(b);
				outputStream.flush();
				
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}

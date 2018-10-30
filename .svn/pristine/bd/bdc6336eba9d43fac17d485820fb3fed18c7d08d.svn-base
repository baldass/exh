//package com.txts.util;
//
//import HslCommunication.Core.Types.OperateResult;
//import HslCommunication.Core.Types.OperateResultExOne;
//import HslCommunication.Profinet.Melsec.MelsecMcNet;
//
///**
// * @description
// * 
// * @author lfy
// * @time 2018年9月26日-上午10:38:05
// */
//public class PlcUtil {
//
//	public static void main(String[] args) {
//		// 如果想使用本组件的数据读取功能，必须先初始化数据访问对象，根据实际情况进行数据的填入。 下面仅仅是测试中的数据：
//		MelsecMcNet melsec_net = new MelsecMcNet("192.168.1.192", 6001);
//		// 然后你可以指定一些参数，网络号，网络站号之类的，通常的情况都是不需要指定的
//		melsec_net.setNetworkNumber((byte) 0x00);
//		melsec_net.setNetworkStationNumber((byte) 0x00);
//		melsec_net.setConnectTimeOut(1000);
//
//		// 打开连接
//		melsec_net.ConnectServer();
//		// 如果想知道有没有连接上去
//		OperateResult connectResult = melsec_net.ConnectServer();
//		if (connectResult.IsSuccess) {
//			System.out.print("连接成功");
//		} else {
//			System.out.print("连接失败：" + connectResult.Message);
//		}
//		// 关于地址的表示方式
//		// 使用字符串表示，这个组件里所有的读写操作提供字符串表示的重载方法，所有的支持访问的类型对应如下，字符串的表示方式存在十进制和十六进制的区别：
//		// 输入继电器："X100","X1A0" // 字符串为十六进制机制
//		// 输出继电器："Y100" ,"Y1A0" // 字符串为十六进制机制
//		// 内部继电器："M100","M200" // 字符串为十进制
//		// 锁存继电器："L100" ,"L200" // 字符串为十进制
//		// 报警器： "F100", "F200" // 字符串为十进制
//		// 边沿继电器："V100" , "V200" // 字符串为十进制
//		// 链接继电器："B100" , "B1A0" // 字符串为十六进制
//		// 步进继电器："S100" , "S200" // 字符串为十进制
//		// 数据寄存器："D100", "D200" // 字符串为十进制
//		// 链接寄存器："W100" ,"W1A0" // 字符串为十六进制
//		// 文件寄存器："R100","R200" // 字符串为十进制
//		// 简单读写的示例
//		boolean[] M100 = melsec_net.ReadBool("M100", (short) 1).Content; // 读取M100是否通，十进制地址
//		boolean[] X1A0 = melsec_net.ReadBool("X1A0", (short) 1).Content; // 读取X1A0是否通，十六进制地址
//		boolean[] Y1A0 = melsec_net.ReadBool("Y1A0", (short) 1).Content; // 读取Y1A0是否通，十六进制地址
//		boolean[] B1A0 = melsec_net.ReadBool("B1A0", (short) 1).Content; // 读取B1A0是否通，十六进制地址
//		short short_D1000 = melsec_net.ReadInt16("D1000").Content; // 读取D1000的short值
//																	// ,W3C0,R3C0
//																	// 效果是一样的
//		int int_D1000 = melsec_net.ReadInt32("D1000").Content; // 读取D1000-D1001组成的int数据
//		float float_D1000 = melsec_net.ReadFloat("D1000").Content; // 读取D1000-D1001组成的float数据
//		long long_D1000 = melsec_net.ReadInt64("D1000").Content; // 读取D1000-D1003组成的long数据
//		double double_D1000 = melsec_net.ReadDouble("D1000").Content; // 读取D1000-D1003组成的double数据
//		String str_D1000 = melsec_net.ReadString("D1000", (short) 10).Content; // 读取D1000-D1009组成的条码数据
//
//		melsec_net.Write("M100", new boolean[] { true }); // 写入M100为通
//		melsec_net.Write("Y1A0", new boolean[] { true }); // 写入Y1A0为通
//		melsec_net.Write("X1A0", new boolean[] { true }); // 写入X1A0为通
//		melsec_net.Write("B1A0", new boolean[] { true }); // 写入B1A0为通
//		melsec_net.Write("D1000", (short) 1234); // 写入D1000 short值 ,W3C0,R3C0
//													// 效果是一样的
//		melsec_net.Write("D1000", 1234566); // 写入D1000 int值
//		melsec_net.Write("D1000", 123.456f); // 写入D1000 float值
//		melsec_net.Write("D1000", 123.456d); // 写入D1000 double值
//		melsec_net.Write("D1000", 123456661235123534L); // 写入D1000 long值
//		melsec_net.Write("D1000", "K123456789"); // 写入D1000 string值
//		// 如下方法演示读取了M200-M209这10个M的值，注意：读取长度必须为偶数，即时写了奇数，也会补齐至偶数，读取和写入的最大长度为7168，否则报错。如需实际需求确实大于7168的，请分批次读取。
//		// 返回值解析：如果读取正常则共返回10个字节的数据，以下示例数据进行批量化的读取
//		OperateResultExOne<boolean[]> read = melsec_net.ReadBool("M100", (short) 10);
//		if (read.IsSuccess) {
//			boolean m100 = read.Content[0];
//			boolean m101 = read.Content[1];
//			boolean m102 = read.Content[2];
//			boolean m103 = read.Content[3];
//			boolean m104 = read.Content[4];
//			boolean m105 = read.Content[5];
//			boolean m106 = read.Content[6];
//			boolean m107 = read.Content[7];
//			boolean m108 = read.Content[8];
//			boolean m109 = read.Content[9];
//		} else {
//			System.out.print("读取失败：" + read.Message);
//		}
//		// 写入测试，M100-M104 写入测试 此处写入后M100:通 M101:断 M102:断 M103:通 M104:通
//		boolean[] values = new boolean[] { true, false, false, true, true };
//		OperateResult write = melsec_net.Write("M100", values);
//		if (write.IsSuccess) {
//			System.out.print("写入成功");
//		} else {
//			System.out.print("写入失败：" + write.Message);
//		}
//		//此处读取针对中间存在整数数据的情况，因为两者读取方式相同，故而只演示一种数据读取， 使用该组件读取数据，一次最多读取或写入960个字，超出则失败。 如果读取的长度确实超过限制，请考虑分批读取。
//		OperateResultExOne<byte[]> read1 = melsec_net.Read("D100",(short)5);
//		if(read1.IsSuccess){
//		    short D100 = melsec_net.getByteTransform().TransByte(read1.Content,0);
//		    short D101 = melsec_net.getByteTransform().TransByte(read1.Content,2);
//		    short D102 = melsec_net.getByteTransform().TransByte(read1.Content,4);
//		    short D103 = melsec_net.getByteTransform().TransByte(read1.Content,6);
//		    short D104 = melsec_net.getByteTransform().TransByte(read1.Content,8);
//		}
//		else {
//		    System.out.print("读取失败："+read1.Message);
//		}
//		// D100为1234,D101为8765,D102为1234,D103为4567,D104为-2563
//		short[] values2 = new short[]{1335, 8765, 1234, 4567, -2563 };
//		OperateResult write2 = melsec_net.Write("M100",values2);
//		if(write2.IsSuccess){
//		    System.out.print("写入成功");
//		}
//		else {
//		    System.out.print("写入失败："+write.Message);
//		}
//		//解析复杂数据
//		OperateResultExOne<byte[]> read3 = melsec_net.Read("D4000", (short) 10);
//		if (read3.IsSuccess)
//		{
//		    double 温度 = melsec_net.getByteTransform().TransInt16(read3.Content, 0) / 10d;//索引很重要
//		    double 压力 = melsec_net.getByteTransform().TransInt16(read3.Content, 2) / 100d;
//		    boolean IsRun = melsec_net.getByteTransform().TransInt16(read3.Content, 4) == 1;
//		    int 产量 =melsec_net.getByteTransform().TransInt32(read3.Content, 6);
//		    String 规格 = melsec_net.getByteTransform().TransString(read3.Content, 10, 10,"ascii");
//		}
//		else
//		{
//		    System.out.print("读取失败："+read3.Message);
//		}
//	}
//}

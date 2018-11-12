package com.txts.exh.console.beans;

public class TotalController {
	private static String ip = "10.0.0.1";// IP地址
	private static int port = 502;// 端口 默认502
	private static int slave = 1;// 从站
	private static Long waitMillis = 1000L;// 默认等待时间

	private static int maxWait = 120;// 最大等待2分钟
	private static int currentSl = 0;// 计划数量
	private static int endsl = 0;// 已完成
}

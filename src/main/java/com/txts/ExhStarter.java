package com.txts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.txts.exh.console.controller.ConsoleController;

@EnableScheduling // 配置了这个注解才可以使用定时器
@SpringBootApplication
public class ExhStarter {
	public static void main(String[] args) {
		SpringApplication.run(ExhStarter.class, args);
		// 启动后记录启动视觉
		ConsoleController.startStamp = System.currentTimeMillis();
	}
}

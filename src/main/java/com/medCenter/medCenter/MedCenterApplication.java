package com.medCenter.medCenter;

import com.sun.tools.javac.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MedCenterApplication {
	private static final Logger logger = LogManager.getLogger(MedCenterApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(MedCenterApplication.class, args);

//		logger.info("Это информационное сообщение");
//		logger.warn("Это предупреждение");
//		logger.error("Это ошибка!");


	}

}

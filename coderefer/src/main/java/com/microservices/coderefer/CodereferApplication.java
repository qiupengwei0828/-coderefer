package com.microservices.coderefer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.microservices.coderefer.mapper")
public class CodereferApplication {
	public static void main(String[] args) {
		SpringApplication.run(CodereferApplication.class, args);
		 System.out.println("启动完毕");
	}

}

package com.microservices.coderefer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.microservices.coderefer.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class CodereferApplication {
	public static void main(String[] args) {
		SpringApplication.run(CodereferApplication.class, args);
		 System.out.println("启动完毕");
	}

}

package com.sparos.uniquone.msachatservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsaChatServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsaChatServiceApplication.class, args);
	}

}

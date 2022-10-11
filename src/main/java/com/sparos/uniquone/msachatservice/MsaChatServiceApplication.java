package com.sparos.uniquone.msachatservice;

import com.sparos.uniquone.msachatservice.config.AuditorAwareImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableFeignClients
@EnableMongoRepositories
//@EnableMongoAuditing
@EnableDiscoveryClient
@SpringBootApplication
public class MsaChatServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsaChatServiceApplication.class, args);
	}

//	@Bean
//	public AuditorAware<String> myAuditorProvider(){
//		return new AuditorAwareImpl();
//	}
}

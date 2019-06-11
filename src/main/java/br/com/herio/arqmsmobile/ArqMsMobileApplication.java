package br.com.herio.arqmsmobile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = { "br.com.herio" })
@EnableFeignClients
public class ArqMsMobileApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArqMsMobileApplication.class, args);
	}
}

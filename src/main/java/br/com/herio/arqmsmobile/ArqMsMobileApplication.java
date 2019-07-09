package br.com.herio.arqmsmobile;

import br.com.herio.arqmsmobile.infra.file.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = { "br.com.herio" })
@EnableFeignClients
@EnableConfigurationProperties({ FileStorageProperties.class })
public class ArqMsMobileApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArqMsMobileApplication.class, args);
	}
}

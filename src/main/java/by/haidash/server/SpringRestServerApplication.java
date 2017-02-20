package by.haidash.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan
@SpringBootApplication
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
public class SpringRestServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestServerApplication.class, args);
	}
}

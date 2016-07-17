package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.web.service.client.CountriesWSClient;

@SpringBootApplication
@ComponentScan({ "com" })
public class SpringSoapWsClientApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringSoapWsClientApplication.class, args);
	}

	@Autowired
	private CountriesWSClient countriesWSClient;

	@Override
	public void run(String... strings) throws Exception {
		countriesWSClient.callWS();
	}
}

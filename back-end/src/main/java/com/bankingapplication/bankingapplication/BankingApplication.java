package com.bankingapplication.bankingapplication;

import application.BankingApplicationHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication(scanBasePackages = {
		"application",
		"domain",
		"infrastructure.persistence",
		"utils"
})
public class BankingApplication  {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(BankingApplication.class, args);
		BankingApplicationHandler handler = context.getBean(BankingApplicationHandler.class);
		handler.run();
	}
}

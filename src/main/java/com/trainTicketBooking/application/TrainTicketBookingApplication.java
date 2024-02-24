package com.trainTicketBooking.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan({"com.trainTicketBooking.application.core.*"})
@EnableJpaRepositories("com.trainTicketBooking.application.core.repository")
@SpringBootApplication
public class TrainTicketBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainTicketBookingApplication.class, args);
	}

}

package com.trainTicketBooking.application.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestDTO {
	
	public String firstName;
	public String lastName;
	public String email;
	public Integer totalNumberOfTickets;
	public String fromLocation;
	public String toLocation;
	
}

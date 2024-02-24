package com.trainTicketBooking.application.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDTO {

	public Long ticketNumber;
	public String from;
	public String to;
	public UserDTO user;
	
}


package com.trainTicketBooking.application.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketReceiptDTO {
	
	public Long ticketNumber;
	public String from;
	public String to;
	public double amountPaid;
	public String userFirstName;
	public String userLastName;
	public String section;
	public int totalSeats;
	
}

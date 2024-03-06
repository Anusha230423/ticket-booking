package com.trainTicketBooking.application.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketUpdateRequestDTO {

	public Long ticketNumber;
	public String discountCode;
}

package com.trainTicketBooking.application;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.trainTicketBooking.application.core.controller.TrainTicketBookingApplicationController;
import com.trainTicketBooking.application.core.dto.TicketRequestDTO;
import com.trainTicketBooking.application.core.dto.TicketResponseDTO;
import com.trainTicketBooking.application.core.model.Ticket;
import com.trainTicketBooking.application.core.model.User;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class TrainTicketBooking1ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testPurchaseTicket() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/ticket/purchase").contentType(MediaType.APPLICATION_JSON)
				.content(
						"{\"fromLocation\":\"London\",\"toLocation\":\"France\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john@example.com\",\"totalNumberOfTickets\":3}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("John Doe")).andDo(print());
	}

	@Test
	public void testGetTicket() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/ticket/receipt?ticketNumber=4"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.ticketNumber").value(4))
				.andExpect(MockMvcResultMatchers.jsonPath("$.userFirstName").value("John"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.userLstName").value("Doe"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.amountPaid").value(15.0))
				.andExpect(MockMvcResultMatchers.jsonPath("$.from").value("London"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.to").value("France"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.section").value("Section A")).andDo(print());
	}

	@Test
	public void testRemoveUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/ticket/removeUser/john@example.com"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("User john@example.com removed from the train"))
				.andDo(print());
	}

	@Test
	public void testModifySeat() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/ticket/updateUserSeat").contentType(MediaType.APPLICATION_JSON)
				.content("{\"email\":\"john@example.com\",\"seatNo\":\"20\"}"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("20 is updated to the Section B")).andDo(print());
	}

}

package com.trainTicketBooking.application.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trainTicketBooking.application.core.dto.TicketReceiptDTO;
import com.trainTicketBooking.application.core.dto.TicketRequestDTO;
import com.trainTicketBooking.application.core.dto.TicketResponseDTO;
import com.trainTicketBooking.application.core.dto.TicketUpdateRequestDTO;
import com.trainTicketBooking.application.core.dto.UserDTO;
import com.trainTicketBooking.application.core.dto.UserSeatUpdateDTO;
import com.trainTicketBooking.application.core.model.Seat;
import com.trainTicketBooking.application.core.model.Ticket;
import com.trainTicketBooking.application.core.model.User;
import com.trainTicketBooking.application.core.repository.SeatRepository;
import com.trainTicketBooking.application.core.repository.TrainTicketRepository;
import com.trainTicketBooking.application.core.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ticket")
@Slf4j
public class TrainTicketBookingApplicationController {

	@Autowired
	public TrainTicketRepository trainTicketRepository;

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public SeatRepository seatRepository;

	/**
	 * @author Anusha L {
	 * @summary API to purchase a ticket and returns receipt in the response }
	 * @param ticketRequest
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/purchase")
	public ResponseEntity<Object> bookTrainTicket(@RequestBody TicketRequestDTO ticketRequest) {
		try {
			if ((StringUtils.isEmpty(ticketRequest.getFirstName()) && StringUtils.isEmpty(ticketRequest.getLastName())
					&& StringUtils.isEmpty(ticketRequest.getEmail()))) {
				return new ResponseEntity<>("Please enter First Name, Last Name and Email Address",
						HttpStatus.BAD_REQUEST);
			}
			double ticketPrice = 5.00 * ticketRequest.getTotalNumberOfTickets();
			User user = userRepository.findByEmail(ticketRequest.getEmail());
			if (user == null) {
				userRepository.save(new User(null, ticketRequest.getFirstName(), ticketRequest.getLastName(),
						ticketRequest.getEmail()));
			}
			Ticket ticket = new Ticket(null, ticketRequest.getFromLocation(), ticketPrice,
					assignSeatSection(trainTicketRepository.findAll()), ticketRequest.getToLocation(), user, "");
			trainTicketRepository.save(ticket);
			for (int i = 0; i < ticketRequest.getTotalNumberOfTickets(); i++) {
				Seat seat = new Seat(null, user, ticket.getSection(), ticket.getId());
				seatRepository.save(seat);
			}
			TicketResponseDTO ticketResponse = new TicketResponseDTO(ticket.getId(), ticket.getFromLocation(),
					ticket.getToLocation(), new UserDTO(user.getFirstName(), user.getLastName(), user.getEmail()));
			return ResponseEntity.ok(ticketResponse);
		} catch (Exception e) {
			log.error("Error in purchase a ticket and returns receipt in the response ", e.getMessage());
			return new ResponseEntity<>("Unable to purchase a ticket and returns receipt in the response",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @author Anusha L {
	 * @summary API to fetch the details of the receipt for the user }
	 * @param ticketNumber
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/receipt")
	public ResponseEntity<Object> getTicketReceipt(@RequestParam Long ticketNumber) {
		try {
			if (StringUtils.isEmpty(ticketNumber)) {
				return new ResponseEntity<>("Enter ticket number", HttpStatus.BAD_REQUEST);
			}
			Ticket ticket = trainTicketRepository.getById(ticketNumber);
			if (ticket == null) {
				return new ResponseEntity<>("Enter valid ticket number", HttpStatus.BAD_REQUEST);
			}
			User user = ticket.getUser();
			List<Seat> seats = seatRepository.findByTicketId(ticketNumber);
			TicketReceiptDTO ticketReceipt = new TicketReceiptDTO(ticketNumber, ticket.getFromLocation(),
					ticket.getToLocation(), ticket.getPrice(), user.getFirstName(), user.getLastName(),
					ticket.getSection(), seats.size());
			return ResponseEntity.ok(ticketReceipt);
		} catch (Exception e) {
			log.error("Error in fetch the details of the receipt for the user ", e.getMessage());
			return new ResponseEntity<>("Unable to fetch the details of the receipt for the user",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @author Anusha L {
	 * @summary API to fetch users and the seat allocated for users by the requested
	 *          section }
	 * @param section
	 * @return
	 */
	@GetMapping("/getUsersAndSeats/{section}")
	public ResponseEntity<Object> getUsersAndSeats(@PathVariable String section) {
		try {
			if (StringUtils.isEmpty(section)) {
				return new ResponseEntity<>("Enter section to fetch the seats and users", HttpStatus.BAD_REQUEST);
			}
			if (!section.equals("Section A") && !section.equals("Section B")) {
				return new ResponseEntity<>("Enter valid section to fetch users and seats", HttpStatus.BAD_REQUEST);
			}
			List<Seat> seats = seatRepository.findBySection(section);
			List<String> response = new ArrayList<>();
			seats.forEach(s -> {
				User user = s.getUser();
				response.add(user.getFirstName() + " " + user.getLastName() + " is allocated to the seat number "
						+ s.getId());
			});
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("Error in fetch users and the seat allocated for users by the requested", e.getMessage());
			return new ResponseEntity<>("Unable to fetch users and the seat allocated for users by the requested",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @author Anusha L {
	 * @summary API to remove user from the train }
	 * @param userEmail
	 * @return
	 */
	@DeleteMapping("/removeUser/{userEmail}")
	public ResponseEntity<Object> removeUser(@PathVariable String userEmail) {
		try {
			User user = userRepository.findByEmail(userEmail);
			if (user == null) {
				return new ResponseEntity<>("Enter valid email address", HttpStatus.BAD_REQUEST);
			}
			List<Ticket> tickets = trainTicketRepository.findByUserId(user.getId());
			trainTicketRepository.deleteAll(tickets);
			List<Seat> seats = seatRepository.findByUserId(user.getId());
			seatRepository.deleteAll(seats);
			return ResponseEntity.ok("User " + userEmail + " removed from the train");
		} catch (Exception e) {
			log.error("Error in remove user from the train ", e.getMessage());
			return new ResponseEntity<>("Unable to remove user from the train", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @author Anusha L {
	 * @summary API to update/modify user's seat }
	 * @param ticketRequest
	 * @return
	 */
	@PostMapping("/updateUserSeat")
	public ResponseEntity<Object> updateUserSeat(@RequestBody UserSeatUpdateDTO userSeatUpdateDTO) {
		try {
			if ((StringUtils.isEmpty(userSeatUpdateDTO.getEmail()) && StringUtils.isEmpty(userSeatUpdateDTO.getSeatNo()))) {
				return new ResponseEntity<>("Please enter Seat Number and Email Address",
						HttpStatus.BAD_REQUEST);
			}
			Seat seat = seatRepository.getById(userSeatUpdateDTO.getSeatNo());
			switch(seat.getSection()) {
			case "Section A" -> seat.setSection("Section B");
			case "Section B" -> seat.setSection("Section A");
			}
			seatRepository.save(seat);
			log.info(userSeatUpdateDTO.getSeatNo() + " is updated to the " + seat.getSection());
			return ResponseEntity.ok(userSeatUpdateDTO.getSeatNo() + " is updated to the " + seat.getSection());
		} catch (Exception e) {
			log.error("Error in update/modify user's seat ", e.getMessage());
			return new ResponseEntity<>("Unable to update/modify user's seat",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public String assignSeatSection(List<Ticket> tickets) {
		return (tickets.size() % 2 == 0) ? "Section A" : "Section B";
	}
	
	@PostMapping("/update")
	public ResponseEntity<Object> updateTrainTicket(@RequestBody TicketUpdateRequestDTO ticketUpdateRequestDTO) {
		try {
			if ((StringUtils.isEmpty(ticketUpdateRequestDTO.getTicketNumber()) && StringUtils.isEmpty(ticketUpdateRequestDTO.getDiscountCode()))) {
				return new ResponseEntity<>("Please enter ticket number and discount code",
						HttpStatus.BAD_REQUEST);
			}
			Map<String, Integer> availableDiscountCodes = new HashMap<>();
			availableDiscountCodes.put("Discount1", 1);
			availableDiscountCodes.put("Discount2", 3);
			availableDiscountCodes.put("Discount3", 10);
			boolean codePresent = availableDiscountCodes.containsKey(ticketUpdateRequestDTO.getDiscountCode());
			if(!codePresent) {
				return new ResponseEntity<>("Enter the correct discount code",
						HttpStatus.BAD_REQUEST);
			}
			Ticket ticket = trainTicketRepository.getById(ticketUpdateRequestDTO.getTicketNumber());
			String discountAdded = ticket.getDiscountAdded() != null ? ticket.getDiscountAdded() : "";
			double price = ticket.getPrice();
			if((discountAdded.isEmpty() || !discountAdded.contains(ticketUpdateRequestDTO.getDiscountCode())) && price > 0.0) {
				price -= availableDiscountCodes.get(ticketUpdateRequestDTO.getDiscountCode());
				ticket.setPrice(price < 0.0 ? 0.0 : price);
				ticket.setDiscountAdded(discountAdded.concat(ticketUpdateRequestDTO.getDiscountCode()));
				trainTicketRepository.save(ticket);
				return ResponseEntity.ok("Ticket is updated!");
			} else if(price <= 0.0) {
				return new ResponseEntity<>("Discount code cannot be applied from the total price is 0.0!",
						HttpStatus.BAD_REQUEST);
			} else {
				return new ResponseEntity<>("Discount code: "+ticketUpdateRequestDTO.getDiscountCode()+" is already applied!",
						HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error("Error in purchase a ticket and returns receipt in the response ", e.getMessage());
			return new ResponseEntity<>("Unable to purchase a ticket and returns receipt in the response",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

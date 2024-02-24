package com.trainTicketBooking.application.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trainTicketBooking.application.core.model.Seat;
import com.trainTicketBooking.application.core.model.Ticket;

@Repository
public interface TrainTicketRepository extends JpaRepository<com.trainTicketBooking.application.core.model.Ticket, Long> {

	@Query (value="select * from tickets where user_id=?1", nativeQuery=true)
	List<Ticket> findByUserId(Long user_id);
	
}

package com.trainTicketBooking.application.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trainTicketBooking.application.core.model.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long>{

	@Query (value="select * from seat where ticket_id=?1", nativeQuery=true)
	List<Seat> findByTicketId(Long ticket_id);
	
	@Query (value="select * from seat where section=?1", nativeQuery=true)
	List<Seat> findBySection(String section);
	
	@Query (value="select * from seat where user_id=?1", nativeQuery=true)
	List<Seat> findByUserId(Long user_id);
}

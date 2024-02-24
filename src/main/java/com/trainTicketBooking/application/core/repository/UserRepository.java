package com.trainTicketBooking.application.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trainTicketBooking.application.core.model.User;

@Repository
public interface UserRepository extends JpaRepository<com.trainTicketBooking.application.core.model.User, Long>{
	
	@Query (value="select * from user where email=?1", nativeQuery=true)
	User findByEmail(String email);

}

package com.psp.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.psp.app.model.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {
	
	@Query( value = "select * from users where email = :email", nativeQuery = true)
	Customer findbyEmail(@Param("email") String email);

}

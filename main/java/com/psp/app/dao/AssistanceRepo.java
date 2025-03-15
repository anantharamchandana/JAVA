package com.psp.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.psp.app.model.Assistance;
import com.psp.app.model.Contact;
import com.psp.app.model.Pet;
import com.psp.app.model.Schedule;


@Repository
public interface AssistanceRepo extends JpaRepository<Assistance, Long> {


}

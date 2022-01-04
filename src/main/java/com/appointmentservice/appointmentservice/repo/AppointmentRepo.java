package com.appointmentservice.appointmentservice.repo;

import com.appointmentservice.appointmentservice.repo.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
    //List<Appointment> findAllByUser()
}

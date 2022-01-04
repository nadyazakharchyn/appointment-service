package com.appointmentservice.appointmentservice.api;

import com.appointmentservice.appointmentservice.api.dto.AppointmentDto;
import com.appointmentservice.appointmentservice.repo.model.Appointment;
import com.appointmentservice.appointmentservice.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.mongo.ReactiveStreamsMongoClientDependsOnBeanFactoryPostProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<Appointment>> showAll() {
        final List<Appointment> appointments = appointmentService.fetchAll();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> showById(@PathVariable long id) {
        try {
            final Appointment appointment = appointmentService.findById(id);

            return ResponseEntity.ok(appointment);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody AppointmentDto appointmentDto){
        long userId = appointmentDto.getUserId();
        long doctorId = appointmentDto.getDoctorId();
        String date = appointmentDto.getDate();

        try {
            final long appointmentId = appointmentService.create(userId, doctorId, date).getId();
            final String appointmentUri = String.format("/appointments/%d", appointmentId);
            return ResponseEntity.created(URI.create(appointmentUri)).build();
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> change(@PathVariable long id, @RequestBody AppointmentDto appointmentDto){
        long userId = appointmentDto.getUserId();
        long doctorId = appointmentDto.getDoctorId();
        String date = appointmentDto.getDate();

        try {
            appointmentService.update(id, userId, doctorId, date);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        appointmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

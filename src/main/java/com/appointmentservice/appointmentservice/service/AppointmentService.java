package com.appointmentservice.appointmentservice.service;

import com.appointmentservice.appointmentservice.api.dto.DoctorDto;
import com.appointmentservice.appointmentservice.api.dto.UserDto;
import com.appointmentservice.appointmentservice.repo.AppointmentRepo;
import com.appointmentservice.appointmentservice.repo.model.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AppointmentService {
    private final String userUrlAddress = "http://user-service:8080/users";
    private final String doctorUrlAddress = "http://user-service:8080/doctors";
    private final AppointmentRepo appointmentRepo;

    public List<Appointment> fetchAll(){
        return appointmentRepo.findAll();
    }

    public boolean checkUser(Long userId){
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> userRequest = new HttpEntity<>(userId);

        final ResponseEntity<UserDto> userResponse = restTemplate
                .exchange(userUrlAddress + "/dto/" + userId,
                        HttpMethod.GET, userRequest, UserDto.class);

        return userResponse.getStatusCode() != HttpStatus.NOT_FOUND;
    }

    public boolean checkDoctor(Long doctorId){
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> userRequest = new HttpEntity<>(doctorId);

        final ResponseEntity<DoctorDto> userResponse = restTemplate
                .exchange(doctorUrlAddress + "/dto/" + doctorId,
                        HttpMethod.GET, userRequest, DoctorDto.class);

        return userResponse.getStatusCode() != HttpStatus.NOT_FOUND;
    }

    public Appointment create(Long userId, Long doctorId, String date){
        if (!checkDoctor(doctorId)) throw new IllegalArgumentException("invalid doctor id");
        else if (!checkUser(userId)) throw new IllegalArgumentException("invalid user id");
        else {
            Appointment appointment = new Appointment(date, userId, doctorId);
            return appointmentRepo.save(appointment);
        }
    }

    public void deleteById(Long id) throws IllegalArgumentException{
        final Optional<Appointment> maybeAppointment = appointmentRepo.findById(id);
        if(maybeAppointment.isEmpty()) throw new RuntimeException("Appointment not found");
        appointmentRepo.delete(maybeAppointment.get());

    }

    public Appointment findById(Long id)throws IllegalArgumentException{
        final Optional<Appointment> maybeAppointment = appointmentRepo.findById(id);
        if(maybeAppointment.isEmpty()) throw new RuntimeException("Appointment not found");
        else return maybeAppointment.get();
    }

    public void update(Long id, Long userId, Long doctorId, String date){
        final Optional<Appointment> maybeAppointment = appointmentRepo.findById(id);
        if(maybeAppointment.isEmpty()) throw new RuntimeException("Appointment not found");
        final Appointment appointment = maybeAppointment.get();
        if (doctorId != null && userId!=null){
            if (!checkDoctor(doctorId)) {
                throw new IllegalArgumentException("Invalid doctor id");
            }
            if(!(checkUser(userId))){
                throw new IllegalArgumentException("Invalid user id");
            }
            appointment.setDoctorId(doctorId);
            appointment.setUserId(userId);
            appointment.setDate(date);
            appointmentRepo.save(appointment);
        }
    }

}

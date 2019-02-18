package com.example.slabiak.appointmentscheduler;

import com.example.slabiak.appointmentscheduler.dao.AppointmentRepository;
import com.example.slabiak.appointmentscheduler.model.Appointment;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class AppointmentSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentSchedulerApplication.class, args);
	}

    @Service
    public static class AppointmentServiceImpl implements AppointmentService {
        @Autowired
        AppointmentRepository appointmentRepository;


        @Override
        public void save(Appointment appointment) {
            appointmentRepository.save(appointment);
        }

        @Override
        public Appointment findById(int id) {
            Optional<Appointment> result = appointmentRepository.findById(id);

            Appointment appointment = null;

            if (result.isPresent()) {
                appointment = result.get();
            }
            else {
                // todo throw new excep
            }

            return appointment;
        }


        @Override
        public List<Appointment> findAll() {
            return appointmentRepository.findAll();
        }

        @Override
        public void deleteById(int id) {
            appointmentRepository.deleteById(id);
        }
    }
}

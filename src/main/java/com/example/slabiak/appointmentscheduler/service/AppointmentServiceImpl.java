package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.dao.AppointmentRepository;
import com.example.slabiak.appointmentscheduler.dao.UserRepository;
import com.example.slabiak.appointmentscheduler.dao.WorkingPlanRepository;
import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;
import com.example.slabiak.appointmentscheduler.model.AppointmentRegisterForm;
import com.example.slabiak.appointmentscheduler.model.DayPlan;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService{

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkService workService;

    @Autowired
    private WorkingPlanRepository workingPlanRepository;


    @Override
    public void save(AppointmentRegisterForm appointmentRegisterForm) {
        Appointment appointment = new Appointment();

        appointment.setCustomer(userService.findById(appointmentRegisterForm.getCustomerId()));
        appointment.setProvider(userService.findById(appointmentRegisterForm.getProviderId()));
        Work work = workService.findById(appointmentRegisterForm.getWorkId());
        appointment.setWork(work);
        appointment.setStart(appointmentRegisterForm.getStart());
        appointment.setEnd(appointmentRegisterForm.getStart().plusMinutes(work.getDuration()));
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

    @Override
    public List<Appointment> findByCustomer(User customer) {
        return appointmentRepository.findByCustomer(customer);
    }

    @Override
    public List<Appointment> findByProvider(User provider) {
        return appointmentRepository.findByCustomer(provider);
    }

    @Override
    public List<Appointment> findByProviderAndDate(User user, LocalDate date) {
        return appointmentRepository.findByProviderAndDate(user,date.atStartOfDay(), date.atStartOfDay().plusHours(24));
    }

    @Override
    public List<Appointment> getAvailableAppointments(int providerId, int workId, LocalDate date) {
        return null;
    }

    @Override
    public List<TimePeroid> getProviderAvailableTimePeroids(int providerId, int workId, LocalDate date){
        WorkingPlan workingPlan = workingPlanRepository.getOne(1);
        DayPlan selectedDay = workingPlan.getDay(date.getDayOfWeek().toString().toLowerCase());
        List<Appointment> providerAppointments = findByProviderAndDate(userService.findById(providerId),date);


        List<TimePeroid> availablePeroids = new ArrayList<TimePeroid>();
        // get peroids from working hours for selected day excluding breaks
        availablePeroids = selectedDay.getPeroidsWithBreaksExcluded();
        // exclude booked appointments for selected provider
        availablePeroids = excludeAppointments(availablePeroids,providerAppointments);
        return availablePeroids;
    }

    public List<TimePeroid> excludeAppointments(List<TimePeroid> peroids, List<Appointment> appointments){
        if(appointments.size()>0){
            List<TimePeroid> toAdd = new ArrayList<TimePeroid>();
            for(Appointment appointment: appointments){
                for(TimePeroid plan:peroids){
                    if(appointment.getStart().toLocalTime().isBefore(plan.getStart()) && appointment.getEnd().toLocalTime().isAfter(plan.getStart()) && appointment.getEnd().toLocalTime().isBefore(plan.getEnd())){
                        plan.setStart(appointment.getEnd().toLocalTime());
                    }
                    if(appointment.getStart().toLocalTime().isAfter(plan.getStart()) && appointment.getStart().toLocalTime().isBefore(plan.getEnd()) && appointment.getEnd().toLocalTime().isAfter(plan.getEnd())){
                        plan.setEnd(appointment.getStart().toLocalTime());
                    }
                    if(appointment.getStart().toLocalTime().isAfter(plan.getStart()) && appointment.getEnd().toLocalTime().isBefore(plan.getEnd())){
                        toAdd.add(new TimePeroid(plan.getStart(),appointment.getStart().toLocalTime()));
                        plan.setStart(appointment.getEnd().toLocalTime());
                    }
                }
            }
            peroids.addAll(toAdd);
            Collections.sort(peroids);
        }
        return peroids;
    }



}

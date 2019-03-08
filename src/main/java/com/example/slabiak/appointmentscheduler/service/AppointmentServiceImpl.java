package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.dao.AppointmentRepository;
import com.example.slabiak.appointmentscheduler.dao.ChatMessageRepository;
import com.example.slabiak.appointmentscheduler.dao.WorkingPlanRepository;
import com.example.slabiak.appointmentscheduler.entity.*;
import com.example.slabiak.appointmentscheduler.model.AppointmentRegisterForm;
import com.example.slabiak.appointmentscheduler.model.DayPlan;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
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

    @Autowired
    private ChatMessageRepository chatMessageRepository;


    @Override
    public void save(AppointmentRegisterForm appointmentRegisterForm) {
        Appointment appointment = new Appointment();
        appointment.setStatus("scheduled");
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

        availablePeroids = selectedDay.peroidsWithBreaksExcluded();
        // exclude booked appointments for selected provider
        availablePeroids = excludeAppointments(availablePeroids,providerAppointments);
       return calculateAvailableHours(availablePeroids,workService.findById(workId));
       // return availablePeroids;
    }

    @Override
    public void save(int workId, int providerId, int customerId, LocalDateTime start) {
        Appointment appointment = new Appointment();
        appointment.setStatus("scheduled");
        appointment.setCustomer(userService.findById(customerId));
        appointment.setProvider(userService.findById(providerId));
        Work work = workService.findById(workId);
        appointment.setWork(work);
        appointment.setStart(start);
        appointment.setEnd(start.plusMinutes(work.getDuration()));
        appointmentRepository.save(appointment);
    }

    @Override
    public void cancelById(int id) {
        Appointment appointment = appointmentRepository.getOne(id);
        appointment.setStatus("canceled");
        appointment.setCanceledAt(LocalDateTime.now());
        appointmentRepository.save(appointment);
    }

    @Override
    public void addChatMessageToAppointment(int appointmentId, int authorId, ChatMessage chatMessage) {
        chatMessage.setAuthor(userService.findById(authorId));
        chatMessage.setAppointment(findById(appointmentId));
        chatMessage.setCreatedAt(LocalDateTime.now());
        chatMessageRepository.save(chatMessage);
    }

    @Override
    public boolean isUserAllowedToCancelAppointment(int userId, int appointmentId) {
        User user = userService.findById(userId);
        Appointment appointment = findById(appointmentId);
         if(appointment.getProvider().equals(user)){
            return true;
        }  else if(LocalDateTime.now().plusHours(24).isAfter(appointment.getStart())){
            return false;
        }
        else if(appointment.getCustomer().equals(user) && appointment.getWork().getEditable() && getAppointmentsCanceledByUserInThisMonth(userId).size()<=1){
            return true;
        }
        return false;
    }

    public List<TimePeroid> calculateAvailableHours(List<TimePeroid> tp, Work work){

        ArrayList<TimePeroid> availableHours = new ArrayList<TimePeroid>();


        for(TimePeroid peroid: tp){
            TimePeroid workPeroid = new TimePeroid(peroid.getStart(),peroid.getStart().plusMinutes(work.getDuration()));
            while(workPeroid.getEnd().isBefore(peroid.getEnd()) || workPeroid.getEnd().equals(peroid.getEnd())){
                availableHours.add(new TimePeroid(workPeroid.getStart(),workPeroid.getStart().plusMinutes(work.getDuration())));
                workPeroid.setStart(workPeroid.getStart().plusMinutes(work.getDuration()));
                workPeroid.setEnd(workPeroid.getEnd().plusMinutes(work.getDuration()));

            }
        }

        return availableHours;
    }

    public List<TimePeroid> excludeAppointments(List<TimePeroid> peroids, List<Appointment> appointments){

            List<TimePeroid> toAdd = new ArrayList<TimePeroid>();
            Collections.sort(appointments);
            for(Appointment appointment: appointments){
                for(TimePeroid peroid:peroids){
                    if((appointment.getStart().toLocalTime().isBefore(peroid.getStart()) || appointment.getStart().toLocalTime().equals(peroid.getStart())) && appointment.getEnd().toLocalTime().isAfter(peroid.getStart()) && appointment.getEnd().toLocalTime().isBefore(peroid.getEnd())){
                        peroid.setStart(appointment.getEnd().toLocalTime());
                    }
                    if(appointment.getStart().toLocalTime().isAfter(peroid.getStart())&& appointment.getStart().toLocalTime().isBefore(peroid.getEnd()) && appointment.getEnd().toLocalTime().isAfter(peroid.getEnd()) || appointment.getEnd().toLocalTime().equals(peroid.getEnd())){
                        peroid.setEnd(appointment.getStart().toLocalTime());
                    }
                    if(appointment.getStart().toLocalTime().isAfter(peroid.getStart()) && appointment.getEnd().toLocalTime().isBefore(peroid.getEnd())){
                        toAdd.add(new TimePeroid(peroid.getStart(),appointment.getStart().toLocalTime()));
                        peroid.setStart(appointment.getEnd().toLocalTime());
                    }
                }
            }
            peroids.addAll(toAdd);
            Collections.sort(peroids);
        return peroids;
    }

    public List<Appointment> getAppointmentsCanceledByUserInThisMonth(int userId){
        User user = userService.findById(userId);
        return appointmentRepository.getAppointmentsCanceledByUserInThisMonth(user, LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay());
    }



}

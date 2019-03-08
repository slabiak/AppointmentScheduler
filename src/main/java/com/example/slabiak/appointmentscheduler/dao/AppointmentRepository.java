package com.example.slabiak.appointmentscheduler.dao;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository  extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByCustomer(User user);
    List<Appointment> findByProvider(User user);

    @Query("select a from Appointment a where a.provider = :user and  a.start >=:dayStart and  a.start <=:dayEnd")
    List<Appointment> findByProviderAndDate(@Param("user") User user, @Param("dayStart") LocalDateTime dayStart, @Param("dayEnd") LocalDateTime dayEnd);

    @Query("select a from Appointment a where a.customer = :user and  a.canceledAt >=:beginingOfCurrentMonth")
    List<Appointment> getAppointmentsCanceledByUserInThisMonth(@Param("user") User user,@Param("beginingOfCurrentMonth") LocalDateTime beginingOfCurrentMonth);
}

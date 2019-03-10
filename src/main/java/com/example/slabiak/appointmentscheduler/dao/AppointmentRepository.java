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

    @Query("select a from Appointment a where a.status = 'scheduled' and :now >= a.end")
    List<Appointment> findAllAppointmentsThatNeedsStatusChangeFromScheduledToFinished(@Param("now") LocalDateTime now);

    @Query("select a from Appointment a where a.status = 'scheduled' and :now >= a.end")
    List<Appointment> findAllScheduledAppointmentsWithEndBeforeDate(@Param("now") LocalDateTime now);

    @Query("select a from Appointment a where a.status = 'scheduled' and :now >= a.end and (a.customer.id = :userId or a.provider.id = :userId)")
    List<Appointment> findUserScheduledAppointmentsWithEndBeforeDate(@Param("now")LocalDateTime now, @Param("userId") int userId);

    @Query("select a from Appointment a where a.status = 'finished' and :dayAgo >= a.end")
    List<Appointment> findAllFinishedAppointmentsWithEndBeforeDate(@Param("dayAgo")LocalDateTime dayAgo);

    @Query("select a from Appointment a where a.status = 'finished' and :dayAgo >= a.end and (a.customer.id = :userId or a.provider.id = :userId)")
    List<Appointment> findUserFinishedAppointmentsWithEndBeforeDate(@Param("dayAgo")LocalDateTime dayAgo,@Param("userId") int userId);
}

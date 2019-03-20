package com.example.slabiak.appointmentscheduler.dao;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository  extends JpaRepository<Appointment, Integer> {

    @Query("select a from Appointment a where a.customer.id = :customerId")
    List<Appointment> findByCustomerId(@Param("customerId") int customerId);

    @Query("select a from Appointment a where a.provider.id = :providerId")
    List<Appointment> findByProviderId(@Param("providerId") int providerId);

    @Query("select a from Appointment a where a.canceler.id = :userId")
    List<Appointment> findAppointmentsCanceledByUser(@Param("userId") int userId);

    @Query("select a from Appointment a where  a.status='scheduled' and (a.customer.id = :userId or a.provider.id = :userId)")
    List<Appointment> findScheduledAppointmentsForUser(@Param("userId") int userId);

    @Query("select a from Appointment a where a.provider = :user and  a.start >=:dayStart and  a.start <=:dayEnd")
    List<Appointment> findByProviderAndDate(@Param("user") User user, @Param("dayStart") LocalDateTime dayStart, @Param("dayEnd") LocalDateTime dayEnd);

    @Query("select a from Appointment a where a.customer = :user and a.canceler=:user and a.canceledAt >=:beginingOfCurrentMonth")
    List<Appointment> getAppointmentsCanceledByUserInThisMonth(@Param("user") User user,@Param("beginingOfCurrentMonth") LocalDateTime beginingOfCurrentMonth);

    @Query("select a from Appointment a where a.status = 'scheduled' and :now >= a.end")
    List<Appointment> findAllScheduledAppointmentsWithEndBeforeDate(@Param("now") LocalDateTime now);

    @Query("select a from Appointment a where a.status = 'scheduled' and :now >= a.end and (a.customer.id = :userId or a.provider.id = :userId)")
    List<Appointment> findUserScheduledAppointmentsWithEndBeforeDate(@Param("now")LocalDateTime now, @Param("userId") int userId);

    @Query("select a from Appointment a where a.status = 'finished' and :dayAgo >= a.end")
    List<Appointment> findAllFinishedAppointmentsWithEndBeforeDate(@Param("dayAgo")LocalDateTime dayAgo);

    @Query("select a from Appointment a where a.status = 'finished' and :dayAgo >= a.end and (a.customer.id = :userId or a.provider.id = :userId)")
    List<Appointment> findUserFinishedAppointmentsWithEndBeforeDate(@Param("dayAgo")LocalDateTime dayAgo,@Param("userId") int userId);

    @Query("select a from Appointment a where a.status = 'confirmed' and a.customer.id = :userId")
    List<Appointment> findConfirmedAppointmentsForUser(@Param("userId")int userId);
}

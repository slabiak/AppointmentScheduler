package com.example.slabiak.appointmentscheduler.dao;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Query("select a from Appointment a where a.customer.id = :customerId")
    List<Appointment> findByCustomerId(@Param("customerId") int customerId);

    @Query("select a from Appointment a where a.provider.id = :providerId")
    List<Appointment> findByProviderId(@Param("providerId") int providerId);

    @Query("select a from Appointment a where a.canceler.id = :userId")
    List<Appointment> findCanceledByUser(@Param("userId") int userId);

    @Query("select a from Appointment a where  a.status='SCHEDULED' and (a.customer.id = :userId or a.provider.id = :userId)")
    List<Appointment> findScheduledByUserId(@Param("userId") int userId);

    @Query("select a from Appointment a where a.provider.id = :providerId and  a.start >=:dayStart and  a.start <=:dayEnd")
    List<Appointment> findByProviderIdWithStartInPeroid(@Param("providerId") int providerId, @Param("dayStart") LocalDateTime startPeroid, @Param("dayEnd") LocalDateTime endPeroid);

    @Query("select a from Appointment a where a.customer.id = :customerId and  a.start >=:dayStart and  a.start <=:dayEnd")
    List<Appointment> findByCustomerIdWithStartInPeroid(@Param("customerId") int customerId, @Param("dayStart") LocalDateTime startPeroid, @Param("dayEnd") LocalDateTime endPeroid);

    @Query("select a from Appointment a where a.customer.id = :customerId and a.canceler.id =:customerId and a.canceledAt >=:date")
    List<Appointment> findByCustomerIdCanceledAfterDate(@Param("customerId") int customerId, @Param("date") LocalDateTime date);

    @Query("select a from Appointment a where a.status = 'SCHEDULED' and :now >= a.end")
    List<Appointment> findScheduledWithEndBeforeDate(@Param("now") LocalDateTime now);

    @Query("select a from Appointment a where a.status = 'SCHEDULED' and :date >= a.end and (a.customer.id = :userId or a.provider.id = :userId)")
    List<Appointment> findScheduledByUserIdWithEndBeforeDate(@Param("date") LocalDateTime date, @Param("userId") int userId);

    @Query("select a from Appointment a where a.status = 'FINISHED' and :date >= a.end")
    List<Appointment> findFinishedWithEndBeforeDate(@Param("date") LocalDateTime date);

    @Query("select a from Appointment a where a.status = 'FINISHED' and :date >= a.end and (a.customer.id = :userId or a.provider.id = :userId)")
    List<Appointment> findFinishedByUserIdWithEndBeforeDate(@Param("date") LocalDateTime date, @Param("userId") int userId);

    @Query("select a from Appointment a where a.status = 'CONFIRMED' and a.customer.id = :customerId")
    List<Appointment> findConfirmedByCustomerId(@Param("customerId") int customerId);

    @Query("select a from Appointment a inner join a.work w where a.status = 'SCHEDULED' and a.customer.id <> :customerId and a.provider.id= :providerId and a.start >= :start and w.id = :workId")
    List<Appointment> getEligibleAppointmentsForExchange(@Param("start") LocalDateTime start, @Param("customerId") Integer customerId, @Param("providerId") Integer providerId, @Param("workId") Integer workId);

    @Query("select a from Appointment a where a.status = 'EXCHANGE_REQUESTED' and a.start <= :start")
    List<Appointment> findExchangeRequestedWithStartBefore(@Param("start") LocalDateTime date);

}

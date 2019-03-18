package com.example.slabiak.appointmentscheduler.dao;

import com.example.slabiak.appointmentscheduler.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkRepository extends JpaRepository<Work, Integer> {
    @Query("select w from Work w inner join w.providers p where p.id in :providerId")
    List<Work> findByProviderId(@Param("providerId") int providerId);
}

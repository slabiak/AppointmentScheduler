package com.example.slabiak.appointmentscheduler.dao;

import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkingPlanRepository extends JpaRepository<WorkingPlan, Integer> {
    @Query("select w from WorkingPlan w where w.provider.id = :providerId")
    WorkingPlan getWorkingPlanByProviderId(@Param("providerId") int providerId);
}

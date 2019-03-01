package com.example.slabiak.appointmentscheduler.dao;

import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkingPlanRepository extends JpaRepository<WorkingPlan, Integer> {
}

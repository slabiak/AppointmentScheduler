package com.example.slabiak.appointmentscheduler.dao;

import com.example.slabiak.appointmentscheduler.model.Work;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository extends JpaRepository<Work, Integer> {
}

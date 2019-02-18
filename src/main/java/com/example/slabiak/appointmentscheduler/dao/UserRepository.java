package com.example.slabiak.appointmentscheduler.dao;

import com.example.slabiak.appointmentscheduler.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}

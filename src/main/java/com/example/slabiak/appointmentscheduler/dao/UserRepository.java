package com.example.slabiak.appointmentscheduler.dao;

import com.example.slabiak.appointmentscheduler.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
}

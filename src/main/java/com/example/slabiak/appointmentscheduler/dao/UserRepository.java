package com.example.slabiak.appointmentscheduler.dao;

import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
    List<User> findByWorks(Work work);

    @Query( "select u from User u inner join u.roles r where r.name in :roleName" )
    List<User> findByRoleName(@Param("roleName") String roleName);
}

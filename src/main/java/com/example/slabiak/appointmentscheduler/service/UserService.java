package com.example.slabiak.appointmentscheduler.service;


import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.model.UserRegisterForm;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    void registerProvider(UserRegisterForm userForm);
    void updateProvider(User user);
    void registerCustomer(UserRegisterForm userForm);
    User findById(int id);
    User findByUserName( String userName);
    List<User> findByRoleName(@Param("roleName") String roleName);
    List<User> findAll();
    void deleteById(int id);
    List<User> findByWorks(Work work);
}

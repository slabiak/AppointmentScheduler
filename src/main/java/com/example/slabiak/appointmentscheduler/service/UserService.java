package com.example.slabiak.appointmentscheduler.service;


import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.model.UserRegisterForm;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    void register(UserRegisterForm userForm);
    User findById(int id);
    User findByUserName( String userName);
    List<User> findAll();
    void deleteById(int id);
    List<User> findByWorks(Work work);



}

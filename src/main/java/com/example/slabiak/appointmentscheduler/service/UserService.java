package com.example.slabiak.appointmentscheduler.service;


import com.example.slabiak.appointmentscheduler.model.User;

import java.util.List;

public interface UserService {

    void save(User customer);
    User findById(int id);
    List<User> findAll();
    void deleteById(int id);

}

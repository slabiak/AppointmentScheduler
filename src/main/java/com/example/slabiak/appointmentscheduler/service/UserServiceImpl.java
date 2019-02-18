package com.example.slabiak.appointmentscheduler.service;


import com.example.slabiak.appointmentscheduler.model.User;
import com.example.slabiak.appointmentscheduler.dao.UserRepository;
import com.example.slabiak.appointmentscheduler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;


    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(int id) {
        Optional<User> result = userRepository.findById(id);

        User user = null;

        if (result.isPresent()) {
            user = result.get();
        }
        else {
            // todo throw new excep
        }

        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }
}


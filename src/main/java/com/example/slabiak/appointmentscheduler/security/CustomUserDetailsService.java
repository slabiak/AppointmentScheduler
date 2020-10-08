package com.example.slabiak.appointmentscheduler.security;

import com.example.slabiak.appointmentscheduler.dao.user.UserRepository;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName).get();
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return CustomUserDetails.create(user);
    }
}

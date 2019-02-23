package com.example.slabiak.appointmentscheduler.user;

import com.example.slabiak.appointmentscheduler.dao.RoleRepository;
import com.example.slabiak.appointmentscheduler.dao.UserRepository;
import com.example.slabiak.appointmentscheduler.entity.Role;
import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.model.UserRegisterForm;
import com.example.slabiak.appointmentscheduler.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private UserRegisterForm userRegisterForm;
    private User user;
    private Optional<User> userOptional;
    private List<User> users;
    private Role role;
    private HashSet<Role> roles;
    private String password;
    private String roleName;

    @Before
    public void initObjects(){
        password = "pass";
        roleName = "ROLE_CUSTOMER";

        userRegisterForm = new UserRegisterForm("username", password, password,"First", "Last","email" );
        role = new Role(roleName);
        user = new User("username", password,"First","Last","email");
        userOptional = Optional.of(user);
        roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        users = new ArrayList<>();
        users.add(user);

    }

    @Test
    public void shouldSaveUser(){
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(roleRepository.findByName(roleName)).thenReturn(role);
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        userService.register(userRegisterForm);
        verify(userRepository).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue(),user);
    }

    @Test
    public void shouldFindById() {
        when(userRepository.findById(1)).thenReturn(userOptional);
        assertEquals(userOptional.get(), userService.findById(1));
        verify(userRepository).findById(1);
    }

    @Test
    public void shouldFindAllUsers(){
        when(userRepository.findAll()).thenReturn(users);
        assertEquals(users,userService.findAll());
        verify(userRepository).findAll();
    }

    @Test
    public void shouldDeleteById() {
        userService.deleteById(1);
        verify(userRepository).deleteById(1);
    }
}

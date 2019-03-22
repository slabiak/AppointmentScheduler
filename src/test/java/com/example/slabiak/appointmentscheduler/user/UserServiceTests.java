package com.example.slabiak.appointmentscheduler.user;

import com.example.slabiak.appointmentscheduler.dao.RoleRepository;
import com.example.slabiak.appointmentscheduler.dao.user.CommonUserRepository;
import com.example.slabiak.appointmentscheduler.entity.user.Role;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    @Mock
    private CommonUserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;


    private User user;
    private String userName;
    private Optional<User> userOptional;
    private List<User> users;
    private Role role;
    private HashSet<Role> roles;
    private String password;
    private String roleName;
    private Work work;
    private List<Work> works;

    @Before
    public void initObjects(){
        password = "pass";
        roleName = "ROLE_CUSTOMER";
        userName = "username";

        //userRegisterForm = new UserRegisterForm(userName, password, password,"First", "Last","email" );
        role = new Role(roleName);
       // user = new User(userName, password,"First","Last","email");
        userOptional = Optional.of(user);
        roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        users = new ArrayList<>();
        users.add(user);
        work = new Work();
        works = new ArrayList<Work>();
        works.add(work);
       // user.setWorks(works);



    }

  /*  @Test
    public void shouldSaveUser(){
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(roleRepository.findByName(roleName)).thenReturn(role);
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        userService.register(userRegisterForm);
        verify(userRepository).createNewAppointment(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue(),user);
    }*/

/*
    @Test
    public void shouldFindById() {
        when(userRepository.findById(1)).thenReturn(userOptional);
        assertEquals(userOptional.get(), userService.findUserById(1));
        verify(userRepository).findById(1);
    }
*/

    @Test
    public void shouldFindByName(){
        when(userRepository.findByUserName(userName)).thenReturn(userOptional);
        assertEquals(userOptional.get(),userService.getUserByUsername(userName));
        verify(userRepository).findByUserName(userName);
    }

    @Test
    public void shouldFindAllUsers(){
        when(userRepository.findAll()).thenReturn(users);
        assertEquals(users,userService.getAllUsers());
        verify(userRepository).findAll();
    }

/*    @Test
    public void shouldFindByWork(){
        when(userRepository.getProviderByWorks(work)).thenReturn(users);
        assertEquals(user,userService.getProviderByWorks(work).get(0));
        verify(userRepository).getProviderByWorks(work);
    }*/

/*    @Test
    public void shouldReturnUserDetails(){
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUserName(), user.getNewPassword(),authorities);
        when(userRepository.getUserByUsername(userName)).thenReturn(userOptional);
        assertEquals(userDetails.getUsername(), userService.loadUserByUsername(userName).getUsername());
        verify(userRepository).getUserByUsername(userName);
    }*/

    @Test
    public void shouldDeleteById() {
        userService.deleteUserById(1);
        verify(userRepository).deleteById(1);
    }
}



package com.example.slabiak.appointmentscheduler.service.user;

import com.example.slabiak.appointmentscheduler.dao.user.UserRepository;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.model.ChangePasswordForm;
import com.example.slabiak.appointmentscheduler.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private int userId;
    private String password;
    private String passwordEncoded;
    private String userName;
    private String newPassword;
    private User user;
    private Optional<User> optionalUser;

    @Before
    public void initObjects() {
        userId = 1;
        passwordEncoded = "encodedpass";
        userName = "username";
        password = "password";
        newPassword = "newpassword";
        user = new User();
        user.setId(userId);
        user.setUserName(userName);
        optionalUser = Optional.of(user);
    }


    @Test
    public void shouldFindUserById() {
        when(userRepository.findById(userId)).thenReturn(optionalUser);
        assertEquals(optionalUser.get(), userService.getUserById(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void shouldUpdateUserPassword() {
        doReturn(new User()).when(userRepository).getOne(userId);
        ChangePasswordForm changePasswordForm = new ChangePasswordForm(userId);
        userService.updateUserPassword(changePasswordForm);
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).getOne(userId);
        verify(userRepository, times(1)).save(argumentCaptor.capture());
    }

    @Test
    public void shouldEncodeUserPasswordWhileUpdate() {
        User userToBeUpdated = new User();
        userToBeUpdated.setPassword(password);
        doReturn(userToBeUpdated).when(userRepository).getOne(userId);
        doReturn(passwordEncoded).when(passwordEncoder).encode(newPassword);
        ChangePasswordForm changePasswordForm = new ChangePasswordForm(userId);
        changePasswordForm.setCurrentPassword(password);
        changePasswordForm.setPassword(newPassword);

        userService.updateUserPassword(changePasswordForm);
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userRepository, times(1)).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getPassword(), passwordEncoded);
    }

    @Test
    public void shouldFindUserBeUsername() {
        User user = new User();
        user.setId(userId);
        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findByUserName(userName)).thenReturn(optionalUser);
        assertEquals(optionalUser.get(), userService.getUserByUsername(userName));
        verify(userRepository, times(1)).findByUserName(userName);
    }


    @Test
    public void shouldFindAllUsers() {
        List<User> users = new ArrayList<>();
        User user = new User();
        users.add(user);
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);
        List<User> fetchedUsers = userService.getAllUsers();
        assertEquals(fetchedUsers, users);
        assertEquals(fetchedUsers.size(), 2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void shouldDeleteUserById() {
        userService.deleteUserById(userId);
        verify(userRepository).deleteById(userId);
    }


}

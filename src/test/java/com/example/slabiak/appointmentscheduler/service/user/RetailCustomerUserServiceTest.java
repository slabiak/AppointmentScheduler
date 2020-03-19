package com.example.slabiak.appointmentscheduler.service.user;

import com.example.slabiak.appointmentscheduler.dao.RoleRepository;
import com.example.slabiak.appointmentscheduler.dao.user.customer.RetailCustomerRepository;
import com.example.slabiak.appointmentscheduler.entity.user.Role;
import com.example.slabiak.appointmentscheduler.entity.user.customer.RetailCustomer;
import com.example.slabiak.appointmentscheduler.model.UserForm;
import com.example.slabiak.appointmentscheduler.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RetailCustomerUserServiceTest {

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RetailCustomerRepository retailCustomerRepository;

    @InjectMocks
    private UserServiceImpl userService;

    UserForm retailUserForm;
    RetailCustomer retailCustomer;
    Optional<RetailCustomer> optionalRetailCustomer;

    private int userId;
    private String userName;
    private String password;
    private String matchingPassword;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String street;
    private String postcode;
    private String city;

    String passwordEncoded;

    private String roleNameCustomer;
    private String roleNameRetailCustomer;
    private Role roleCustomer;
    private Role roleRetailCustomer;
    Collection<Role> retailCustomerRoles;


    @Before
    public void initObjects() {

        userId = 1;
        userName = "username";
        password = "password";
        matchingPassword = "password";
        firstName = "firstname";
        lastName = "lastname";
        email = "email@example.com";
        mobile = "123456789";
        street = "street";
        postcode = "12-345";
        city = "city";

        passwordEncoded = "xxxx";

        retailUserForm = new UserForm();
        retailUserForm.setUserName(userName);
        retailUserForm.setPassword(password);
        retailUserForm.setMatchingPassword(matchingPassword);
        retailUserForm.setFirstName(firstName);
        retailUserForm.setLastName(lastName);
        retailUserForm.setEmail(email);
        retailUserForm.setMobile(mobile);
        retailUserForm.setStreet(street);
        retailUserForm.setPostcode(postcode);
        retailUserForm.setCity(city);
        retailUserForm.setId(userId);

        roleNameCustomer = "ROLE_CUSTOMER";
        roleNameRetailCustomer = "ROLE_CUSTOMER_RETAIL";
        roleCustomer = new Role(roleNameCustomer);
        roleRetailCustomer = new Role(roleNameRetailCustomer);
        retailCustomerRoles = new HashSet<>();
        retailCustomerRoles.add(roleCustomer);
        retailCustomerRoles.add(roleRetailCustomer);


    }

    @Test
    public void shouldSaveNewRetailCustomer() {
        ArgumentCaptor<RetailCustomer> argumentCaptor = ArgumentCaptor.forClass(RetailCustomer.class);
        userService.saveNewRetailCustomer(retailUserForm);

        verify(retailCustomerRepository, times(1)).save(argumentCaptor.capture());
    }

    @Test
    public void shouldEncodePasswordWhenForNewRetailCustomer() {
        when(passwordEncoder.encode(password)).thenReturn(passwordEncoded);

        ArgumentCaptor<RetailCustomer> argumentCaptor = ArgumentCaptor.forClass(RetailCustomer.class);
        userService.saveNewRetailCustomer(retailUserForm);

        verify(retailCustomerRepository, times(1)).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getPassword(), passwordEncoded);
    }

    @Test
    public void userFormDataShouldMatchRetailCustomerObject() {

        ArgumentCaptor<RetailCustomer> argumentCaptor = ArgumentCaptor.forClass(RetailCustomer.class);
        userService.saveNewRetailCustomer(retailUserForm);

        verify(retailCustomerRepository, times(1)).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getUserName(), retailUserForm.getUserName());
        assertEquals(argumentCaptor.getValue().getFirstName(), retailUserForm.getFirstName());
        assertEquals(argumentCaptor.getValue().getLastName(), retailUserForm.getLastName());
        assertEquals(argumentCaptor.getValue().getEmail(), retailUserForm.getEmail());
        assertEquals(argumentCaptor.getValue().getMobile(), retailUserForm.getMobile());
        assertEquals(argumentCaptor.getValue().getStreet(), retailUserForm.getStreet());
        assertEquals(argumentCaptor.getValue().getCity(), retailUserForm.getCity());
        assertEquals(argumentCaptor.getValue().getPostcode(), retailUserForm.getPostcode());
    }

    @Test
    public void shouldAssignTwoRolesForRetailCustomer() {
        doReturn(roleRetailCustomer).when(roleRepository).findByName(roleNameRetailCustomer);
        doReturn(roleCustomer).when(roleRepository).findByName(roleNameCustomer);

        ArgumentCaptor<RetailCustomer> argumentCaptor = ArgumentCaptor.forClass(RetailCustomer.class);
        userService.saveNewRetailCustomer(retailUserForm);

        verify(retailCustomerRepository, times(1)).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getRoles().size(), 2);
    }

    @Test
    public void shouldAssignCorrectRolesForRetailCustomer() {
        doReturn(roleRetailCustomer).when(roleRepository).findByName(roleNameRetailCustomer);
        doReturn(roleCustomer).when(roleRepository).findByName(roleNameCustomer);

        ArgumentCaptor<RetailCustomer> argumentCaptor = ArgumentCaptor.forClass(RetailCustomer.class);
        userService.saveNewRetailCustomer(retailUserForm);

        verify(retailCustomerRepository, times(1)).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().hasRole(roleNameRetailCustomer), true);
        assertEquals(argumentCaptor.getValue().hasRole(roleNameCustomer), true);
    }

    @Test
    public void shouldUpdateRetailCustomerProfileData() {
        RetailCustomer customerToBeUpdated = new RetailCustomer();
        customerToBeUpdated.setId(userId);

        doReturn(customerToBeUpdated).when(retailCustomerRepository).getOne(userId);

        ArgumentCaptor<RetailCustomer> argumentCaptor = ArgumentCaptor.forClass(RetailCustomer.class);
        userService.updateRetailCustomerProfile(retailUserForm);
        verify(retailCustomerRepository, times(1)).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getFirstName(), retailUserForm.getFirstName());
        assertEquals(argumentCaptor.getValue().getLastName(), retailUserForm.getLastName());
        assertEquals(argumentCaptor.getValue().getEmail(), retailUserForm.getEmail());
        assertEquals(argumentCaptor.getValue().getMobile(), retailUserForm.getMobile());
        assertEquals(argumentCaptor.getValue().getStreet(), retailUserForm.getStreet());
        assertEquals(argumentCaptor.getValue().getCity(), retailUserForm.getCity());
        assertEquals(argumentCaptor.getValue().getPostcode(), retailUserForm.getPostcode());
    }

    @Test
    public void shouldNotAffectUsernameAndPasswordAndRolesWhileRetailCustomerProfileUpdate() {
        RetailCustomer customerToBeUpdated = new RetailCustomer();
        String currentUsername = "username2";
        String currentPassword = "password2";
        customerToBeUpdated.setUserName(currentUsername);
        customerToBeUpdated.setPassword(currentPassword);
        customerToBeUpdated.setRoles(retailCustomerRoles);
        doReturn(customerToBeUpdated).when(retailCustomerRepository).getOne(userId);

        ArgumentCaptor<RetailCustomer> argumentCaptor = ArgumentCaptor.forClass(RetailCustomer.class);
        userService.updateRetailCustomerProfile(retailUserForm);
        verify(retailCustomerRepository, times(1)).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getUserName(), currentUsername);
        assertEquals(argumentCaptor.getValue().getPassword(), currentPassword);
        assertEquals(argumentCaptor.getValue().getRoles(), retailCustomerRoles);
    }

    @Test
    public void shouldFindRetailCustomerById() {
        retailCustomer = new RetailCustomer();
        retailCustomer.setId(userId);
        retailCustomer.setUserName(userName);
        optionalRetailCustomer = Optional.of(retailCustomer);
        when(retailCustomerRepository.findById(userId)).thenReturn(optionalRetailCustomer);
        assertEquals(optionalRetailCustomer.get(), userService.getRetailCustomerById(userId));
        verify(retailCustomerRepository, times(1)).findById(userId);
    }

}



package com.example.slabiak.appointmentscheduler.service.impl;

import com.example.slabiak.appointmentscheduler.dao.RoleRepository;
import com.example.slabiak.appointmentscheduler.dao.user.customer.RetailCustomerRepository;
import com.example.slabiak.appointmentscheduler.entity.user.Role;
import com.example.slabiak.appointmentscheduler.entity.user.customer.RetailCustomer;
import com.example.slabiak.appointmentscheduler.model.UserForm;
import com.example.slabiak.appointmentscheduler.service.impl.RetailCustomerServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetailCustomerServiceImplTest {

    @Test
    void updateRetailCustomerProfile() {
    }

    @Test
    void saveNewRetailCustomer() {
    }

    @Test
    void getAllRetailCustomers() {
    }

    @Test
    void getRetailCustomerById() {
    }

    @Test
    void getRolesForRetailCustomer() {
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testUpdateRetailCustomerProfile() {
    }

    @Mock
    private RetailCustomerRepository retailCustomerRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RetailCustomerServiceImpl retailCustomerService;


    @org.junit.Test
    public void testSaveNewRetailCustomer() {
        // Arrange
        UserForm userForm = new UserForm();
        userForm.setUserName("testuser");
        userForm.setPassword("password");

        Role roleRetailCustomer = new Role("ROLE_CUSTOMER_RETAIL");
        Role roleCustomer = new Role("ROLE_CUSTOMER");
        when(roleRepository.findByName("ROLE_CUSTOMER_RETAIL")).thenReturn(roleRetailCustomer);
        when(roleRepository.findByName("ROLE_CUSTOMER")).thenReturn(roleCustomer);

        RetailCustomer savedRetailCustomer = new RetailCustomer();
        when(retailCustomerRepository.save(any(RetailCustomer.class))).thenReturn(savedRetailCustomer);

        // Act
        retailCustomerService.saveNewRetailCustomer(userForm);

        // Assert
        verify(passwordEncoder).encode("password");
        verify(retailCustomerRepository).save(any(RetailCustomer.class));
    }

    @org.junit.Test
    public void testGetAllRetailCustomers() {
        // Arrange
        RetailCustomer retailCustomer1 = new RetailCustomer();
        RetailCustomer retailCustomer2 = new RetailCustomer();
        List<RetailCustomer> retailCustomerList = new ArrayList<>();
        retailCustomerList.add(retailCustomer1);
        retailCustomerList.add(retailCustomer2);
        when(retailCustomerRepository.findAll()).thenReturn(retailCustomerList);

        // Act
        List<RetailCustomer> retailCustomers = retailCustomerService.getAllRetailCustomers();

        // Assert
        assertEquals(2, retailCustomers.size());
    }

    @org.junit.Test
    public void testGetRolesForRetailCustomer() {
        // Arrange
        Role roleRetailCustomer = new Role("ROLE_CUSTOMER_RETAIL");
        Role roleCustomer = new Role("ROLE_CUSTOMER");
        when(roleRepository.findByName("ROLE_CUSTOMER_RETAIL")).thenReturn(roleRetailCustomer);
        when(roleRepository.findByName("ROLE_CUSTOMER")).thenReturn(roleCustomer);

        // Act
        Collection<Role> roles = retailCustomerService.getRolesForRetailCustomer();

        // Assert
        assertEquals(2, roles.size());
        assertTrue(roles.contains(roleRetailCustomer));
        assertTrue(roles.contains(roleCustomer));
    }

    @org.junit.Test
    public void testGetRetailCustomerById() {
        // Arrange
        RetailCustomer retailCustomer = new RetailCustomer();
        when(retailCustomerRepository.findById(1)).thenReturn(Optional.of(retailCustomer));

        // Act
        RetailCustomer retrievedRetailCustomer = retailCustomerService.getRetailCustomerById(1);

        // Assert
        assertNotNull(retrievedRetailCustomer);
        assertEquals(retailCustomer, retrievedRetailCustomer);
    }

    @org.junit.Test
    public void testGetRetailCustomerById_ThrowsExceptionWhenNotFound() {
        // Arrange
        when(retailCustomerRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> retailCustomerService.getRetailCustomerById(1));
    }

}



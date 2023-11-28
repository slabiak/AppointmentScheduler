package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.user.Role;
import com.example.slabiak.appointmentscheduler.entity.user.customer.RetailCustomer;
import com.example.slabiak.appointmentscheduler.model.UserForm;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Collection;
import java.util.List;

public interface RetailCustomerService {
    public void saveNewRetailCustomer(UserForm userForm);

    public void updateRetailCustomerProfile(UserForm updateData);

    public List<RetailCustomer> getAllRetailCustomers();

    public RetailCustomer getRetailCustomerById(int retailCustomerId);

    public Collection<Role> getRolesForRetailCustomer();


}

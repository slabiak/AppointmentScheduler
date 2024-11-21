// RetailCustomerService.java

package com.example.slabiak.appointmentscheduler.service.impl;

import com.example.slabiak.appointmentscheduler.dao.RoleRepository;
import com.example.slabiak.appointmentscheduler.dao.user.customer.RetailCustomerRepository;
import com.example.slabiak.appointmentscheduler.entity.user.Role;  // Add this import
import com.example.slabiak.appointmentscheduler.entity.user.customer.RetailCustomer;
import com.example.slabiak.appointmentscheduler.model.UserForm;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
public class RetailCustomerService implements com.example.slabiak.appointmentscheduler.service.RetailCustomerService {

    private final RetailCustomerRepository retailCustomerRepository;
    private final PasswordEncoder passwordEncoder;

    private RoleRepository roleRepository ;

    public RetailCustomerService(RetailCustomerRepository retailCustomerRepository,PasswordEncoder passwordEncoder,RoleRepository roleRepository) {
        this.retailCustomerRepository = retailCustomerRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;

    }

    @Override
    @PreAuthorize("#updateData.id == principal.id or hasRole('ADMIN')")
    public void updateRetailCustomerProfile(UserForm updateData) {
        RetailCustomer retailCustomer = retailCustomerRepository.getOne(updateData.getId());
        retailCustomer.update(updateData);
        retailCustomerRepository.save(retailCustomer);
    }

    @Override
    public void saveNewRetailCustomer(UserForm userForm) {
        RetailCustomer retailCustomer = new RetailCustomer(userForm, passwordEncoder.encode(userForm.getPassword()), getRolesForRetailCustomer());
        retailCustomerRepository.save(retailCustomer);
    }

    @Override
    public List<RetailCustomer> getAllRetailCustomers() {
        return retailCustomerRepository.findAll();
    }


    @Override
    @PreAuthorize("#retailCustomerId == principal.id or hasRole('ADMIN')")
    public RetailCustomer getRetailCustomerById(int retailCustomerId) {
        return retailCustomerRepository.findById(retailCustomerId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }


    public Collection<Role> getRolesForRetailCustomer() {
        HashSet<Role> roles = new HashSet();
        roles.add(roleRepository.findByName("ROLE_CUSTOMER_RETAIL"));
        roles.add(roleRepository.findByName("ROLE_CUSTOMER"));
        return roles;
    }

}

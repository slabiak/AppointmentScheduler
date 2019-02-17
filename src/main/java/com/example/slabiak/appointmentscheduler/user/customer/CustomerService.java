package com.example.slabiak.appointmentscheduler.user.customer;


import com.example.slabiak.appointmentscheduler.user.customer.Customer;

import java.util.List;

public interface CustomerService{
    void save(Customer customer);
    Customer findById(int id);
    List<Customer> findAll();
    void deleteById(int id);

}

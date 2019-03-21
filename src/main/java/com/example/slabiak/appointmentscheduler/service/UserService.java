package com.example.slabiak.appointmentscheduler.service;


import com.example.slabiak.appointmentscheduler.model.UserFormDTO;
import com.example.slabiak.appointmentscheduler.entity.*;
import com.example.slabiak.appointmentscheduler.entity.user.*;
import com.example.slabiak.appointmentscheduler.entity.user.customer.CorporateCustomer;
import com.example.slabiak.appointmentscheduler.entity.user.customer.Customer;
import com.example.slabiak.appointmentscheduler.entity.user.customer.RetailCustomer;
import com.example.slabiak.appointmentscheduler.entity.user.provider.Provider;

import java.util.Collection;
import java.util.List;

public interface UserService {

    User getUserById(int userId);
    User findById(int id);
    User findByUserName( String userName);
    List<User> findByRoleName(String roleName);
    List<User> findAll();
    List<Provider> findByWorks(Work work);
    void deleteById(int id);
    List<Provider> getAllProvidersWithRetailWorks();
    List<Provider> getAllProvidersWithCorporateWorks();
    List<Provider> getAllProviders();
    List<Customer> getAllCustomers();
    List<RetailCustomer> getAllRetailCustomers();

    void saveNewRetailCustomer(UserFormDTO userForm);
    void saveNewCorporateCustomer(UserFormDTO userForm);
    void saveNewProvider(UserFormDTO userForm);

    Provider getProviderById(int providerId);
    Customer getCustomerById(int customerId);
    RetailCustomer getRetailCustomerById(int retailCustomerId);
    CorporateCustomer getCorporateCustomerById(int corporateCustomerId);

    void updateProviderProfile(UserFormDTO updateData);
    void updateRetailCustomerProfile(UserFormDTO updateData);
    void updateCorporateCustomerProfile(UserFormDTO updateData);

    boolean updateUserPassword(UserFormDTO userForm);

    Collection<Role> getRetailCustomerRoles();
    Collection<Role> getProviderRoles();
    Collection<Role> getCorporateCustomerRoles();
}


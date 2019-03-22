package com.example.slabiak.appointmentscheduler.service;


import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.entity.user.Role;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.entity.user.customer.CorporateCustomer;
import com.example.slabiak.appointmentscheduler.entity.user.customer.Customer;
import com.example.slabiak.appointmentscheduler.entity.user.customer.RetailCustomer;
import com.example.slabiak.appointmentscheduler.entity.user.provider.Provider;
import com.example.slabiak.appointmentscheduler.model.UserFormDTO;

import java.util.Collection;
import java.util.List;

public interface UserService {
    /*
    * User
    * */
    User getUserById(int userId);
    User getUserByUsername(String userName);
    List<User> getUsersByRoleName(String roleName);
    List<User> getAllUsers();
    void deleteUserById(int userId);
    boolean updateUserPassword(UserFormDTO userForm);

    /*
    * Provider
    * */
    Provider getProviderById(int providerId);
    List<Provider> getProvidersWithRetailWorks();
    List<Provider> getProvidersWithCorporateWorks();
    List<Provider> getProvidersByWork(Work work);
    List<Provider> getAllProviders();
    void saveNewProvider(UserFormDTO userForm);
    void updateProviderProfile(UserFormDTO updateData);
    Collection<Role> getRolesForProvider();

    /*
    * Customer
    * */
    Customer getCustomerById(int customerId);
    List<Customer> getAllCustomers();

    /*
    * RetailCustomer
    * */
    RetailCustomer getRetailCustomerById(int retailCustomerId);
    void saveNewRetailCustomer(UserFormDTO userForm);
    void updateRetailCustomerProfile(UserFormDTO updateData);
    Collection<Role> getRolesForRetailCustomer();

    /*
    * CorporateCustomer
    * */
    CorporateCustomer getCorporateCustomerById(int corporateCustomerId);
    List<RetailCustomer> getAllRetailCustomers();
    void saveNewCorporateCustomer(UserFormDTO userForm);
    void updateCorporateCustomerProfile(UserFormDTO updateData);
    Collection<Role> getRoleForCorporateCustomers();










}


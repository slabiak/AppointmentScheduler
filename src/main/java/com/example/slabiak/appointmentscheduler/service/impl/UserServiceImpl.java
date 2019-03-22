package com.example.slabiak.appointmentscheduler.service.impl;

import com.example.slabiak.appointmentscheduler.dao.RoleRepository;
import com.example.slabiak.appointmentscheduler.dao.user.UserRepository;
import com.example.slabiak.appointmentscheduler.dao.user.customer.CorporateCustomerRepository;
import com.example.slabiak.appointmentscheduler.dao.user.customer.CustomerRepository;
import com.example.slabiak.appointmentscheduler.dao.user.customer.RetailCustomerRepository;
import com.example.slabiak.appointmentscheduler.dao.user.provider.ProviderRepository;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;
import com.example.slabiak.appointmentscheduler.entity.user.Role;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.entity.user.customer.CorporateCustomer;
import com.example.slabiak.appointmentscheduler.entity.user.customer.Customer;
import com.example.slabiak.appointmentscheduler.entity.user.customer.RetailCustomer;
import com.example.slabiak.appointmentscheduler.entity.user.provider.Provider;
import com.example.slabiak.appointmentscheduler.model.UserFormDTO;
import com.example.slabiak.appointmentscheduler.service.UserService;
import com.example.slabiak.appointmentscheduler.service.WorkingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    ProviderRepository providerRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CorporateCustomerRepository corporateCustomerRepository;

    @Autowired
    RetailCustomerRepository retailCustomerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private WorkingPlanService workingPlanService;


    @Override
    public User getUserById(int userId) {
        Optional<User> result = userRepository.findById(userId);
        User user = null;
        if (result.isPresent()) {
            user = result.get();
        }
        else {
            // todo throw new excep
        }
        return user;
    }

    @Override
    public Provider getProviderById(int providerId) {
        Optional<Provider> optionalProvider = providerRepository.findById(providerId);
        Provider provider = null;
        if (optionalProvider.isPresent()) {
            provider = optionalProvider.get();
        }
        else {
            // todo throw new excep
        }
        return provider;
    }

    @Override
    public Customer getCustomerById(int customerId) {
        return customerRepository.getOne(customerId);
    }

    @Override
    public RetailCustomer getRetailCustomerById(int retailCustomerId) {
        Optional<RetailCustomer> optionalRetailCustomer = retailCustomerRepository.findById(retailCustomerId);
        RetailCustomer retailCustomer = null;
        if (optionalRetailCustomer.isPresent()) {
            retailCustomer = optionalRetailCustomer.get();
        }
        else {
            // todo throw new excep
        }
        return retailCustomer;
    }

    @Override
    public CorporateCustomer getCorporateCustomerById(int corporateCustomerId) {
        Optional<CorporateCustomer> optionalCorporateCustomer = corporateCustomerRepository.findById(corporateCustomerId);
        CorporateCustomer corporateCustomer = null;
        if (optionalCorporateCustomer.isPresent()) {
            corporateCustomer = optionalCorporateCustomer.get();
        }
        else {
            // todo throw new excep
        }
        return corporateCustomer;
    }

    @Override
    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public List<RetailCustomer> getAllRetailCustomers() {
        return retailCustomerRepository.findAll();
    }


    @Override
    public User getUserByUsername(String userName) {
        Optional<User> result = userRepository.findByUserName(userName);

        User user = null;

        if (result.isPresent()) {
            user = result.get();
        }
        else {
            // todo throw new excep
        }
        return user;
    }

    @Override
    public List<User> getUsersByRoleName(String roleName) {
        return userRepository.findByRoleName(roleName);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(int userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<Provider> getProvidersWithRetailWorks() {
        return providerRepository.findAllWithRetailWorks();
    }

    @Override
    public List<Provider> getProvidersWithCorporateWorks() {
        return providerRepository.findAllWithCorporateWorks();
    }

    @Override
    public List<Provider> getProvidersByWork(Work work) {
        return providerRepository.findByWorks(work);
    }

    @Override
    public boolean updateUserPassword(UserFormDTO userForm) {
        User user = userRepository.getOne(userForm.getId());
        if(!userForm.getPassword().equals(userForm.getMatchingPassword())){
            return false;
        } else if(passwordEncoder.matches(userForm.getCurrentPassword(),user.getPassword())){
            user.setPassword(passwordEncoder.encode(userForm.getPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void updateProviderProfile(UserFormDTO updateData) {
     Provider provider = providerRepository.getOne(updateData.getId());
     provider.update(updateData);
     providerRepository.save(provider);
    }

    @Override
    public void updateRetailCustomerProfile(UserFormDTO updateData) {
        RetailCustomer retailCustomer = retailCustomerRepository.getOne(updateData.getId());
        retailCustomer.update(updateData);
        retailCustomerRepository.save(retailCustomer);

    }

    @Override
    public void updateCorporateCustomerProfile(UserFormDTO updateData) {
        CorporateCustomer corporateCustomer = corporateCustomerRepository.getOne(updateData.getId());
        corporateCustomer.update(updateData);
        corporateCustomerRepository.save(corporateCustomer);

    }

    @Override
    public void saveNewRetailCustomer(UserFormDTO userForm) {
        RetailCustomer retailCustomer = new RetailCustomer(userForm,passwordEncoder.encode(userForm.getPassword()), getRolesForRetailCustomer());
        retailCustomerRepository.save(retailCustomer);
    }

    @Override
    public void saveNewCorporateCustomer(UserFormDTO userForm) {
        CorporateCustomer corporateCustomer = new CorporateCustomer(userForm,passwordEncoder.encode(userForm.getPassword()), getRoleForCorporateCustomers());
        corporateCustomerRepository.save(corporateCustomer);
    }

    @Override
    public void saveNewProvider(UserFormDTO userForm) {
        WorkingPlan workingPlan = workingPlanService.generateDefaultWorkingPlan();
        Provider provider = new Provider(userForm,passwordEncoder.encode(userForm.getPassword()), getRolesForProvider(),workingPlan);
        providerRepository.save(provider);
    }

    @Override
    public Collection<Role> getRolesForRetailCustomer() {
        HashSet<Role> roles = new HashSet<Role>();
        roles.add(roleRepository.findByName("ROLE_CUSTOMER_RETAIL"));
        roles.add(roleRepository.findByName("ROLE_CUSTOMER"));
        return roles;
    }


    @Override
    public Collection<Role> getRoleForCorporateCustomers() {
        HashSet<Role> roles = new HashSet<Role>();
        roles.add(roleRepository.findByName("ROLE_CUSTOMER_CORPORATE"));
        roles.add(roleRepository.findByName("ROLE_CUSTOMER"));
        return roles;
    }

    @Override
    public Collection<Role> getRolesForProvider() {
        HashSet<Role> roles = new HashSet<Role>();
        roles.add(roleRepository.findByName("ROLE_PROVIDER"));
        return roles;
    }


}


package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.dao.RoleRepository;
import com.example.slabiak.appointmentscheduler.dao.UserRepository;
import com.example.slabiak.appointmentscheduler.dto.UserFormDTO;
import com.example.slabiak.appointmentscheduler.entity.Role;
import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;
import com.example.slabiak.appointmentscheduler.model.UserRegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private WorkingPlanService workingPlanService;


    @Override
    public void saveNewUser(UserFormDTO userForm) {
        User user = new User();
        user.setUserName(userForm.getUserName());
        user.setPassword(passwordEncoder.encode(userForm.getNewPassword()));
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setEmail(userForm.getEmail());
        user.setCity(userForm.getCity());
        user.setStreet(userForm.getStreet());
        user.setPostcode(userForm.getPostcode());
        user.setMobile(userForm.getMobile());
        user.setRoles(getCustomerRoles());
        if(userForm.isProviderAccount()){
            user.setWorks(userForm.getWorks());
            user.setRoles(getProviderRoles());
            WorkingPlan wp= workingPlanService.generateDefaultWorkingPlan();
            wp.setProvider(user);
            user.setWorkingPlan(wp);
        }
        userRepository.save(user);
    }


    @Override
    public User findById(int id) {
        Optional<User> result = userRepository.findById(id);

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
    public User findByUserName(String userName) {
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
    public List<User> findByRoleName(String roleName) {
        return userRepository.findByRoleName(roleName);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findByWorks(Work work) {
        return userRepository.findByWorks(work);
    }

    @Override
    public void updateUserProfile(UserFormDTO updateData) {
        User user = findById(updateData.getId());
        user.setFirstName(updateData.getFirstName());
        user.setLastName(updateData.getLastName());
        user.setEmail(updateData.getEmail());
        user.setMobile(updateData.getMobile());
        user.setStreet(updateData.getStreet());
        user.setCity(updateData.getCity());
        user.setPostcode(updateData.getPostcode());
        user.setWorks(updateData.getWorks());
        userRepository.save(user);
    }

    @Override
    public boolean updateUserPassword(int userId, String currentPassword, String newPassword, String matchingPassword) {
        User user = userRepository.getOne(userId);
        if(!newPassword.equals(matchingPassword)){
            System.out.println("1");
            return false;
        } else if(passwordEncoder.matches(currentPassword,user.getPassword())){
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            System.out.println("2");
            return true;
        }
        System.out.println("3");
        return false;

    }

    @Override
    public Collection<Role> getCustomerRoles() {
        Role role = roleRepository.findByName("ROLE_CUSTOMER");
        HashSet<Role> roles = new HashSet<Role>();
        roles.add(role);
        return roles;
    }

    @Override
    public Collection<Role> getProviderRoles() {
        Role role = roleRepository.findByName("ROLE_PROVIDER");
        HashSet<Role> roles = new HashSet<Role>();
        roles.add(role);
        return roles;
    }
}


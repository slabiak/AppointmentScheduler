package com.example.slabiak.appointmentscheduler.service;


import com.example.slabiak.appointmentscheduler.dto.UserFormDTO;
import com.example.slabiak.appointmentscheduler.entity.Role;
import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.model.UserRegisterForm;

import java.util.Collection;
import java.util.List;

public interface UserService {

    User findById(int id);
    User findByUserName( String userName);
    List<User> findByRoleName(String roleName);
    List<User> findAll();
    List<User> findByWorks(Work work);

    void saveNewUser(UserFormDTO userForm);

    void updateUserProfile(UserFormDTO updateData);
    boolean updateUserPassword(int userId, String currentPassword, String newPassword, String matchingPassword);


    void deleteById(int id);

    Collection<Role> getCustomerRoles();
    Collection<Role> getProviderRoles();
}


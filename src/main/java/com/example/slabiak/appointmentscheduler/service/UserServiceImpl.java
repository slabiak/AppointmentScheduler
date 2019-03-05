package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.dao.RoleRepository;
import com.example.slabiak.appointmentscheduler.dao.UserRepository;
import com.example.slabiak.appointmentscheduler.entity.Role;
import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;
import com.example.slabiak.appointmentscheduler.model.DayPlan;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;
import com.example.slabiak.appointmentscheduler.model.UserRegisterForm;
import org.apache.tomcat.jni.Local;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.swing.BakedArrayList;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private WorkService workService;

    @Override
    public void registerCustomer(UserRegisterForm userForm) {
        User user = new User();
        user.setUserName(userForm.getUserName());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setEmail(userForm.getEmail());
        Role role = roleRepository.findByName("ROLE_CUSTOMER");
        HashSet<Role> roles = new HashSet<Role>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public void registerProvider(UserRegisterForm userForm) {
        User user = new User();
        user.setUserName(userForm.getUserName());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setEmail(userForm.getEmail());
        List<Work> works = new ArrayList<Work>();
        for(int workId : userForm.getSelectedWorks()){
            works.add(workService.findById(workId));
        }
        user.setWorks(works);
        Role role = roleRepository.findByName("ROLE_PROVIDER");
        HashSet<Role> roles = new HashSet<Role>();
        roles.add(role);
        user.setRoles(roles);
        WorkingPlan wp= new WorkingPlan();
        wp.setMonday(new DayPlan(new TimePeroid(LocalTime.parse("06:00"),LocalTime.parse("18:00"))));
        wp.setTuesday(new DayPlan(new TimePeroid(LocalTime.of(06,00),LocalTime.of(18,00))));
        wp.setWednesday(new DayPlan(new TimePeroid(LocalTime.of(06,00),LocalTime.of(18,00))));
        wp.setThursday(new DayPlan(new TimePeroid(LocalTime.of(06,00),LocalTime.of(18,00))));
        wp.setFriday(new DayPlan(new TimePeroid(LocalTime.of(06,00),LocalTime.of(18,00))));
        wp.setSaturday(new DayPlan(new TimePeroid(LocalTime.of(06,00),LocalTime.of(18,00))));
        wp.setSunday(new DayPlan(new TimePeroid(LocalTime.of(06,00),LocalTime.of(18,00))));
        wp.setProvider(user);
        user.setWorkingPlan(wp);
        userRepository.save(user);
    }

    @Override
    public void updateProvider(User userUpdateData) {
        User user = findById(userUpdateData.getId());
        user.setFirstName(userUpdateData.getFirstName());
        user.setLastName(userUpdateData.getLastName());
        user.setEmail(userUpdateData.getEmail());
        user.setWorks(userUpdateData.getWorks());
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
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = findByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}


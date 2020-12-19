package com.example.slabiak.appointmentscheduler.service.impl;

import com.example.slabiak.appointmentscheduler.dao.WorkRepository;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.entity.user.customer.Customer;
import com.example.slabiak.appointmentscheduler.exception.WorkNotFoundException;
import com.example.slabiak.appointmentscheduler.service.UserService;
import com.example.slabiak.appointmentscheduler.service.WorkService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkServiceImpl implements WorkService {

    private final WorkRepository workRepository;
    private final UserService userService;

    public WorkServiceImpl(WorkRepository workRepository, UserService userService) {
        this.workRepository = workRepository;
        this.userService = userService;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void createNewWork(Work work) {
        workRepository.save(work);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void updateWork(Work workUpdateData) {
        Work work = getWorkById(workUpdateData.getId());
        work.setName(workUpdateData.getName());
        work.setPrice(workUpdateData.getPrice());
        work.setDuration(workUpdateData.getDuration());
        work.setDescription(workUpdateData.getDescription());
        work.setEditable(workUpdateData.getEditable());
        work.setTargetCustomer(workUpdateData.getTargetCustomer());
        workRepository.save(work);
    }

    @Override
    public Work getWorkById(int workId) {
        return workRepository.findById(workId).orElseThrow(WorkNotFoundException::new);
    }

    @Override
    public List<Work> getAllWorks() {
        return workRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteWorkById(int workId) {
        workRepository.deleteById(workId);
    }

    @Override
    public boolean isWorkForCustomer(int workId, int customerId) {
        Customer customer = userService.getCustomerById(customerId);
        Work work = getWorkById(workId);
        if (customer.hasRole("ROLE_CUSTOMER_RETAIL") && !work.getTargetCustomer().equals("retail")) {
            return false;
        } else return !customer.hasRole("ROLE_CUSTOMER_CORPORATE") || work.getTargetCustomer().equals("corporate");
    }

    @Override
    public List<Work> getWorksByProviderId(int providerId) {
        return workRepository.findByProviderId(providerId);
    }

    @Override
    public List<Work> getRetailCustomerWorks() {
        return workRepository.findByTargetCustomer("retail");
    }

    @Override
    public List<Work> getCorporateCustomerWorks() {
        return workRepository.findByTargetCustomer("corporate");
    }

    @Override
    public List<Work> getWorksForRetailCustomerByProviderId(int providerId) {
        return workRepository.findByTargetCustomerAndProviderId("retail", providerId);
    }

    @Override
    public List<Work> getWorksForCorporateCustomerByProviderId(int providerId) {
        return workRepository.findByTargetCustomerAndProviderId("corporate", providerId);
    }


}

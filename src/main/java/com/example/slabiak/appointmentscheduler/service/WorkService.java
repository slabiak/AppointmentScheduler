package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.Work;

import java.util.List;

public interface WorkService {
    void save(Work work);
    void update(Work work);
    Work findById(int id);
    List<Work> findAll();
    void deleteById(int id);
    List<Work> findByProviderId(int providerId);
    List<Work> getWorksForRetailCustomer();
    List<Work> getWorksForCorporateCustomer();

    List<Work> getWorksForRetailCustomer(int providerId);
    List<Work> getWorksForCorporateCustomer(int providerId);
}

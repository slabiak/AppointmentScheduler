package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.Work;

import java.util.List;

public interface WorkService {
    void createNewWork(Work work);
    Work getWorkById(int workId);
    List<Work> getAllWorks();
    List<Work> getWorksByProviderId(int providerId);
    List<Work> getRetailCustomerWorks();
    List<Work> getCorporateCustomerWorks();
    List<Work> getRetailCustomerWorksByProviderId(int providerId);
    List<Work> getCorporateCustomerWorksByProviderId(int providerId);
    void updateWork(Work work);
    void deleteWorkById(int workId);
}

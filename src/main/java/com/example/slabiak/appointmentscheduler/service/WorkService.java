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

    List<Work> getWorksForRetailCustomerByProviderId(int providerId);

    List<Work> getWorksForCorporateCustomerByProviderId(int providerId);

    void updateWork(Work work);

    void deleteWorkById(int workId);

    boolean isWorkForCustomer(int workId, int customerId);
}

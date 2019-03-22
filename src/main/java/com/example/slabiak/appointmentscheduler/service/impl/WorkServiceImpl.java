package com.example.slabiak.appointmentscheduler.service.impl;

import com.example.slabiak.appointmentscheduler.dao.WorkRepository;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkServiceImpl implements WorkService {
    
    @Autowired
    WorkRepository workRepository;


    @Override
    public void createNewWork(Work work) {
        workRepository.save(work);
    }

    @Override
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
        Optional<Work> result = workRepository.findById(workId);

        Work work = null;

        if (result.isPresent()) {
            work = result.get();
        }
        else {
            // todo throw new excep
        }

        return work;
    }


    @Override
    public List<Work> getAllWorks() {
        return workRepository.findAll();
    }

    @Override
    public void deleteWorkById(int workId) {
        workRepository.deleteById(workId);
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
    public List<Work> getRetailCustomerWorksByProviderId(int providerId) {
        return workRepository.findByTargetCustomerAndProviderId("retail",providerId);
    }

    @Override
    public List<Work> getCorporateCustomerWorksByProviderId(int providerId) {
        return workRepository.findByTargetCustomerAndProviderId("corporate",providerId);
    }


}

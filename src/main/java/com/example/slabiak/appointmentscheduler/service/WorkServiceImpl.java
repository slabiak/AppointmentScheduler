package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.dao.WorkRepository;
import com.example.slabiak.appointmentscheduler.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkServiceImpl implements WorkService {
    
    @Autowired
    WorkRepository workRepository;


    @Override
    public void save(Work work) {
        workRepository.save(work);
    }

    @Override
    public void update(Work workUpdateData) {
        Work work = findById(workUpdateData.getId());
        work.setName(workUpdateData.getName());
        work.setPrice(workUpdateData.getPrice());
        work.setDuration(workUpdateData.getDuration());
        work.setDescription(workUpdateData.getDescription());
        work.setEditable(workUpdateData.getEditable());
        workRepository.save(work);
    }

    @Override
    public Work findById(int id) {
        Optional<Work> result = workRepository.findById(id);

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
    public List<Work> findAll() {
        return workRepository.findAll();
    }

    @Override
    public void deleteById(int id) {
        workRepository.deleteById(id);
    }

    @Override
    public List<Work> findByProviderId(int providerId) {
        return workRepository.findByProviderId(providerId);
    }
}

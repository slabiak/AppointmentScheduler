package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.dao.WorkRepository;
import com.example.slabiak.appointmentscheduler.model.Work;
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
    public void save(Work work) {
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
}

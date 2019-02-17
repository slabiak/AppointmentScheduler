package com.example.slabiak.appointmentscheduler.work;

import java.util.List;

public interface WorkService {
    void save(Work customer);
    Work findById(int id);
    List<Work> findAll();
    void deleteById(int id);
}

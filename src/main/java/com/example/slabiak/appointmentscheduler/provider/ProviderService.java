package com.example.slabiak.appointmentscheduler.provider;


import java.util.List;

public interface ProviderService {
    void save(Provider customer);
    Provider findById(int id);
    List<Provider> findAll();
    void deleteById(int id);
}

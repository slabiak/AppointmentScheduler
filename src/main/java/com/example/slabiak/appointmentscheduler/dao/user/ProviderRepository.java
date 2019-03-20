package com.example.slabiak.appointmentscheduler.dao.user;

import com.example.slabiak.appointmentscheduler.dao.user.CommonUserRepository;
import com.example.slabiak.appointmentscheduler.entity.user.provider.Provider;
import com.example.slabiak.appointmentscheduler.entity.Work;

import java.util.List;

public interface ProviderRepository extends CommonUserRepository<Provider> {
       List<Provider> findByWorks(Work work);
}

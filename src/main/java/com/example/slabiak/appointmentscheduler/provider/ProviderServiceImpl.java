package com.example.slabiak.appointmentscheduler.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderServiceImpl implements  ProviderService {

    @Autowired
    ProviderRepository providerRepository;


    @Override
    public void save(Provider provider) {
        providerRepository.save(provider);
    }

    @Override
    public Provider findById(int id) {
        Optional<Provider> result = providerRepository.findById(id);

        Provider provider = null;

        if (result.isPresent()) {
            provider = result.get();
        }
        else {
            // todo throw new excep
        }

        return provider;
    }


    @Override
    public List<Provider> findAll() {
        return providerRepository.findAll();
    }

    @Override
    public void deleteById(int id) {
        providerRepository.deleteById(id);
    }

}


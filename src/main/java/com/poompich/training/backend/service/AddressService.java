package com.poompich.training.backend.service;

import com.poompich.training.backend.entity.Address;
import com.poompich.training.backend.entity.User;
import com.poompich.training.backend.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    private final AddressRepository repository;

    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    public List<Address> findByUser(User user) {
        return repository.findByUser(user);
    }

    public Address create(User user, String line1, String line2, String zipCode) {
        Address entity = new Address();
        entity.setUser(user);
        entity.setLine1(line1);
        entity.setLine2(line2);
        entity.setZipCode(zipCode);

        return repository.save(entity);
    }
}

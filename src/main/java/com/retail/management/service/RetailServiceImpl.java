package com.retail.management.service;

import com.retail.management.entity.Retailer;
import com.retail.management.repository.RetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RetailServiceImpl implements RetailService{

    @Autowired private RetailRepository repository;


    @Override
    public boolean createRetailer(Retailer retailer) {
        Retailer retail = new Retailer();
        retail.setName(retailer.getName());
        retail.setAddress(retailer.getAddress());
        retail.setCity(retailer.getCity());
        retail.setState(retailer.getState());
        retail.setEmail(retailer.getEmail());
        retail.setMobile(retailer.getMobile());
        repository.save(retail);
        return true;
    }

    @Override
    public boolean updateRetailer(String retailId, Retailer retailer) {
        Optional<Retailer> byId = repository.findById(retailId);
        if (byId.isPresent()) {
            Retailer entity = byId.get();
            entity.setName(retailer.getName());
            entity.setAddress(retailer.getAddress());
            entity.setCity(retailer.getCity());
            entity.setState(retailer.getState());
            entity.setEmail(retailer.getEmail());
            entity.setMobile(retailer.getMobile());
            repository.save(entity);
            return true;
        }
        else {
            return false;
        }


    }

    @Override
    public boolean deleteRetailer(String retailId) {
        Optional<Retailer> byId = repository.findById(retailId);
        if (byId.isPresent()) {
            Retailer retailer = byId.get();
            repository.delete(retailer);
            return true;
        }
        else {
            return false;
        }

    }

    @Override
    public Retailer getRetailer(String retailId) {
        return repository.findById(retailId).get();
    }

    @Override
    public List<Retailer> getAllRetailers() {
        return repository.findAll();
    }
}

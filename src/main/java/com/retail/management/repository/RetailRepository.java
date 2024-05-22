package com.retail.management.repository;

import com.retail.management.entity.Retailer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RetailRepository extends MongoRepository<Retailer,String> {
}

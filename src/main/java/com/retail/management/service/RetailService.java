package com.retail.management.service;

import com.retail.management.entity.Retailer;

import java.util.List;

public interface RetailService {

    boolean createRetailer(Retailer retailer);

    boolean updateRetailer(String retailId,Retailer retailer);

    boolean deleteRetailer(String retailId);

    Retailer getRetailer(String retailId);
    List<Retailer> getAllRetailers();
}

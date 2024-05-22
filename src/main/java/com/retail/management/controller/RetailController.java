package com.retail.management.controller;

import com.retail.management.entity.Retailer;
import com.retail.management.service.RetailService;
import io.prometheus.client.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/RETAIL-APP")
public class RetailController {


    @Autowired private RetailService service;

    static final Counter counter = Counter.build()
            .name("total_request")
            .help("total number of requests")
            .register();


    @PostMapping("/register")
    public ResponseEntity<String> registerRetailer(@RequestBody Retailer retailer){
        counter.inc();
        service.createRetailer(retailer);
        return new ResponseEntity<>("Retailer registered successfully..",HttpStatus.CREATED);
    }

    @PutMapping("/modify/{retailerId}")
    public ResponseEntity<String> modifyRetailer(@PathVariable String retailerId, @RequestBody Retailer retailer){
        counter.inc();
        boolean status = service.updateRetailer(retailerId, retailer);
        if (status){
            return new ResponseEntity<>("Retailer Id '"+retailerId+"'modified successfully..",HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Retailer not found..",HttpStatus.NOT_FOUND);
        }

    }
    @DeleteMapping("/delete/{retailerId}")
    public ResponseEntity<String> deleteRetailer(@PathVariable String retailerId){
        counter.inc();
        boolean status = service.deleteRetailer(retailerId);
        if (status){
            return new ResponseEntity<>("Retailer deleted successfully..",HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Retailer Id '"+retailerId+"'not found..",HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/retailer/{retailerId}")
    public ResponseEntity<Retailer> getRetailer(@PathVariable String retailerId){
        counter.inc();
        return ResponseEntity.ok(service.getRetailer(retailerId));
    }
    @GetMapping("/retailers")
    public ResponseEntity<List<Retailer>> getRetailers(){
        counter.inc();
        return ResponseEntity.ok(service.getAllRetailers());
    }


}

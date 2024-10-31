package com.retail.management.retail_service_impl_test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.retail.management.entity.Retailer;
import com.retail.management.repository.RetailRepository;
import com.retail.management.service.RetailServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
public class CreateRetailerTest {

    Retailer retailer;
    AutoCloseable autoCloseable;
    String requestJson;
    @InjectMocks
    private RetailServiceImpl retailService;

    @MockBean
    private RetailRepository retailRepository;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        autoCloseable = MockitoAnnotations.openMocks(this);
        retailer = new Retailer("1", "ABC", "AddressTest", "Pune", "Mah", "12345", "abc@gmail.com");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writer().withDefaultPrettyPrinter();
        requestJson = writer.writeValueAsString(retailer);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void retailer_create_test() {


    }
}

package com.retail.management.restretailcontrollertest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.retail.management.controller.RetailController;
import com.retail.management.entity.Retailer;
import com.retail.management.service.RetailService;
import io.prometheus.client.Counter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RetailController.class)

public class RegisterRetailerTest {

    Retailer retailer;
    AutoCloseable autoCloseable;
    String requestJson;
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private RetailController retailController;
    @Mock
    private Counter counter;
    @MockBean
    private RetailService retailService;

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
    void registerRetailerTest() throws Exception {

        given(retailService.createRetailer(retailer)).willReturn(true);
        mockMvc.perform(post("/RETAIL-APP/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void registerRetailer_withNullInput_shouldReturnBadRequest() throws Exception {
        // Given
        Retailer nullRetailer = null;

        // When
        mockMvc.perform(post("/RETAIL-APP/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(nullRetailer)))
                .andExpect(status().isBadRequest());

        // Then
        verify(retailService, never()).createRetailer(any(Retailer.class));
    }

    private String asJsonString(final Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    @Test
    void modifyRetailer_shouldReturn200OkAndUpdateRetailer_whenRetailerIdExists() throws Exception {
        given(retailService.updateRetailer(any(String.class), any(Retailer.class))).willReturn(true);

        mockMvc.perform(put("/RETAIL-APP/modify/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"retailerId\": \"123\", \"retailerName\": \"Test Retailer Updated\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void modifyRetailer_shouldReturn400_whenRequestBodyIsEmpty() throws Exception {
        mockMvc.perform(put("/RETAIL-APP/modify/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());

    }

    @Test
    void modifyRetailer_notFound_returns404() {
        // given
        String retailerId = "test123";


        when(retailService.updateRetailer(retailerId, retailer)).thenReturn(false);

        // when
        ResponseEntity<String> response = retailController.modifyRetailer(retailerId, retailer);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Retailer not found..");
    }

    @Test
    public void testGetRetailer_whenRetailerExists_shouldReturn200_OkAndRetailer() {
        // given
        String retailerId = "123";

        when(retailService.getRetailer(retailerId)).thenReturn(retailer);

        // when
        ResponseEntity<Retailer> responseEntity = retailController.getRetailer(retailerId);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(retailer);
    }

    @Test
    public void testGetRetailers_whenRetailersExist_shouldReturn200_OkAndRetailList() {
        // Arrange
        when(retailService.getAllRetailers()).thenReturn(List.of(retailer));

        // Act
        ResponseEntity<List<Retailer>> responseEntity = retailController.getRetailers();

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(List.of(retailer));
    }

    @Test
    public void testGetRetailers_whenNoRetailersExist_shouldReturn200OkAndEmptyList() {
        // Arrange
        when(retailService.getAllRetailers()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Retailer>> responseEntity = retailController.getRetailers();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.emptyList(), responseEntity.getBody());
    }

    @Test
    public void testDeleteRetailer_whenRetailerIdExists_returns200() {
        // Given
        String retailerId = "123";
        when(retailService.deleteRetailer(retailerId)).thenReturn(true);

        // When
        ResponseEntity<String> responseEntity = retailController.deleteRetailer(retailerId);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Retailer deleted successfully..");
    }

    @Test
    void deleteRetailer_notFound() throws Exception {
        given(retailService.deleteRetailer(anyString())).willReturn(false);
        mockMvc.perform(delete("/RETAIL-APP/delete/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteRetailer_shouldDecreaseRetailersBy1_whenRetailerIdExists() {
        // Given
        String retailerId = "123";
        when(retailService.deleteRetailer(retailerId)).thenReturn(true);

        // When
        ResponseEntity<String> response = retailController.deleteRetailer(retailerId);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Retailer deleted successfully..");
        verify(retailService, times(1)).deleteRetailer(retailerId);
    }
}

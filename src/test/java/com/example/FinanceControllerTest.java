package com.example;

import com.example.controller.FinanceController;
import com.example.model.FinanceData;
import com.example.service.FinanceService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FinanceControllerTest {

    @Mock
    private FinanceService financeService;

    @InjectMocks
    private FinanceController financeController;

    private FinanceData financeData;

    @BeforeEach
    void setUp() {
        // Setup mock data for the test
        financeData = new FinanceData();
        financeData.setId(1L);
        financeData.setBaseCurrency("USD");
        financeData.setTargetCurrency("EUR");
        financeData.setExchangeRate(0.85);
    }

    @Test
    void testFetchAndSaveExchangeRateData_success() {
        // Mock the service method
        doNothing().when(financeService).fetchAndSaveExchangeRateData(anyString(), anyString());

        // Call the controller method
        Response response = financeController.fetchAndSaveExchangeRateData("USD", "EUR");

        // Verify the response
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Exchange rate data saved successfully.", response.getEntity());

        // Verify that the service method was called
        verify(financeService, times(1)).fetchAndSaveExchangeRateData("USD", "EUR");
    }

    @Test
    void testFetchAndSaveExchangeRateData_missingCurrency() {
        // Call the controller method with missing currencies
        Response response = financeController.fetchAndSaveExchangeRateData(null, "EUR");

        // Verify the response
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Base and target currencies are required.", response.getEntity());
    }

    @Test
    void testGetFinanceDataById_found() {
        // Mock the service method
        when(financeService.getFinanceDataById(1L)).thenReturn(Optional.of(financeData));

        // Call the controller method
        Response response = financeController.getFinanceDataById(1L);

        // Verify the response
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(financeData, response.getEntity());

        // Verify the service method was called
        verify(financeService, times(1)).getFinanceDataById(1L);
    }

    @Test
    void testGetFinanceDataById_notFound() {
        // Mock the service method to return empty
        when(financeService.getFinanceDataById(1L)).thenReturn(Optional.empty());

        // Call the controller method
        Response response = financeController.getFinanceDataById(1L);

        // Verify the response
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Finance data not found for ID: 1", response.getEntity());

        // Verify the service method was called
        verify(financeService, times(1)).getFinanceDataById(1L);
    }

    @Test
    void testUpdateFinanceData() {
        // Mock the service method
        doNothing().when(financeService).updateFinanceData(anyLong(), any(FinanceData.class));

        // Call the controller method
        Response response = financeController.updateFinanceData(1L, financeData);

        // Verify the response
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Finance data updated successfully.", response.getEntity());

        verify(financeService, times(1)).updateFinanceData(1L, financeData);
    }

    @Test
    void testDeleteFinanceData() {
        // Mock the service method
        doNothing().when(financeService).deleteFinanceData(anyLong());

        // Call the controller method
        Response response = financeController.deleteFinanceData(1L);

        // Verify the response
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Finance data deleted successfully.", response.getEntity());

        // Verify the service method was called
        verify(financeService, times(1)).deleteFinanceData(1L);
    }

    @Test
    void testFetchAndSaveMultipleCurrencies_success() {
        // Mock the service method
        doNothing().when(financeService).fetchAndSaveMultipleCurrencies(anyString(), anyList());

        // Prepare test data
        String baseCurrency = "USD";
        List<String> targetCurrencies = Arrays.asList("EUR", "GBP", "JPY");

        // Call the controller method
        Response response = financeController.fetchAndSaveMultipleCurrencies(baseCurrency, "EUR,GBP,JPY");

        // Verify the response
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Exchange rate data for multiple currencies saved successfully.", response.getEntity());

        // Verify that the service method was called
        verify(financeService, times(1)).fetchAndSaveMultipleCurrencies("USD", targetCurrencies);
    }

    @Test
    void testFetchAndSaveMultipleCurrencies_missingBaseCurrency() {
        // Call the controller method with missing base currency
        Response response = financeController.fetchAndSaveMultipleCurrencies(null, "EUR,GBP,JPY");

        // Verify the response
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Base currency and target currencies are required.", response.getEntity());
    }

    @Test
    void testFetchAndSaveMultipleCurrencies_missingTargetCurrencies() {
        // Call the controller method with missing target currencies
        Response response = financeController.fetchAndSaveMultipleCurrencies("USD", "");

        // Verify the response
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Base currency and target currencies are required.", response.getEntity());
    }

    @Test
    void testUpdateExchangeRatesForMultipleCurrencies_success() {
        // Mock the service method
        doNothing().when(financeService).updateExchangeRatesForMultipleCurrencies(anyString(), anyMap());

        // Prepare test data
        Map<String, Double> targetCurrenciesRates = new HashMap<>();
        targetCurrenciesRates.put("EUR", 0.85);
        targetCurrenciesRates.put("GBP", 0.75);

        // Call the controller method
        Response response = financeController.updateExchangeRatesForMultipleCurrencies(targetCurrenciesRates);

        // Verify the response
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Exchange rates for multiple currencies updated successfully.", response.getEntity());

        // Verify that the service method was called
        verify(financeService, times(1)).updateExchangeRatesForMultipleCurrencies("INR", targetCurrenciesRates);
    }

    @Test
    void testUpdateExchangeRatesForMultipleCurrencies_missingRates() {
        // Call the controller method with empty target currencies
        Response response = financeController.updateExchangeRatesForMultipleCurrencies(new HashMap<>());

        // Verify the response
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Target currencies and exchange rates are required.", response.getEntity());
    }
}

package com.example.controller;

import com.example.model.FinanceData;
import com.example.service.FinanceService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/api/finance")
public class FinanceController {

    @Inject
    private FinanceService financeService;

    @POST
    @Path("/fetch-and-save")
    public Response fetchAndSaveExchangeRateData(
            @QueryParam("baseCurrency") String baseCurrency,
            @QueryParam("targetCurrency") String targetCurrency) {
        if (baseCurrency == null || targetCurrency == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Base and target currencies are required.").build();
        }
        financeService.fetchAndSaveExchangeRateData(baseCurrency, targetCurrency);
        return Response.ok("Exchange rate data saved successfully.").build();
    }

    @POST
    @Path("/fetch-and-save-multiple")
    public Response fetchAndSaveMultipleCurrencies(
            @QueryParam("baseCurrency") String baseCurrency,
            @QueryParam("targetCurrencies") String targetCurrencies) {
        if (baseCurrency == null || targetCurrencies == null || targetCurrencies.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Base currency and target currencies are required.").build();
        }

        List<String> targetCurrencyList = Arrays.asList(targetCurrencies.split(","));

        financeService.fetchAndSaveMultipleCurrencies(baseCurrency, targetCurrencyList);
        return Response.ok("Exchange rate data for multiple currencies saved successfully.").build();
    }


    @GET
    @Path("/all")
    public Response getAllFinanceData() {
        List<FinanceData> financeDataList = financeService.getAllFinanceData();
        return Response.ok(financeDataList).build();
    }

    @GET
    @Path("/{id}")
    public Response getFinanceDataById(@PathParam("id") Long id) {
        Optional<FinanceData> financeData = financeService.getFinanceDataById(id);
        return financeData.map(Response::ok)
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity("Finance data not found for ID: " + id)).build();
    }

    @GET
    @Path("/currency-pair")
    public Response getFinanceDataByCurrencyPair(
            @QueryParam("baseCurrency") String baseCurrency,
            @QueryParam("targetCurrency") String targetCurrency) {
        if (baseCurrency == null || targetCurrency == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Base and target currencies are required.").build();
        }
        Optional<FinanceData> financeData = financeService.getFinanceDataByCurrencyPair(baseCurrency, targetCurrency);
        return financeData.map(Response::ok)
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity("Finance data not found for currency pair: " + baseCurrency + " to " + targetCurrency)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateFinanceData(@PathParam("id") Long id, FinanceData financeData) {
        financeService.updateFinanceData(id, financeData);
        return Response.ok("Finance data updated successfully.").build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteFinanceData(@PathParam("id") Long id) {
        financeService.deleteFinanceData(id);
        return Response.ok("Finance data deleted successfully.").build();
    }

    @DELETE
    @Path("/all")
    public Response deleteAllFinanceData() {
        financeService.deleteAllFinanceData();
        return Response.ok("All finance data deleted successfully.").build();
    }

    @PUT
    @Path("/update-multiple")
    public Response updateExchangeRatesForMultipleCurrencies(Map<String, Double> targetCurrenciesRates) {
        if (targetCurrenciesRates == null || targetCurrenciesRates.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Target currencies and exchange rates are required.").build();
        }
        financeService.updateExchangeRatesForMultipleCurrencies("INR", targetCurrenciesRates);  // Example with INR as base currency
        return Response.ok("Exchange rates for multiple currencies updated successfully.").build();
    }
}

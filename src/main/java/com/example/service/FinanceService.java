package com.example.service;

import com.example.config.ExchangeApiProperties;
import com.example.dto.ExchangeRateResponseDTO;
import com.example.model.FinanceData;
import com.example.repository.FinanceDataRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class FinanceService {

    private static final Logger LOGGER = Logger.getLogger(FinanceService.class);

    private final String apiUrl;
    private final String apiKey;
    private final FinanceDataRepository financeDataRepository;

    public FinanceService(ExchangeApiProperties exchangeApiProperties, FinanceDataRepository financeDataRepository) {
        this.apiUrl = exchangeApiProperties.getUrl();
        this.apiKey = exchangeApiProperties.getKey();
        this.financeDataRepository = financeDataRepository;
    }

    @Transactional
    public void fetchAndSaveExchangeRateData(String baseCurrency, String targetCurrency) {
        String url = apiUrl.replace("{API_KEY}", apiKey) + "/" + baseCurrency;

        Client client = ClientBuilder.newClient();
        Response response = client.target(url).request().get();

        if (response.getStatus() == 200) {
            ExchangeRateResponseDTO exchangeRateResponse = response.readEntity(ExchangeRateResponseDTO.class);
            if (exchangeRateResponse.getConversion_rates().containsKey(targetCurrency)) {
                FinanceData data = new FinanceData();
                data.setBaseCurrency(baseCurrency);
                data.setTargetCurrency(targetCurrency);
                data.setExchangeRate(exchangeRateResponse.getConversion_rates().get(targetCurrency));

                financeDataRepository.persist(data);
                LOGGER.info("Exchange rate data saved successfully.");
            } else {
                LOGGER.warn("Target currency not found in the response.");
            }
        } else {
            LOGGER.error("Error fetching data from Exchange Rate API. Status: " + response.getStatus());
        }
    }

    public List<FinanceData> getAllFinanceData() {
        return financeDataRepository.listAll();
    }

    public Optional<FinanceData> getFinanceDataById(Long id) {
        return Optional.ofNullable(financeDataRepository.findById(id));
    }

    @Transactional
    public void updateFinanceData(Long id, FinanceData financeData) {
        Optional<FinanceData> existingData = getFinanceDataById(id);
        if (existingData.isPresent()) {
            FinanceData existing = existingData.get();
            existing.setBaseCurrency(financeData.getBaseCurrency());
            existing.setTargetCurrency(financeData.getTargetCurrency());
            existing.setExchangeRate(financeData.getExchangeRate());
            LOGGER.info("Finance data updated successfully.");
        } else {
            LOGGER.warn("Finance data with ID " + id + " not found.");
        }
    }

    @Transactional
    public void deleteFinanceData(Long id) {
        financeDataRepository.deleteById(id);
        LOGGER.info("Finance data deleted successfully.");
    }

    @Transactional
    public void deleteAllFinanceData() {
        financeDataRepository.deleteAll();
        LOGGER.info("All finance data deleted successfully.");
    }

    @Transactional
    public void fetchAndSaveMultipleCurrencies(String baseCurrency, List<String> targetCurrencies) {
        for (String targetCurrency : targetCurrencies) {
            fetchAndSaveExchangeRateData(baseCurrency, targetCurrency);
        }
    }

    public Optional<FinanceData> getFinanceDataByCurrencyPair(String baseCurrency, String targetCurrency) {
        List<FinanceData> data = financeDataRepository.find("baseCurrency = ?1 and targetCurrency = ?2", baseCurrency, targetCurrency).list();
        if (!data.isEmpty()) {
            return Optional.of(data.get(0));
        }
        return Optional.empty();
    }

    @Transactional
    public void updateExchangeRatesForMultipleCurrencies(String baseCurrency, Map<String, Double> targetCurrenciesRates) {
        for (Map.Entry<String, Double> entry : targetCurrenciesRates.entrySet()) {
            String targetCurrency = entry.getKey();
            Double newExchangeRate = entry.getValue();

            Optional<FinanceData> existingData = getFinanceDataByCurrencyPair(baseCurrency, targetCurrency);
            if (existingData.isPresent()) {
                FinanceData existing = existingData.get();
                existing.setExchangeRate(newExchangeRate);
                LOGGER.info("Exchange rate updated for " + baseCurrency + " to " + targetCurrency);
            } else {
                LOGGER.warn("No data found for the currency pair: " + baseCurrency + " to " + targetCurrency);
            }
        }
    }
}

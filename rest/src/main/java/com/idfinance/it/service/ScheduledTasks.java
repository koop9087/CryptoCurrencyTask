package com.idfinance.it.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idfinance.it.pojo.CryptoCurrency;
import com.idfinance.it.pojo.User;
import com.idfinance.it.wrapper.CryptoCurrencyWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@EnableScheduling
@Service
@PropertySource({"classpath:cryptocurrency.json"})
public class ScheduledTasks {
    public static final Logger LOGGER = Logger.getLogger(ScheduledTasks.class.getName());
    public static final String COIN_INFO_URL = "https://api.coinlore.net/api/ticker/?id=%d";
    public static final String USER_COIN_INFO = "user:%S, id=%S, percent=%S";
    public static final String CRYPTOCURRENCY_JSON_FILE_PATH = "cryptocurrency.json";
    public static final String COIN_SERVICE_INFO_MESSAGE = "The coin service did not return the value";

    private List<Long> cryptoCurrencyIds = new ArrayList<>();

    @Autowired
    private CryptoCurrencyService cryptoCurrencyService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Bean
    public void saveCryptoCurrencyInDB() {
        try {
            Resource resource = new ClassPathResource(CRYPTOCURRENCY_JSON_FILE_PATH);
            ObjectMapper objectMapper = new ObjectMapper();
            CryptoCurrencyFromFile cryptoCurrencyFromFile = objectMapper.readValue(resource.getFile(),
                    CryptoCurrencyFromFile.class);
            List<CryptoCurrency> cryptoCurrencies = cryptoCurrencyFromFile.getCurrency();
            for (CryptoCurrency cryptoCurrency : cryptoCurrencies) {
                cryptoCurrencyService.save(cryptoCurrency);
                cryptoCurrencyIds.add(cryptoCurrency.getId());
            }
        } catch (IOException e) {
            LOGGER.log(Level.INFO, e.getMessage());
        }
    }

    @Scheduled(fixedRate = 60000)
    public void updateDateInDB() {
        for (Long id : cryptoCurrencyIds) {
            multiUpdateCurrency(id);
        }
    }

    private void multiUpdateCurrency(Long id) {
        CryptoCurrencyWrapper[] response = restTemplate
                .getForEntity(String.format(COIN_INFO_URL, id), CryptoCurrencyWrapper[].class).getBody();
        if (response != null && response.length != 0) {
            cryptoCurrencyService.update(response[0]);
            checkUserCurrencyPrice();
        } else {
            LOGGER.log(Level.WARNING, COIN_SERVICE_INFO_MESSAGE);
        }
    }

    private void checkUserCurrencyPrice() {
        List<User> users = userService.readAllUsers();
        for (User user : users) {
            BigDecimal userCurrencyValue = cryptoCurrencyService.readCurrencyBySymbol(user.getSymbol()).getPrice();
            BigDecimal realCurrencyValue = user.getPrice();
            BigDecimal difference = realCurrencyValue.subtract(userCurrencyValue).abs();
            BigDecimal percent = difference.multiply(BigDecimal.valueOf(100)).divide(userCurrencyValue,3,
                    RoundingMode.DOWN);
            if (percent.compareTo(BigDecimal.ONE) >= 0) {
                LOGGER.log(Level.WARNING, String.format(USER_COIN_INFO,
                        user.getUsername(), user.getSymbol(), percent));
            }
        }
    }
}

package com.idfinance.it.service;

import com.idfinance.it.pojo.CryptoCurrency;
import com.idfinance.it.wrapper.CryptoCurrencyWrapper;

import java.util.List;

public interface CryptoCurrencyService {
    void save(CryptoCurrency cryptoCurrency);

    CryptoCurrency read(Long id);

    void update(CryptoCurrencyWrapper cryptoCurrencyWrapper);

    void delete(CryptoCurrency cryptoCurrency);

    List<CryptoCurrency> findAll();

    CryptoCurrency readCurrencyBySymbol(String symbol);
}

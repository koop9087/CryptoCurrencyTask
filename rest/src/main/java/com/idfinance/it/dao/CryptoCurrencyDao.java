package com.idfinance.it.dao;

import com.idfinance.it.pojo.CryptoCurrency;
import com.idfinance.it.wrapper.CryptoCurrencyWrapper;

import java.util.List;
import java.util.Optional;

public interface CryptoCurrencyDao {
    void save(CryptoCurrency cryptoCurrency);

    Optional<CryptoCurrency> read(Long id);

    void update(CryptoCurrencyWrapper cryptoCurrencyWrapper);

    void delete(CryptoCurrency cryptoCurrency);

    List<CryptoCurrency> findAll();

    CryptoCurrency readCurrencyBySymbol(String symbol);
}

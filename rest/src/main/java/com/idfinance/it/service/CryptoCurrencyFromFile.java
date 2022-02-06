package com.idfinance.it.service;

import com.idfinance.it.pojo.CryptoCurrency;

import java.util.List;

public class CryptoCurrencyFromFile {

    private List<CryptoCurrency> currency;

    public List<CryptoCurrency> getCurrency() {
        return currency;
    }

    public void setCurrency(List<CryptoCurrency> currency) {
        this.currency = currency;
    }
}

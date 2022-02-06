package com.idfinance.it.service;

import com.idfinance.it.dao.CryptoCurrencyDao;
import com.idfinance.it.pojo.CryptoCurrency;
import com.idfinance.it.wrapper.CryptoCurrencyWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.NESTED)
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {
    @Autowired
    private CryptoCurrencyDao cryptoCurrencyDao;

    @Override
    public void save(CryptoCurrency cryptoCurrency) {
        this.cryptoCurrencyDao.save(cryptoCurrency);
    }

    @Override
    public CryptoCurrency read(Long id) {
        return cryptoCurrencyDao.read(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public void update(CryptoCurrencyWrapper cryptoCurrencyWrapper) {
        this.cryptoCurrencyDao.update(cryptoCurrencyWrapper);
    }

    @Override
    public void delete(CryptoCurrency cryptoCurrency) {
        this.cryptoCurrencyDao.delete(cryptoCurrency);
    }

    @Override
    public List<CryptoCurrency> findAll() {
        return this.cryptoCurrencyDao.findAll();
    }

    @Override
    public CryptoCurrency readCurrencyBySymbol(String symbol) {
        return this.cryptoCurrencyDao.readCurrencyBySymbol(symbol);
    }
}

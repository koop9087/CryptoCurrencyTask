package com.idfinance.it.dao;

import com.idfinance.it.pojo.CryptoCurrency;
import com.idfinance.it.wrapper.CryptoCurrencyWrapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CryptoCurrencyDaoImpl implements CryptoCurrencyDao {
    public static final String ERROR_READ_MESSAGE = "Can't read this object because it's don't exist";
    public static final String CRYPTO_CURRENCY_QUERY = "from CryptoCurrency";
    public static final String CRYPTO_CURRENCY_QUERY_WITH_SYMBOL = "from CryptoCurrency where symbol=:symbol";
    public static final String SYMBOL_QUERY_PARAM = "symbol";

    @Autowired
    @Qualifier("customSessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public void save(CryptoCurrency cryptoCurrency) {
        Session session = this.sessionFactory.getCurrentSession();
        session.save(cryptoCurrency);
    }

    @Override
    public Optional<CryptoCurrency> read(Long id) {
        Session session = this.sessionFactory.getCurrentSession();
        Optional<CryptoCurrency> cryptoCurrencyOptional;
        CryptoCurrency loadedCryptoCurrency = session.get(CryptoCurrency.class, id);
        if (loadedCryptoCurrency != null) {
            cryptoCurrencyOptional = Optional.of(loadedCryptoCurrency);
        } else {
            throw new RuntimeException(ERROR_READ_MESSAGE);
        }
        return cryptoCurrencyOptional;
    }

    @Override
    public void update(CryptoCurrencyWrapper cryptoCurrencyWrapper) {
        Session session = this.sessionFactory.getCurrentSession();
        CryptoCurrency cryptoCurrency = new CryptoCurrency();
        cryptoCurrency.setId(cryptoCurrencyWrapper.getId());
        cryptoCurrency.setSymbol(cryptoCurrencyWrapper.getSymbol());
        cryptoCurrency.setPrice(cryptoCurrencyWrapper.getPrice_usd());
        session.update(cryptoCurrency);
    }

    @Override
    public void delete(CryptoCurrency cryptoCurrency) {
        Session session = this.sessionFactory.getCurrentSession();
        session.delete(cryptoCurrency);
    }

    @Override
    public List<CryptoCurrency> findAll() {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createQuery(CRYPTO_CURRENCY_QUERY, CryptoCurrency.class).list();
    }

    @Override
    public CryptoCurrency readCurrencyBySymbol(String symbol) {
        Session session = this.sessionFactory.getCurrentSession();
        return (CryptoCurrency) session
                .createQuery(CRYPTO_CURRENCY_QUERY_WITH_SYMBOL)
                .setParameter(SYMBOL_QUERY_PARAM, symbol)
                .getSingleResult();
    }
}

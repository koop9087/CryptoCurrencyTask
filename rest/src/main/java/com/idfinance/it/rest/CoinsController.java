package com.idfinance.it.rest;

import com.idfinance.it.pojo.CryptoCurrency;
import com.idfinance.it.pojo.User;
import com.idfinance.it.service.CryptoCurrencyService;
import com.idfinance.it.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class CoinsController {

    @Autowired
    private CryptoCurrencyService cryptoCurrencyService;

    @Autowired
    private UserService userService;

    @GetMapping("/coins")
    public ResponseEntity<List<CryptoCurrency>> getAllCryptoCoins() {
        List<CryptoCurrency> cryptoCurrencies = cryptoCurrencyService.findAll();
        if (cryptoCurrencies.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cryptoCurrencies, HttpStatus.OK);
    }

    @GetMapping("/coins/{id}")
    public ResponseEntity<CryptoCurrency> getCoin(@PathVariable("id") Long id) {
        CryptoCurrency cryptoCurrency = cryptoCurrencyService.read(id);
        if (cryptoCurrency == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cryptoCurrency, HttpStatus.OK);
    }

    @PostMapping("/coins/{username}/{symbol}")
    public ResponseEntity<Serializable> notify(@PathVariable("username") String username,
                                               @PathVariable("symbol") String symbol) {
        User user = new User();
        user.setUsername(username);
        user.setSymbol(symbol);
        user.setPrice(cryptoCurrencyService.readCurrencyBySymbol(symbol).getPrice());
        Serializable id = userService.saveUser(user);
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
